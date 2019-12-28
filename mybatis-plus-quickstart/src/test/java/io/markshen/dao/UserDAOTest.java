package io.markshen.dao;

import io.markshen.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class UserDAOTest {

    @Autowired
    private UserDAO userDAO;

    @Test
    public void testSelect() {
        List<User> users = userDAO.selectList(null);
        Assert.notNull(users, "object is not null.");
        users.forEach(System.out::println);
    }

    @Test
    public void testInsert() {
        User u = new User();
        u.setName("杜甫");
        u.setAge(16);
        u.setManagerId(1088248166370832385L);
        u.setCreateTime(LocalDateTime.now());
        int result = userDAO.insert(u);
        System.out.println(result);
    }

    @Test
    public void testSelectById() {
        User u = userDAO.selectById(1087982257332887553L);
        Assert.notNull(u, "object is not null.");
        System.out.println(u);
    }
}
