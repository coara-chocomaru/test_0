const characterText = document.getElementById("characterText");
const cryingImage = document.getElementById("cryingImage");
const animationButton = document.getElementById("animationButton");
const characterImage = document.querySelector(".character-img");
let isSpinning = false;
let spinCount = 0;

// 時間帯ごとにセリフを変更
function updateGreeting() {
    const hour = new Date().getHours();
    if (hour < 12) {
        characterText.textContent = "おはよう！";
    } else if (hour < 18) {
        characterText.textContent = "こんにちは！";
    } else {
        characterText.textContent = "こんばんは！";
    }
}

// ボタンがクリックされた時の処理
animationButton.addEventListener("click", function() {
    if (!isSpinning) {
        isSpinning = true;
        spinCount++;
        cryingImage.style.visibility = "hidden"; // 涙を非表示
        characterText.textContent = "くるくる回ってるよ〜！";
        characterImage.classList.add("animate");  // 回転アニメーションを追加

        setTimeout(() => {
            characterImage.classList.remove("animate");  // 回転アニメーションを削除
            characterText.textContent = "終わったよ！";
            isSpinning = false;

            if (spinCount > 3) {
                cryingImage.style.visibility = "visible";
                characterText.textContent = "くるくる〜〜";
            }
        }, 3000);
    }
});

// 初回読み込み時に時間帯ごとのセリフを表示
updateGreeting();
