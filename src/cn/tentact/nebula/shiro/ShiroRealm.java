package cn.tentact.nebula.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 * Shiro认证与授权类
 * 
 * @author 杨迪
 *
 */
public class ShiroRealm extends AuthorizingRealm {
	/**
	 * 授权函数
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection collection) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		String role = (String) session.getAttribute("role"); // 获取用户的角色
		// 创建授权对象
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addRole(role);
		return info; // 把授权对象返回给Shiro，Shiro会对比用户的授权信息与要求的是否匹配
	}

	/**
	 * 认证函数
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		// 用户提交的用户名和密码
		String username = token.getUsername();
		String password = new String(token.getPassword());
		// 创建认证对象
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, password, getName());
		return info; // 把认证对象返回给Shiro， Shiro会把认证对象缓存到Session中，然后ID保存到Cookie中
	}

}
