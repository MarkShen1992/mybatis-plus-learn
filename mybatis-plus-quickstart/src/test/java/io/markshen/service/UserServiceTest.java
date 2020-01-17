package io.markshen.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.markshen.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testGetOne() {
        User u = userService.getOne(Wrappers.<User>lambdaQuery().gt(User::getAge, 25), Boolean.FALSE);
        System.out.println(u);
    }

    @Test
    public void testBatchInsert() {
        User u1 = new User();
        u1.setName("Mark");
        u1.setAge(25);

        User u2 = new User();
        u2.setName("Alice");
        u2.setAge(23);

        List<User> users = Arrays.asList(u1, u2);
        boolean result = userService.saveBatch(users);
        System.out.println("是否插入成功：" + (result == true ? "插入成功":"没有插入成功"));
    }

    @Test
    public void testBatchInsertOrUpdate() {
        User u1 = new User();
        u1.setName("Mark");
        u1.setAge(25);

        User u2 = new User();
        u2.setId(1218304500591243268L);
        u2.setName("Lily");
        u2.setAge(25);

        List<User> users = Arrays.asList(u1, u2);
        boolean result = userService.saveOrUpdateBatch(users);
        System.out.println("是否插入成功：" + (result == true ? "插入成功":"没有插入成功"));
    }

    @Test
    public void testBatchChainSelect() {
        List<User> users = userService.lambdaQuery().gt(User::getAge, 25).list();
        users.forEach(System.out::println);
    }

    @Test
    public void testBatchChainUpdate() {
        boolean result = userService.lambdaUpdate().eq(User::getAge, 25).set(User::getAge, 26).update();
        System.out.println(result);
    }

    @Test
    public void testBatchChainRemove() {
        boolean result = userService.lambdaUpdate().eq(User::getAge, 26).remove();
        System.out.println(result);
    }
}