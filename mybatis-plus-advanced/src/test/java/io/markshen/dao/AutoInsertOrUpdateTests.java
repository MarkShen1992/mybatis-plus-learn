package io.markshen.dao;

import io.markshen.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class AutoInsertOrUpdateTests {

	@Resource
	private UserDAO userDAO;

	@Test
	public void testInsert() {
		User u = new User();
		u.setName("Puma");
		u.setAge(31);
		u.setEmail("puma@163.com");
		u.setManagerId(1088248166370832385L);

		int rows = userDAO.insert(u);
		System.out.println(rows);
	}

	@Test
	public void testUpdate() {
		User u = new User();
		u.setAge(26);
		u.setId(1088248166370832385L);

		int rows = userDAO.updateById(u);
		System.out.println(rows);
	}
}
