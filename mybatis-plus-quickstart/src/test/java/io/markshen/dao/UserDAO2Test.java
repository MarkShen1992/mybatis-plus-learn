package io.markshen.dao;

import io.markshen.entity.User2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class UserDAO2Test {

    @Autowired
    private UserDAO2 userDAO;

    @Test
    public void testInsert() {
        User2 u = new User2();
        u.setRealName("向北");
        u.setAge(16);
        u.setManagerId(1088248166370832385L);
        u.setCreateTime(LocalDateTime.now());
        u.setRemark("sadfjsldfj");
        int result = userDAO.insert(u);
        System.out.println(result);
    }
}
