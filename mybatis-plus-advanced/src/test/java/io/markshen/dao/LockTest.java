package io.markshen.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.markshen.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 悲观锁：写多读少
 * 乐观锁：读多写少
 */
@SpringBootTest
public class LockTest {

	@Resource
	private UserDAO userDAO;

	@Test
	public void testUpdate02() {
		User u = new User();
		u.setEmail("pu@163.com");
		u.setVersion(2);
		u.setManagerId(1088248166370832385L);

		QueryWrapper<User> query = Wrappers.<User>query();
		query.eq("name", "puma");
		int rows = userDAO.update(u, query);
		System.out.println(rows);

		User u2 = new User();
		u2.setEmail("p@163.com");
		u2.setVersion(3);
		u2.setAge(26);

		int rows2 = userDAO.update(u2, query);
		System.out.println(rows2);
	}

	@Test
	public void testUpdate() {
		User u = new User();
		u.setEmail("pm@163.com");
		u.setId(1219752185055727617L);
		u.setVersion(1);

		int rows = userDAO.updateById(u);
		System.out.println(rows);
	}
}
