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
