(function() {
    const _0x1a2b3c = document.getElementById("characterText");
    const _0x4d5e6f = document.getElementById("heartEffect");
    const _0x7f8a9b = document.getElementById("animationButton");
    const _0x0d0e0f = document.querySelector(".character-img");
    let _0xaaaa = false;
    let _0xbbbb = 0;

    
    function _0x12ab34() {
        const _0xcdeff = new Date().getHours();
        if (_0xcdeff < 12) {
            _0x1a2b3c.textContent = "おはよう！";
        } else if (_0xcdeff < 18) {
            _0x1a2b3c.textContent = "こんにちは！";
        } else {
            _0x1a2b3c.textContent = "こんばんは！";
        }
    }

    
    _0x7f8a9b.addEventListener("click", function() {
        if (!_0xaaaa) {
            _0xaaaa = true;
            _0xbbbb++;
            _0x4d5e6f.style.visibility = "hidden";
            _0x1a2b3c.textContent = "くるくる回ってるよ〜！";
            _0x0d0e0f.classList.add("animate");

            setTimeout(() => {
                _0x0d0e0f.classList.remove("animate");
                _0x1a2b3c.textContent = "終わったよ！";
                _0xaaaa = false;

                if (_0xbbbb > 3) {
                    _0x4d5e6f.style.visibility = "visible"; 
                    _0x1a2b3c.textContent = "くるくる〜〜";
                }
            }, 3000);
        }
    });

    
    _0x12ab34(); 
})();
