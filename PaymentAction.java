@ParentPackage("member")
@Namespace("/payment")
@Results({ 
	@Result(name = "wxNativePay", location = "/WEB-INF/payment/qrcode.jsp")
})
public class PaymentAction extends BaseAction {
  private static final long serialVersionUID = 2252366781790927392L;
	private Logger log = Logger.getLogger(PaymentAction.class);
  
  private String orderNo;
  
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
}
