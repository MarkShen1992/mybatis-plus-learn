package io.markshen.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.markshen.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

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
}
