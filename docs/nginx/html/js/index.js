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
        for (let i = 0; i < cookieArr.length; i++) {
            let cookiePair = cookieArr[i].split("=");
            if (name == cookiePair[0].trim()) {
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

    // 轮播图逻辑
    const swiperWrapper = document.querySelector('.swiper-wrapper');
    const pagination = document.querySelector('.swiper-pagination');
    let currentIndex = 0;

// 创建分页点
    for (let i = 0; i < 3; i++) {
        const dot = document.createElement('div');
        dot.className = `swiper-dot${i === 0 ? ' active' : ''}`;
        pagination.appendChild(dot);
    }

// 自动轮播
    setInterval(() => {
        currentIndex = (currentIndex + 1) % 3;
        swiperWrapper.style.transform = `translateX(-${currentIndex * 100}%)`;
        document.querySelectorAll('.swiper-dot').forEach((dot, index) => {
            dot.className = `swiper-dot${index === currentIndex ? ' active' : ''}`;
        });
    }, 3000);

// 倒计时逻辑（完整实现）
    class Countdown {
        constructor(element, initialTime) {
            this.element = element;
            this.remaining = this.parseTime(initialTime);
            this.timer = null;
            this.start();
        }

        parseTime(timeString) {
            const [hours, minutes, seconds] = timeString.split(':').map(Number);
            return hours * 3600 + minutes * 60 + seconds;
        }

        formatTime(seconds) {
            const h = Math.floor(seconds / 3600).toString().padStart(2, '0');
            const m = Math.floor((seconds % 3600) / 60).toString().padStart(2, '0');
            const s = (seconds % 60).toString().padStart(2, '0');
            return `${h}:${m}:${s}`;
        }

        update() {
            this.remaining--;
            if (this.remaining <= 0) {
                this.element.textContent = '00:00:00';
                this.stop();
                return;
            }
            this.element.textContent = this.formatTime(this.remaining);
        }

        start() {
            this.timer = setInterval(() => this.update(), 1000);
        }

        stop() {
            clearInterval(this.timer);
        }
    }

    // 初始化所有倒计时
    document.querySelectorAll('.countdown').forEach(el => {
        new Countdown(el, el.textContent);
    })
})