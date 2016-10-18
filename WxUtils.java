public class WxUtils {
  private static Logger log = Logger.getLogger(WxUtils.class);

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
  
}
