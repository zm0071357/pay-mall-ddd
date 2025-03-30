document.addEventListener("DOMContentLoaded", async () => {
    const modal = document.getElementById("paymentModal")
    const paymentAmount = document.getElementById("paymentAmount")
    const cancelPayment = document.getElementById("cancelPayment")
    const completePayment = document.getElementById("completePayment")
    const qrCodeImg = document.querySelector(".qr-code") // 获取二维码图片元素
    const userId = getCookie("username") || "lzm0071357"; // 测试用默认值
    let marketData = null;

    // 新增：调用试算接口
    try {
        const response = await fetch("http://localhost:8091/api/gbm/index/query_group_buy_market_config", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                userId: userId,
                source: "s01",
                channel: "c01",
                goodsId: "9890001"
            })
        });
        const result = await response.json();

        if (result.code === "0000") {
            marketData = result.data;
            // 更新促销信息
            document.getElementById('promotionText').textContent =
                `直降 ¥${(marketData.goods.originalPrice - marketData.goods.payPrice).toFixed(2)} 
                ${marketData.teamStatistic.allTeamUserCount}人再抢，参与马上抢到`;

            // 更新按钮价格
            document.querySelector('.buy-alone').innerHTML =
                `单独购买(￥${marketData.goods.originalPrice.toFixed(2)})`;
            document.querySelector('.group-buy').innerHTML =
                `开团购买(￥${marketData.goods.payPrice.toFixed(2)})`;

            // 新增：动态生成拼单列表
            const groupList = document.querySelector('.group-list');
            groupList.innerHTML = ''; // 清空原有内容

            marketData.teamList.forEach(team => {
                const remaining = team.targetCount - team.lockCount;

                const groupItem = document.createElement('div');
                groupItem.className = 'group-item';
                groupItem.innerHTML = `
            <div>
                <div class="user-info">${team.userId}</div>
                <div class="group-status">
                    <span>组队仅剩${remaining}人，拼单即将结束</span>
                    <span class="countdown">${team.validTimeCountdown}</span>
                </div>
            </div>
            <div class="right">
                <button class="group-btn" 
                        data-price="${marketData.goods.payPrice.toFixed(2)}">
                    参与拼团
                </button>
            </div>
        `;

                groupList.appendChild(groupItem);
            });

            // 重新初始化倒计时（需要调整原有倒计时逻辑）
            document.querySelectorAll('.countdown').forEach(el => {
                new Countdown(el, el.textContent);
            });
        }
    } catch (error) {
        console.error("试算接口调用失败:", error);
    }

    // 获取所有的按钮
    const buttons = document.querySelectorAll(".group-btn, .action-btn")

    // 修改后的按钮点击事件（原代码调整）
    document.querySelectorAll(".action-btn").forEach(button => {
        button.addEventListener("click", async function() {
            if (!userId) {
                window.location.href = "login.html";
                return;
            }

            const isGroupBuy = this.classList.contains('group-buy');
            const price = isGroupBuy ? marketData.goods.payPrice : marketData.goods.originalPrice;

            try {
                const response = await fetch("http://localhost:8099/api/pay/create_pay_order", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        userId: userId,
                        productId: "9890001",
                        activityId: marketData.activityId,
                        marketType: isGroupBuy ? 1 : 0
                    })
                });

                const result = await response.json();
                if (result.code === "0000") {
                    qrCodeImg.src = result.data;
                    paymentAmount.textContent = `支付金额：￥${price.toFixed(2)}`;
                    modal.style.display = "block";
                } else {
                    alert(`支付失败：${result.info}`);
                }
            } catch (error) {
                console.error("支付请求失败:", error);
                alert("支付请求失败，请稍后重试");
            }
        });
    });

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
            // 新增：处理带小时的时间格式
            this.remaining = this.parseTime(initialTime.includes(':') ? initialTime : `00:${initialTime}`);
            this.timer = null;
            this.start();
        }

        parseTime(timeString) {
            const parts = timeString.split(':');
            if (parts.length === 2) parts.unshift('00'); // 处理mm:ss格式
            const [hours, minutes, seconds] = parts.map(Number);
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