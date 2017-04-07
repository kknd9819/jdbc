package top.zz.config.shiro;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import top.zz.model.Permission;
import top.zz.model.Role;
import top.zz.model.User;
import top.zz.model.vo.Principal;
import top.zz.service.UserService;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 权限认证
 * @Date 2014-12-31
 * @author 欧志辉
 * @version 1.0
 */
public class AuthenticationRealm extends AuthorizingRealm {

	
	@Resource(name = "userServiceImpl")
	private UserService userService;

	/**
	 * 获取认证信息
	 * 
	 * @param token
	 *            令牌
	 * @return 认证信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
		
		/****************************
		 * 安全认证框架	Shiro1.2
		 * UsernamePasswordToken 支持最常见的 用户名/密码 的认证机制 org.apache.shiro.authc.UsernamePasswordToken
		 * 由于它实现了RememberMeAuthenticationToken接口，我们可以通过令牌设置“记住我”的功能 ，“已记住”和“已认证”是有区别的：
		 * 已记住的用户仅仅是非匿名用户，你可以通过subject.getPrincipals()获取用户信息。但是它并非是完全认证通过的用户，
		 * 当你访问需要认证用户的功能时，你仍然需要重新提交认证信息。 
		 * 
		 * AuthenticationToken : 重写加入 验证码 。
		 * **/
		AuthenticationToken authenticationToken = (AuthenticationToken) token;
		
		String username = authenticationToken.getUsername();
		String password = new String(authenticationToken.getPassword());
		String ip = authenticationToken.getHost();

		
		
		if (username != null && password != null) {
			User user = userService.findByUsername(username);
			if (user == null) {
				throw new UnknownAccountException();
			}
			if (!user.getEnabled()) {
				throw new DisabledAccountException();
			}
			if (user.getLocked()) {
				int loginFailureLockTime = 10;
				if (loginFailureLockTime == 0) {
					throw new LockedAccountException();
				}
				Date lockedDate = user.getLockedDate();
				Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
				if (new Date().after(unlockDate)) {
					user.setLoginFailureCount(0);
					user.setLocked(false);
					user.setLockedDate(null);
					userService.update(user);
				} else {
					throw new LockedAccountException();
				}
			}
			if (!DigestUtils.md5Hex(password).equals(user.getPassword())) {
				int loginFailureCount = user.getLoginFailureCount() + 1;
				if (loginFailureCount >= 5) {
					user.setLocked(true);
					user.setLockedDate(new Date());
				}
				user.setLoginFailureCount(loginFailureCount);
				userService.update(user);
				throw new IncorrectCredentialsException();
			}
			user.setLoginIp(ip);
			user.setLoginDate(new Date());
			user.setLoginFailureCount(0);
			userService.update(user);
			return new SimpleAuthenticationInfo(new Principal(user.getUid(), username, user.getName()), password, getName());
		}
		throw new UnknownAccountException();
	}

	/**
	 * 获取授权信息
	 * @param principals principals
	 * @return AuthorizationInfo 授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		User user = (User) principals.getPrimaryPrincipal();
		for(Role role:user.getRoleList()){
			authorizationInfo.addRole(role.getRole());
			for(Permission permission:role.getPermissions()){
				authorizationInfo.addStringPermission(permission.getPermission());
			}
		}
		return authorizationInfo;
	}

}