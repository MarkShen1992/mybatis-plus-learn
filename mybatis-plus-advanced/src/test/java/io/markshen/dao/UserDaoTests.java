package io.markshen.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.markshen.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class UserDaoTests {

	@Resource
	private UserDAO userDAO;

	@Test
	public void testSelectAll() {
		List<User> users = userDAO.selectList(null);
		users.forEach(System.out::println);
	}

	/**
	 * 手动加 deleted = 0的条件
	 */
	@Test
	public void testMySelectList() {
		List<User> users = userDAO.mySelectList(Wrappers.<User>lambdaQuery().gt(User::getAge, 25).eq(User::getDeleted, 0));
		users.forEach(System.out::println);
	}

	@Test
	public void testLogicDelete() {
		int rows = userDAO.deleteById(1094592041087729666L);
		System.out.println(rows);
	}

	@Test
	public void testUpdate() {
		User u = new User();
		u.setId(1094592041087729666L);
		u.setAge(30);

		int rows = userDAO.updateById(u);
		System.out.println(rows);
	}
}
