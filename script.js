const _0x1b2c3d = document.getElementById("characterText");
const _0x4e5f6g = document.getElementById("heartEffect");
const _0x7a8b9c = document.getElementById("animationButton");
const _0x1f2d3e = document.querySelector(".character-img");
let _0xaaa1b2 = false;
let _0xbbccdd = 0;

// 時間帯ごとにセリフを変更
function _0x234567() {
    const _0x5d6789 = new Date().getHours();
    if (_0x5d6789 < 12) {
        _0x1b2c3d.textContent = "おはよう！";
    } else if (_0x5d6789 < 18) {
        _0x1b2c3d.textContent = "こんにちは！";
    } else {
        _0x1b2c3d.textContent = "こんばんは！";
    }
}

// ボタンがクリックされた時の処理
_0x7a8b9c.addEventListener("click", function() {
    if (!_0xaaa1b2) {
        _0xaaa1b2 = true;
        _0xbbccdd++;
        _0x4e5f6g.style.visibility = "hidden"; // ハートを非表示
        _0x1b2c3d.textContent = "くるくる回ってるよ〜！";
        _0x1f2d3e.classList.add("animate");  // 回転アニメーションを追加

        setTimeout(() => {
            _0x1f2d3e.classList.remove("animate");  // 回転アニメーションを削除
            _0x1b2c3d.textContent = "終わったよ！";
            _0xaaa1b2 = false;

            if (_0xbbccdd > 3) {
                _0x4e5f6g.style.visibility = "visible"; // ハートエフェクトを表示
                _0x1b2c3d.textContent = "くるくる〜〜";
            }
        }, 3000);
    }
});

// 初回読み込み時に時間帯ごとのセリフを表示
_0x234567();
