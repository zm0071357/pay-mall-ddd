document.addEventListener("DOMContentLoaded", () => {
  const loginForm = document.getElementById("loginForm")
  const errorMessage = document.getElementById("errorMessage")

  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault()

    const username = document.getElementById("username").value.trim()
    const password = document.getElementById("password").value.trim()

    try {
      const response = await fetch("http://localhost:8099/api/login/doLogin", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          account: username,
          password: password
        })
      })

      const data = await response.json()

      if (data.code === '0000' && data.data.isSuccess === 0) {
        // 登录成功处理
        errorMessage.style.display = "none"

        // 设置Cookie
        const expirationDate = new Date()
        expirationDate.setDate(expirationDate.getDate() + 1)
        document.cookie = `username=${username}; expires=${expirationDate.toUTCString()}; path=/`

        // 跳转首页
        window.location.href = "index.html"
      } else {
        // 显示后端返回的错误信息
        errorMessage.textContent = data.info || "登录失败，请检查账号密码"
        errorMessage.style.display = "block"
      }
    } catch (error) {
      console.error("登录请求失败:", error)
      errorMessage.textContent = "网络错误，请稍后重试"
      errorMessage.style.display = "block"
    }
  })
})