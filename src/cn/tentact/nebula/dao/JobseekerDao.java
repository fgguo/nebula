package cn.tentact.nebula.dao;

import com.jfinal.plugin.activerecord.Db;

public class JobseekerDao {
public Boolean login(String username,String password) {
	String sql=Db.getSql("login");
	long count=Db.queryLong(sql,username,password);
	boolean bool=count==1?true:false;
	return bool;
 }
public boolean register(String username,String password) {
	String sql1=Db.getSql("check");
	long count1=Db.queryLong(sql1,username);
	if(count1==1)
		return false;
	System.out.println("注册");
	String sql2=Db.getSql("register");
	Db.queryLong(sql2,username,password);
	return true;
	}
public boolean update(String sex,String birthday,int height,int weight,String marriage, String ancestral_home,String tel,String email,String education) {
	String sql=Db.getSql("updat");
	Db.queryLong(sql,sex,birthday,height,weight,marriage,ancestral_home,tel,email,education);
	return true;
	}
}
