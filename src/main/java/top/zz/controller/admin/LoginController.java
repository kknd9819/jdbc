package top.zz.controller.admin;

import cn.shengyuan.tools.util.AES.AES64;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import top.zz.service.RSAService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by X-man on 2017/3/31.
 */
@Controller
public class LoginController {

    @Resource
    private RSAService rsaService;

    @RequestMapping("/")
    public String login(HttpServletRequest request, Model model){

        RSAPublicKey publicKey = rsaService.generateKey(request);
        String modulus = AES64.encodeBase64String(publicKey.getModulus().toByteArray());
        String exponent = AES64.encodeBase64String(publicKey.getPublicExponent().toByteArray());
        model.addAttribute("modulus",modulus);
        model.addAttribute("exponent",exponent);
        String message = null;
        String loginFailure = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if (loginFailure != null) {
            if (loginFailure.equals("org.apache.shiro.authc.pam.UnsupportedTokenException")) {
                message = "验证码输入错误";
            } else if (loginFailure.equals("org.apache.shiro.authc.UnknownAccountException")) {
                message = "此账号不存在";
            } else if (loginFailure.equals("org.apache.shiro.authc.DisabledAccountException")) {
                message = "此账号已被禁用";
            } else if (loginFailure.equals("org.apache.shiro.authc.LockedAccountException")) {
                message = "此账号已被锁定";
            } else if (loginFailure.equals("org.apache.shiro.authc.IncorrectCredentialsException")) {
                message = "密码错误，若连续5次密码错误账号将被锁定";
            } else if (loginFailure.equals("org.apache.shiro.authc.AuthenticationException")) {
                message = "账号认证失败";
            }
            request.setAttribute("message", message);
            model.addAttribute("message",message);
        } else {
            model.addAttribute("message","");
        }
        return "/login";
    }
}
