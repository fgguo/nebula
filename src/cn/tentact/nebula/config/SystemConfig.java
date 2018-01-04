package cn.tentact.nebula.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.plugin.shiro.ShiroInterceptor;
import com.jfinal.ext.plugin.shiro.ShiroPlugin;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

import cn.tentact.nebula.jobseeker.JobseekerController;



/**
 * 系统配置封装类
 * 
 * @author 杨迪
 *
 */
public class SystemConfig extends JFinalConfig {
	private Routes routes;

	@Override
	public void configConstant(Constants constants) {
		constants.setDevMode(true); // 开启调试模式，控制台打印HTTP请求

	}

	@Override
	public void configEngine(Engine engine) {
		// OR映射

	}

	@Override
	public void configHandler(Handlers handlers) {
		// 自定义HTTP处理回调
	}

	@Override
	public void configInterceptor(Interceptors interceptors) {
		// HTTP拦截器
		interceptors.add(new ShiroInterceptor()); // Shiro拦截器

	}

	@Override
	public void configPlugin(Plugins plugins) {
		/*
		 * MySQL数据库连接
		 */
		String url = "jdbc:mysql://127.0.0.1:8066/TESTDB";
		String username = "root";
		String password = "fuguo666";
		/*
		 * MySQL数据库连接池
		 */
		DruidPlugin druidPlugin = new DruidPlugin(url, username, password); // 使用阿里巴巴Druid连接池
		druidPlugin.setDriverClass(JdbcConstants.MARIADB_DRIVER);// 数据库驱动类
		druidPlugin.addFilter(new StatFilter()); // 连接池开启状态监控
		druidPlugin.setInitialSize(10); // 初始连接数
		druidPlugin.setMaxActive(20);// 最大活动连接数
		druidPlugin.setMinIdle(10);// 最小空闲连接数
		druidPlugin.setValidationQuery("SELECT 1"); // 用于执行检测的SQL语句
		druidPlugin.setTestOnReturn(false);// 归还给连接池的时候不检测连接
		druidPlugin.setTestOnBorrow(true); // 使用连接之前检测连接
		druidPlugin.setMaxPoolPreparedStatementPerConnectionSize(50); // 每个连接可以缓存多少个PreparedStatement
		/*
		 * 数据库防火墙（拦截恶意SQL）
		 */
		WallFilter wall = new WallFilter();
		wall.setDbType(JdbcConstants.MARIADB);
		WallConfig config = new WallConfig();
		config.setStrictSyntaxCheck(false); // 关闭druid的SQL解析功能，这个功能会导致全局主键的失效
		wall.setConfig(config);
		druidPlugin.addFilter(wall);

		plugins.add(druidPlugin); // 向JFinal注册数据库连接池插件

		/*
		 * 持久层插件（替代JDBC）
		 */
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		arp.setBaseSqlTemplatePath(PathKit.getRootClassPath() + "/cn/tentact/nebula/dao"); // SQL文件目录
		// 注册SQL文件
		arp.addSqlTemplate("jobseeker.sql");

		plugins.add(arp); // 向JFinal注册持久层插件

		/*
		 * Shiro插件
		 */
		ShiroPlugin shiroPlugin = new ShiroPlugin(this.routes); // 路由注入到Shiro，由Shiro判断请求是否合法
		plugins.add(shiroPlugin);// 向JFinal注册Shiro插件

	}

	@Override
	public void configRoute(Routes routes) {
		this.routes = routes;// 把路由保存到实例变量，以供Shiro使用
		/*
		 * 定义路由地址
		 */
		routes.add("/jobseeker",JobseekerController.class);
	}

	@Override
	public void afterJFinalStart() {
		/*
		 * RememberMe功能设置：将Cookie保存到浏览器，即便浏览器关闭之后再次启动也会免于重新登录
		 */
		SimpleCookie rememberMeCookie = new SimpleCookie("rememberMe");
		rememberMeCookie.setHttpOnly(true);
		rememberMeCookie.setMaxAge(2592000); // 30天

		CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
		rememberMeManager.setCipherKey("nebula".getBytes());
		rememberMeManager.setCookie(rememberMeCookie);

		DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
		defaultWebSecurityManager.setRememberMeManager(rememberMeManager);
		SecurityUtils.setSecurityManager(defaultWebSecurityManager);

	}

}
