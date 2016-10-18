# WeixinNativePay
网站集成微信扫码支付方式

1.首先去（https://pay.weixin.qq.com）微信支付官网申请一个商户号。

2.创建一个javabean用于封装
 # WxPay.java
 public class WxPay {
  private String orderId;
	private String totalFee;
	private String spbillCreateIp; // 订单生成的机器 IP
	private String notifyUrl; // 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等
	private String body; // 商品描述根据情况修改
	private String openId; // 微信用户对一个公众号唯一
  
  public String getOrderId() {
		return orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public String getTotalFee() {
		return totalFee;
	}
	
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	
	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}
	
	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}
	
	public String getNotifyUrl() {
		return notifyUrl;
	}
	
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getOpenId() {
		return openId;
	}
	
	public void setOpenId(String openId) {
		this.openId = openId;
	}
 }

3.在Action创建一个跳转到二维码支付页面的方法，如下
 # struts2 示例 PayAction
 @Action("wxNativePay")
 public String wxNativePay() {
    try {
      Order order = getOrderByNo();
      WxPay wxPay = new WxPay();
      wxpay.setBody(order.getName(isEnLanguage));
			wxpay.setOrderId(order.getPayNo());
			wxpay.setSpbillCreateIp(WxUtils.localIp()); // 根据情况自己设定
			wxpay.setTotalFee(Math.round(getOrderAmount(order).doubleValue()+"");
			String codeUrl = WxUtils.getCodeurl(wxpay, "http://www.xxx.com/callback/wxReturn");
    } catch(Exception e) {
      setErrorCode(ErrorCode.PARAM_ERROR);
    }
    return "wxNativePay";
 }
 
 # WxUtils
  /** 获取微信扫码支付二维码链接 */
  public static String getCodeurl(WxPay wxPay, String notifyUrl) {
    String attach = "XXX"; // 附加数据，原样返回，公司名标识那些
    String trade_type = "NATIVE";
    String spbill_create_ip = wxPay.getSpbillCreateIp();
    
    String mch_id = "23156421"; // 申请的商户号
    String nonce_str = getNonceStr(); // 随机字符串
		String out_trade_no = wxPay.getOrderId();
    String body = wxPay.getBody();
    String totalFee = getMoney(wxPay.getTotalFee()); // 总金额以分为单位，不带小数点，自己可以写方法过滤一下
    
    SortedMap<String, String> packageParams = new TreeMap<String, String>();
    packageParams.put("appid", WECHAT_APP_ID);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notifyUrl);
		packageParams.put("trade_type", trade_type);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(WECHAT_APP_ID, WECHAT_APP_SECRET, WECHAT_PARTNERKEY);
    
    String sign = reqHandler.createSign(packageParams);
    String xml = "<xml>" + "<appid>" + WECHAT_APP_ID + "</appid>" + "<mch_id>" + mch_id + "</mch_id>"
				+ "<nonce_str>" + nonce_str + "</nonce_str>" + "<sign>" + sign + "</sign>" + "<body><![CDATA[" + body
				+ "]]></body>" + "<out_trade_no>" + out_trade_no + "</out_trade_no>" + "<attach>" + attach
				+ "</attach>" + "<total_fee>" + totalFee + "</total_fee>" + "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notifyUrl + "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "</xml>";
		String code_url = "";

		code_url = GetWxOrderno.getCodeUrl(CREATE_ORDER_URL, xml);
    log.info("---------------------WxNativePay Code Url -> " + code_url);
    
    return code_url;
  }
 
 4.jsp页面部分
  # qrcode.jsp
 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <!Doctype html>
  <html>
  <head>
    <script type="text/javascript" src="../plugins/jquery-1.11.0.min.js"></script>
    <script type="text/javascript">
      var url = '${codeUrl}';
      var orderNo = '${orderNo}';
    </script>
  </head>
  <body>
    <div class="order-item" align="center" id="qrcode">
    </div>
    <script type="text/javascript" src="../wxpay/jquery.qrcode.js"></script><!-- 官网有 -->
    <script type="text/javascript" src="../wxpay/native.js"></script>
  </body>
  </html>
  
  #natvie.js
  $(document).ready(function() {
    $("#qrcode").qrcode({
      render : "canvas",
      text : url,
      width : "256",
      height : "256",
      correctLevel : QRErrorCorrectLevel.H,
      background : "#ffffff",
      foreground : "#000000",
      src : null
    });
    chkIsSuccess();
  });
  // 轮询结果
  function checkOrderSuccess() {
    if (orderNo != '') {
      var seconds = 3600;
      var newText;

      var int = setInterval(function() {
        seconds--;
        newText = '' + seconds + '';
        $.ajax({
          url : WEB_CTX + "/callback/orderQuery",
          data : {
            "orderNo" : decodeURIComponent(orderNo)
          },
          dataType : "json",
          async : false,
          success : function(data) {
            if (data.errorCode == 0) {
              clearInterval(int);
              window.location.href = WEB_CTX + "/callback/success?orderNo=" + orderNo;
            }
          }
        });
        if (seconds == 0) {
          clearInterval(int);
          window.location.href = WEB_CTX + "/callback/fail";
        }
      }, 5000);
    }
  }
 
