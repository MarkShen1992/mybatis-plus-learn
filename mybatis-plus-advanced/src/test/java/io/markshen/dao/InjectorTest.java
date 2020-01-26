package io.markshen.dao;

import io.markshen.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
@SpringBootTest
public class InjectorTest {

	@Resource
	private UserDAO userDAO;

	@Test
	public void testDeleteAll() {
		int rows = userDAO.deleteAll();
		System.out.println("删除行数：" + rows);
	}

	@Test
	public void testInsertBatch() {
		User u1 = new User();
		u1.setName("李兴华");
		u1.setAge(34);
		u1.setManagerId(1088248166370832385L);

		User u2 = new User();
		u2.setName("李兴华");
		u2.setAge(34);
		u2.setManagerId(1088248166370832385L);

		List<User> users = Arrays.asList(u1, u2);
		int rows = userDAO.insertBatchSomeColumn(users);
		System.out.println("影响行数: " + rows);
	}

	@Test
	public void testDeleteByIdWithFill() {
		User u = new User();
		u.setId(1221311782769991682L);
		u.setName("李兴华");
		int rows = userDAO.deleteByIdWithFill(u);
		System.out.println("删除行数：" + rows);
	}
}
