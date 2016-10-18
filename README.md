# WeixinNativePay
网站集成微信扫码支付方式

1.首先去（https://pay.weixin.qq.com）微信支付官网申请一个商户号。

2.创建一个javabean用于封装微信使用的参数
 # WxPay.java
 
3.在Action创建一个跳转到二维码支付页面的方法
 # struts2 示例 PayAction
 # WxUtils 微信核心工具类
  
 
 4.jsp页面部分
  # qrcode.jsp

 5.根据返回的code url通过二维码显示在页面上，然后轮询结果，如果支付成功则跳转至成功页面，否则至失败页面。
  #natvie.js
  
 
