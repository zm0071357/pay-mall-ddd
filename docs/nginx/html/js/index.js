document.addEventListener("DOMContentLoaded", () => {
    const modal = document.getElementById("paymentModal")
    const paymentAmount = document.getElementById("paymentAmount")
    const cancelPayment = document.getElementById("cancelPayment")
    const completePayment = document.getElementById("completePayment")
    const qrCodeImg = document.querySelector(".qr-code") // 获取二维码图片元素

    // 获取所有的按钮
    const buttons = document.querySelectorAll(".group-btn, .action-btn")

    // 为每个按钮添加点击事件
    buttons.forEach((button) => {
        button.addEventListener("click", async function () {
            var userId = getCookie("username");
            if (!userId) {
                window.location.href = "login.html";
                return;
            }

            const price = this.getAttribute("data-price")
            try {
                // 调用创建支付订单接口
                const response = await fetch("http://localhost:8099/api/pay/create_pay_order", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        userId: userId,
                        productId: "10001" // 固定productId为10001
                    })
                });

                const result = await response.json();
                if (result.code === "0000") { // 假设成功code为0000
                    // 更新二维码图片和支付金额
                    qrCodeImg.src = result.data;
                    paymentAmount.textContent = `支付金额：￥${price}`;
                    modal.style.display = "block";
                } else {
                    alert(`支付失败：${result.info}`);
                }
            } catch (error) {
                console.error("支付请求失败:", error);
                alert("支付请求失败，请稍后重试");
            }
        })
    })

    // 隐藏支付弹窗
    function hidePaymentModal() {
        modal.style.display = "none"
    }

    // 取消支付
    cancelPayment.addEventListener("click", hidePaymentModal)

    // 支付完成
    completePayment.addEventListener("click", () => {
        alert("支付成功！")
        hidePaymentModal()
    })

    function getCookie(name) {
        let cookieArr = document.cookie.split(";");
        for(let i = 0; i < cookieArr.length; i++) {
            let cookiePair = cookieArr[i].split("=");
            if(name == cookiePair[0].trim()) {
                return decodeURIComponent(cookiePair[1]);
            }
        }
        return null;
    }

    // 点击弹窗外部关闭弹窗
    window.addEventListener("click", (event) => {
        if (event.target == modal) {
            hidePaymentModal()
        }
    })

    // 保持原有的轮播图和倒计时逻辑...
    // [原有轮播图和倒计时代码保持不变]
})