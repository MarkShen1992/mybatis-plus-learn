package io.markshen.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.markshen.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;

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

    @Test
    public void testSelectByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1087982257332887553L);
        ids.add(1088248166370832385L);
        List<User> users = userDAO.selectBatchIds(ids);
        users.forEach(System.out::println);
    }

    @Test
    public void testSelectByMap() {
        Map<String, Object> condition = new HashMap<>();
        // 查询条件组合 WHERE name = ? AND age = ?, 其中key是 数据库中 的列
        condition.put("name", "王天风");
        condition.put("age", 25);
        List<User> users = userDAO.selectByMap(condition);
        users.forEach(System.out::println);
    }

    // =============== 查询需求 =================

    /**
     * 名字中包含雨并且年龄小于40
     * 	   WHERE (name LIKE ? AND age < ?)
     */
    @Test
    public void testSelectByWrapper01() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // QueryWrapper<User> query = Wrappers.query();
        wrapper.like("name", "雨").lt("age", 40);
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }

    /**
     * 名字中包含雨年并且龄大于等于20且小于等于40并且email不为空
     *     WHERE (name LIKE ? AND age BETWEEN ? AND ? AND email IS NOT NULL)
     */
    @Test
    public void testSelectByWrapper02() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("name", "雨").between("age", 20, 40).isNotNull("email");
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }

    /**
     * 名字为王姓或者年龄大于等于25，按照年龄降序排列，年龄相同按照id升序排列
     *     name like '王%' or age>=25 order by age desc,id asc
     */
    @Test
    public void testSelectByWrapper03() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.likeRight("name", "王").or().ge("age", 25).orderByDesc("age").orderByAsc("id");
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }

    /**
     * Warn: 有SQL注入的风险
     * 创建日期为2019年2月14日并且直属上级为名字为王姓
     *     date_format(create_time,'%Y-%m-%d')='2019-02-14' and manager_id in (select id from user where name like '王%')
     */
    @Test
    public void testSelectByWrapper04() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        wrapper.apply("date_format(create_time,'%Y-%m-%d') = {0}", "2019-02-14")
//            .inSql("manager_id", "select id from user where name like '王%'");
        wrapper.apply("date_format(create_time,'%Y-%m-%d') = 2019-02-14 or true or true")
                .inSql("manager_id", "select id from user where name like '王%'");
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }

    /**
     * 名字为王姓并且（年龄小于40或邮箱不为空）
     *     name like '王%' and (age<40 or email is not null)
     */
    @Test
    public void testSelectByWrapper05() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.likeRight("name", "王")
                .and(condition -> condition.lt("age", 40).or().isNotNull("email"));
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }

    /**
     * 名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空）
     *     name like '王%' or (age<40 and age>20 and email is not null)
     */
    @Test
    public void testSelectByWrapper06() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.likeRight("name", "王")
                .or(condition -> condition.lt("age", 40).gt("age", 20).isNotNull("email"));
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }

    /**
     *  优先级： and > or
     * （年龄小于40或邮箱不为空）并且名字为王姓
     *     (age<40 or email is not null) and name like '王%'
     */
    @Test
    public void testSelectByWrapper07() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.nested(condition -> condition.lt("age", 40).or().isNotNull("email"))
                .likeRight("name", "王");
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }

    /**
     *  年龄为30、31、34、35
     *     age in (30、31、34、35)
     */
    @Test
    public void testSelectByWrapper08() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.in("age", Arrays.asList(30, 31, 34, 35));
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }

    /**
     *  Warn：慎用，有SQL注入风险
     *  只返回满足条件的其中一条语句即可
     *      limit 1
     */
    @Test
    public void testSelectByWrapper09() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.in("age", Arrays.asList(30, 31, 34, 35)).last("limit 1");
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }

    // =============== select中字段不全部出现的查询 =================

    /**
     * 名字中包含雨并且年龄小于40
     * 	   WHERE (name LIKE ? AND age < ?)
     */
    @Test
    public void testSelectByWrapper10() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("id", "name").like("name", "雨").lt("age", 40);
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }

    /**
     * 名字中包含雨并且年龄小于40
     * 	   WHERE (name LIKE ? AND age < ?)
     */
    @Test
    public void testSelectByWrapper11() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("name", "雨").lt("age", 40)
                .select(User.class, info -> !info.getColumn().equals("create_time")
                && !info.getColumn().equals("manager_id"));
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }

    // =============== 条件构造器中condition的作用 =================
    // Controller 模拟
    private void condition(String name, String email) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // 第一种方式
//        if (StringUtils.isNotBlank(name)) {
//            wrapper.like("name", name);
//        }
//        if (StringUtils.isNotBlank(email)) {
//            wrapper.like("email", email);
//        }

        // 第二中方式, 使用链式编程, 通过condition来控制条件加不加入到SQL中
        wrapper.like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(email), "email", email);
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void testCondition() {
        String name = "王";
        String email = ""; // 不会加入SQL中
        condition(name, email);
    }

    // =============== 实体作为条件构造器构造方法参数 =================
    @Test
    public void testSelectByWrapperEntity() {
        // 查询条件 对应SQL语句 => WHERE name=? AND age=?
        User u = new User();
        u.setName("刘红雨"); // 配合注解 @TableField(condition=SqlCondition.LIKE)
        u.setAge(32);

        QueryWrapper<User> wrapper = new QueryWrapper<>(u);
        // 此时要在SQL中添加 wrapper 参数, 会生成SQL语句 => WHERE name=? AND age=? AND (name = ? AND age = ?)
        wrapper.eq("name", "刘红雨").eq("age", 32);
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }
}