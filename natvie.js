/**
 * 扫码支付
 */
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
