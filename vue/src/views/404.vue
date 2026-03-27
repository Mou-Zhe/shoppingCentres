<template>
  <div>
    <div style="height: 100vh;display: flex; justify-content: center;align-items: center">
      <div style="width: 30%">
        <img src="@/assets/imgs/4044.jpg" alt="" style="width: 100%">
        <div style="font-size: 30px; text-align: center">找不到页面啦！<router-link to="/">请返回主页</router-link></div>
      </div>
    </div>
  </div>
</template>

<!--@Transactional  //代码抛异常 → 数据库操作自动撤销立即回滚，具有原子性，电商、支付、购物车、订单的add必须加，否则会出大问题-->
<!--iter快捷for循环-->
<!--// Ctrl + Alt + V → 生成局部变量-->
<!--//Ctrl + Alt + F → 生成成员变量-->
<!--//Ctrl + Alt + C → 生成常量-->
<!--//Ctrl + Alt + L → 格式化代码-->


<!--1. 回退到上一个版本-->
<!--powershell-->
<!--git reset &#45;&#45;hard HEAD^-->
<!--git push -f origin main-->
<!--2. 回退到当前这个版本（你现在的 V6）-->
<!--powershell-->
<!--git reset &#45;&#45;hard 10fc0a4-->
<!--git push -f origin main-->
<!--3. 下载 GitHub 最新代码到本地-->
<!--powershell-->
<!--git pull origin main-->
<!--4. 提交新代码并上传-->
<!--powershell-->
<!--git add .-->
<!--git commit -m "谋者更新"-->
<!--git push origin main-->