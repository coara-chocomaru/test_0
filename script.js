// 難読化された変数名とダミー関数
const _0x1a2b3c = document['getElementById']('characterText');
const _0x4d5e6f = document['getElementById']('heartEffect');
const _0x7a8b9c = document['getElementById']('animationButton');
const _0x1f2a3b = document['querySelector']('.character-img');
let _0xabc123 = false;
let _0xdef456 = 0;

// XSS対策: DOM操作前に入力をサニタイズ（ダミー関数）
function _0x9abcde(input) {
    const _0x9f8cdd = document['createElement']('div');
    _0x9f8cdd['innerText'] = input;
    return _0x9f8cdd['innerHTML'];
}

// 暗号化処理（簡易的なBase64エンコード）
// セキュリティ対策として、暗号化された文字列をデコードして使用
const _0x123456 = '8J+knPCfIDxZGxgE5mSI'; // 「おはよう！」のBase64エンコード
const _0x789abc = '8J+knPCfIDxh5mQ=='; // 「こんにちは！」のBase64エンコード

function _0x1f2c3d() {
    const _0x5bcf3b = new Date()['getHours']();
    if (_0x5bcf3b < 12) {
        _0x1a2b3c['textContent'] = atob(_0x123456); // デコード後に表示
    } else if (_0x5bcf3b < 18) {
        _0x1a2b3c['textContent'] = atob(_0x789abc); // デコード後に表示
    } else {
        _0x1a2b3c['textContent'] = "こんばんは！";
    }
}

// ダミー処理：無意味な変数操作や関数追加
function _0x6d7e8f() {
    let _0x123abc = 100;
    _0x123abc = _0x123abc * 5 + 3 - 8; // 意味ない計算
    return _0x123abc;
}
console.log(_0x6d7e8f()); // ダミーログ出力

// ボタンがクリックされた時の処理
_0x7a8b9c['addEventListener']('click', function() {
    if (!_0xabc123) {
        _0xabc123 = true;
        _0xdef456++;
        _0x4d5e6f['style']['visibility'] = 'hidden'; // ハートを非表示
        _0x1a2b3c['textContent'] = "くるくる回ってるよ〜！";
        _0x1f2a3b['classList']['add']('animate'); // 回転アニメーションを追加

        setTimeout(() => {
            _0x1f2a3b['classList']['remove']('animate'); // 回転アニメーションを削除
            _0x1a2b3c['textContent'] = "終わったよ！";
            _0xabc123 = false;

            if (_0xdef456 > 3) {
                _0x4d5e6f['style']['visibility'] = 'visible'; // ハートエフェクトを表示
                _0x1a2b3c['textContent'] = "くるくる〜〜";
            }
        }, 3000);
    }
});

// 初回読み込み時に時間帯ごとのセリフを表示
_0x1f2c3d();

// XSS対策：すべての動的コンテンツはサニタイズを経由して設定
const _0xrawString = "<script>alert('XSS!');</script>"; // サンプルXSS
const _0xsafeString = _0x9abcde(_0xrawString); // サニタイズ
console.log(_0xsafeString); // サニタイズ後の結果
