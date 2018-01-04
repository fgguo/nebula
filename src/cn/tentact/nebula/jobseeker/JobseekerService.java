package cn.tentact.nebula.jobseeker;

import cn.tentact.nebula.dao.JobseekerDao;

public class JobseekerService {
	private JobseekerDao dao=new JobseekerDao();
	public boolean login(String username,String password) {
		boolean bool=dao.login(username,password);
		return bool;
	}
	public boolean register(String username,String password) {
		boolean bool=dao.register(username,password);
		return bool;
	}
	public boolean update(String sex,String birthday,int height,int weight,String marriage, String ancestral_home,String tel,String email,String education) {
		boolean bool=dao.update(sex, birthday, height, weight, marriage, ancestral_home, tel, email, education);
		return bool;
	}
}
