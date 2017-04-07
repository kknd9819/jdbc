<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>云生源登录后台</title>
<meta http-equiv="expires" content="0" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta name="author" content="生源科技" />
<meta name="copyright" content="生源科技" />
 
<link rel="stylesheet" href="/css/login.css">
<link rel="stylesheet" href="/common/jqueryeasyui/themes/default/easyui.css">

<script type="text/javascript" src="/common/jquery/jquery.js"></script>
<script type="text/javascript" src="/common/jqueryeasyui/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/common/jqueryeasyui/js/easyui-lang-zh_CN.js"></script>
 
	
<script type="text/javascript" src="/common/login/jsbn.js"></script>
<script type="text/javascript" src="/common/login/prng4.js"></script>
<script type="text/javascript" src="/common/login/rng.js"></script>
<script type="text/javascript" src="/common/login/rsa.js"></script>
<script type="text/javascript" src="/common/login/base64.js"></script>
<script type="text/javascript" src="/js/jquery.cookie.js"></script>
 
<script type="text/javascript">
	$().ready( function() {
		var $loginForm = $("#loginForm");
		var $enPassword = $("#enPassword");
		var $username = $("#username");
		var $password = $("#password");
		var $captcha = $("#captcha");
		var $captchaImage = $("#captchaImage");
		var $isRememberUsername = $("#isRememberUsername");
		
		// 记住用户名
		var $adminUsername = $.cookie('adminUsername');
		if( $adminUsername != null && $adminUsername != "null" ) {
			$isRememberUsername.prop("checked", true);
			$username.val($adminUsername);
			$password.focus();
		} else {
			$isRememberUsername.prop("checked", false);
			$username.focus();
		}
		
		// 更换验证码
		$captchaImage.click( function() {
			$captchaImage.attr("src", "/admin/common/captchaImage?timestamp=" + (new Date()).valueOf());
		});
		
		// 表单验证、记住用户名
		$('#loginButton').bind('click',function(){
			if ($username.val() == "") {
				$.messager.alert('登录','请输入您的用户名！','warning');
				return false;
			}
			if ($password.val() == "") {
				$.messager.alert('登录','请输入您的密码！','warning');
				return false;
			}
			if ($captcha.val() == "") {
				$.messager.alert('登录','请输入您的验证码！','warning');
				return false;
			}
			
			if ($isRememberUsername.prop("checked")) {
				$.cookie('adminUsername', $username.val(), {expires: 7 * 24 * 60 * 60});
			} else {
				$.cookie('adminUsername',null);
			}
			
			var rsaKey = new RSAKey();
			var modulus = $("#modulus").val();
			var exponent = $("#exponent").val();
			rsaKey.setPublic(b64tohex(modulus), b64tohex(exponent));
			var enpassword = hex2b64(rsaKey.encrypt($password.val()));
			$enPassword.val(enpassword);
			$loginForm.submit();
		});
		 
		$("body").keydown(function(event) {
		    if (event.keyCode == "13") {
		    	$('#loginButton').click();
		    }
		});    
		$(window).keydown(function (e) {
			if (e.which == 13) {
				return false;
			}
		}); 		
		
		//得到焦点
		$("#password").focus(function(){
			$("#left_hand").animate({
				left: "150",
				top: " -65"
			},{step: function(){
				if(parseInt($("#left_hand").css("left"))>140){
					$("#left_hand").attr("class","left_hand");
				}
			}}, 2000);
			$("#right_hand").animate({
				right: "-63",
				top: "-65px"
			},{step: function(){
				if(parseInt($("#right_hand").css("right"))> -70){
					$("#right_hand").attr("class","right_hand");
				}
			}}, 2000);
		});
		//失去焦点
		$("#password").blur(function(){
			$("#left_hand").attr("class","initial_left_hand");
			$("#left_hand").attr("style","left:112px;top:-12px;");
			$("#right_hand").attr("class","initial_right_hand");
			$("#right_hand").attr("style","right:-92px;top:-12px");
		});
		
		 var message = $("#message").val();
		if (message != null && message != '') {
			$.messager.alert('登录', message,'error');
		}
	});
</script>
</head>
<body>
	 
	  
		<div class="top_div"></div>
		<form id="loginForm" action="login.jsp" method="post">
			<div style="background: rgb(255, 255, 255); margin: -100px auto auto; border: 1px solid rgb(231, 231, 231); border-image: none; width: 400px; height: 250px; text-align: center;">
			<div style="width: 165px; height: 96px; position: absolute;">
			<div class="tou"></div>
			<div class="initial_left_hand" id="left_hand"></div>
			<div class="initial_right_hand" id="right_hand"></div></div>
			<p style="margin:10px 0 -20px 0;font-size:18px;font-weight:600;">云生源后台管理系统</p>
			<p style="padding: 30px 0px 10px; position: relative;">
				<span  class="u_logo"></span>         
				<input id="username" name="username" class="ipt" type="text" placeholder="请输入用户名" value="" maxlength="20">
				<input type="hidden" id="modulus" value="${modulus}" />
                <input type="hidden" id="exponent" value="${exponent}" />
                <input type="hidden" id="message" value="${message}" />
			</p>
			<p style="position: relative;"><span class="p_logo"></span>         
				<input type="password" id="password" class="ipt" type="password" placeholder="请输入密码" value="" maxlength="20">   
				<input type="hidden" id="enPassword" name="enPassword" />
  			</p>
  			<p style="padding:10px  0px 0px 0px; position: relative;"> 
  				<img id="captchaImage" class="captchaImage" src="/admin/common/captchaImage" width="120" height="38" valign="bottom" style="float:right; margin-right:31px;">
  				<span class="p_yanzheng"></span>
  				<input id="captcha" name="captcha" class="ipt1" type="text" placeholder="请输入验证码" value="" maxlength="4">
   		  </p>
		 <div style="height: 50ppx; height:px;x; line-height: 50px; margin-top: 20px; border-top-color: rgb(231, 231, 231); border-top-width: 1px; border-top-style: solid;">
			<p style="margin: 0px 35px 20px 45px;"><span style="float: left;"><input id="isRememberUsername" name="rememberMe" type="checkbox"  value="true" /> 记住我</span>
           <span style="float: right;"> 
              <a id="loginButton" style="background: rgb(0, 142, 173); padding: 7px 20px; border-radius: 4px; border: 1px solid rgb(26, 117, 152); border-image: none; color: rgb(255, 255, 255); font-weight: bold;"  href="javascript:;">登录</a> 
           </span>        
            </p>
           </div>
         </div>
       </form>
     
</body>
</html>