package cn.tentact.nebula.jobseeker;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.crypto.SealedObject;
import javax.security.auth.Subject;
import javax.swing.text.ChangedCharSetException;

import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateUserStatement.UserSpecification;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.sun.xml.internal.ws.developer.UsesJAXBContext;

public class JobseekerController extends Controller{
	//登录
	private JobseekerService jobseekerService=new JobseekerService();
	public void login() {
		//获得ajax数据
		String username=getPara("username");
		String password=getPara("password");
		//执行
;		boolean bool=jobseekerService.login(username, password);
		//返回结果
		if(bool) {
			org.apache.shiro.subject.Subject subject=SecurityUtils.getSubject();
			UsernamePasswordToken token=new UsernamePasswordToken("username","password");
			subject.login(token);
			Session session=subject.getSession();
			session.setAttribute("role", "jobseeker");
			session.setAttribute("username",username);
			session.setAttribute("password", password);
		}
		renderJson("result",bool);
	}
	public void register() {
		//获得ajax数据
		String username=getPara("username");
		String password=getPara("password");
		//System.out.println(username);
		//执行
		boolean bool=jobseekerService.register(username, password);
		//返回结果
		if(bool) {
			org.apache.shiro.subject.Subject subject=SecurityUtils.getSubject();
			UsernamePasswordToken token=new UsernamePasswordToken("username","password");
			subject.login(token);
			Session session=subject.getSession();
			session.setAttribute("role", "jobseeker");
		}
		renderJson("result",bool);
	}
	public void recruit() {
		//获得ajax数据
		
		//执行
		//返回结果
		//int start=Integer.parseInt(getPara("start"));
		//int length=Integer.parseInt(getPara("length"));
		List<Record> users = Db.find("select * from recruit");
		List<Record> result = new ArrayList<Record>();
		//Iterator<Record> iterator=users.iterator();
		for(int i=users.size();i>(users.size()-7);i--) {
			Record user =users.get(i-1);
			result.add(user);
			System.out.println(i+user.toString());
		}
		renderJson("result",result);
	}
	public void searchInfo() {
		
		//返回结果
			org.apache.shiro.subject.Subject subject=SecurityUtils.getSubject();
			UsernamePasswordToken token=new UsernamePasswordToken("username","password");
			subject.login(token);
			Session session=subject.getSession();
			session.setAttribute("role", "jobseeker");
			String username=(String) session.getAttribute("username");
			String password=(String) session.getAttribute("password");
			String sql=Db.getSql("search");
			List<Record> users = Db.find(sql,username,password);
			//Iterator<Record> iterator=users.iterator();
			int i=users.size();
			Record user =users.get(i-1);
		    renderJson("result",user);
	}
	public void detail() {
		//获得ajax数据
		int index=Integer.parseInt(getPara("index"));
		List<Record> users = Db.find("select * from recruit");
		List<Record> result = new ArrayList<Record>();
		Record user =users.get(6-index);
		result.add(user);
		renderJson("result",result);
	}
	public void update() {
		//获得ajax数据
		
		//执行
		//返回结果
		String sex=getPara("sex");
		String birthday=getPara("birthday");
		char array [] =getPara("height").toCharArray();
		String hString=new String(array, 0, 3);
		int height=Integer.parseInt(hString);
		//System.out.println(height);
		char array2 [] =getPara("weight").toCharArray();
		String hString2=new String(array2, 0, 2);
		int weight=Integer.parseInt(hString2);
		String marriage=getPara("marriage");
		String ancestral_home=getPara("ancestral_home");
		String tel=getPara("tel");
		String email=getPara("email");
		String education=getPara("education");
		boolean bool=jobseekerService.update(sex, birthday, height, weight, marriage, ancestral_home, tel, email, education);
		renderJson("result",bool);
	}
	public void resume() {
		//获得ajax数据
		
		//执行
		//返回结果
		//int length=Integer.parseInt(getPara("length"));
		List<Record> users = Db.find("SELECT * from resume where name='赵萌萌';");
		List<Record> result = new ArrayList<Record>();
		//Iterator<Record> iterator=users.iterator();
		int i=users.size();
		Record user =users.get(i-1);
	    renderJson("result",user);
	}
}
