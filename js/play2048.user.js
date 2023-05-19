// ==UserScript==
// @name         2048 Solver
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  Solve 2048.
// @author       bitfexl
// @match        https://play2048.co/
// @icon         https://www.google.com/s2/favicons?sz=64&domain=play2048.co
// @grant        GM_xmlhttpRequest
// @connect      localhost
// ==/UserScript==

(async function () {
    "use strict";

    const oldEmit = KeyboardInputManager.prototype.emit;
    KeyboardInputManager.prototype.emit = function (e, t) {
        window.game = this;
        return oldEmit.apply(this, [e, t]);
    };

    function parse(gameState) {
        let bytesSaveStateVertical = [];

        for (let col of gameState.grid.cells) {
            for (let field of col) {
                if (field == null) {
                    bytesSaveStateVertical.push(0);
                } else {
                    bytesSaveStateVertical.push(Math.log2(field.value));
                }
            }
        }

        let bytesSaveStateHorizontal = [];

        for (let r = 0; r < 4; r++) {
            for (let c = 0; c < 4; c++) {
                bytesSaveStateHorizontal.push(bytesSaveStateVertical[c * 4 + r]);
            }
        }

        return {
            score: gameState.score,
            board: {
                value: bytesSaveStateHorizontal,
            },
        };
    }

    async function requestMove(boardArr) {
        return new Promise((resolve) => {
            GM_xmlhttpRequest({
                method: "POST",
                url: "http://localhost:3000/solve",
                data: JSON.stringify(boardArr),
                onload(response) {
                    resolve(response.responseText);
                },
            });
        });
    }

    async function clearCache() {
        return new Promise((resolve) => {
            GM_xmlhttpRequest({
                method: "GET",
                url: "http://localhost:3000/clearCache",
                onload: resolve,
            });
        });
    }

    function restart() {
        document.querySelector("body > div.container > div.above-game > a").click();
    }

    setTimeout(restart, 100);

    while (true) {
        if (localStorage.getItem("gameState") == null) {
            await clearCache();
            await new Promise((resolve) => setTimeout(resolve, 4000));
            restart();
        }

        let moves = {
            UP: 0,
            RIGHT: 1,
            DOWN: 2,
            LEFT: 3,
        };

        let move = await requestMove(parse(JSON.parse(localStorage.getItem("gameState"))));

        console.log(move);

        window.game.emit("move", moves[move]);
    }
})();
