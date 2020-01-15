package io.markshen.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.markshen.entity.User;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
        u.setName("刘红雨"); // 配合注解 @TableField(condition=SqlCondition.LIKE), 当SqlCondition中的变量不够用的时候, 自己造
        u.setAge(32);

        QueryWrapper<User> wrapper = new QueryWrapper<>(u);
        // 此时要在SQL中添加 wrapper 参数, 会生成SQL语句 => WHERE name=? AND age=? AND (name = ? AND age = ?)
        // wrapper.eq("name", "刘红雨").eq("age", 32);
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }

    // =============== 条件构造器allEq用法 =================
    @Test
    public void testSelectByWrapperAllEq01() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "王天风");
        params.put("age", 25);

        wrapper.allEq(params);
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void testSelectByWrapperAllEq02() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "王天风");
        params.put("age", null);

        wrapper.allEq(params);
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void testSelectByWrapperAllEq03() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "王天风");
        params.put("age", null);

        boolean isIgnoreNullVal = false;
        wrapper.allEq(params, isIgnoreNullVal);
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void testSelectByWrapperAllEq04() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "王天风");
        params.put("age", null);

        // 过滤条件中的name
        wrapper.allEq((k, v) -> !k.equals("name"), params);
        List<User> users = userDAO.selectList(wrapper);
        users.forEach(System.out::println);
    }

    // =============== 其他使用条件构造器的方法 =================

    /**
     * 只查询表中的几列就可以了
     * selectMaps
     */
    @Test
    public void testSelectByWrapperMaps01() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("name", "雨").lt("age", 40);
        List<Map<String, Object>> users = userDAO.selectMaps(wrapper);
        users.forEach(System.out::println);
    }

    /**
     * 只查询表中的几列就可以了
     * selectMaps
     */
    @Test
    public void testSelectByWrapperMaps02() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("name", "雨").lt("age", 40).select("name", "age");
        List<Map<String, Object>> users = userDAO.selectMaps(wrapper);
        users.forEach(System.out::println);
    }

    /**
     * 按照直属上级分组，查询每组的平均年龄、最大年龄、最小年龄。
     * 并且只取年龄总和小于500的组。
     * select avg(age) avg_age,min(age) min_age,max(age) max_age
     * from user
     * group by manager_id
     * having sum(age) <500
     */
    @Test
    public void testSelectByWrapperMaps03() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // SELECT avg(age) AS avg_age,min(age) AS min_age,max(age) AS max_age FROM user GROUP BY manager_id HAVING sum(age) < ?
        wrapper.select("avg(age) AS avg_age", "min(age) AS min_age", "max(age) AS max_age")
                .groupBy("manager_id").having("sum(age) < {0}", 500);
        List<Map<String, Object>> users = userDAO.selectMaps(wrapper);
        users.forEach(System.out::println);
    }

    /**
     * 只有一列
     * selectObjs
     */
    @Test
    public void testSelectByWrapperObjs() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("name", "雨").lt("age", 40).select("id", "name");
        List<Object> users = userDAO.selectObjs(wrapper);
        users.forEach(System.out::println);
    }

    /**
     * 总记录数
     */
    @Test
    public void testSelectByWrapperCount() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("name", "雨").lt("age", 40);
        Integer count = userDAO.selectCount(wrapper);
        System.out.println(count);
    }



    /**
     * 只输出一条反例：会出现异常
     */
    @Test
    public void testSelectByWrapperSelectOne02() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("name", "雨").lt("age", 40);
        Exception exception = assertThrows(
                MyBatisSystemException.class,
                () -> userDAO.selectOne(wrapper));
    }

    // ==================== Lambda表达式 ========================

    /**
     * 防止数据库列名误写
     */
    @Test
    public void testSelectLambda01() {
        LambdaQueryWrapper<User> lambda = new QueryWrapper<User>().lambda();
        // LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // LambdaQueryWrapper<User> UserLambdaQueryWrapper = Wrappers.<User>lambdaQuery();

        // WHERE (name LIKE ? AND age < ?)
        lambda.like(User::getName, "雨").lt(User::getAge, 40);
        List<User> users = userDAO.selectList(lambda);
        users.forEach(System.out::println);
    }

    /**
     * 防止数据库列名误写
     * WHERE (name LIKE ? AND ( (age < ? OR email IS NOT NULL) ))
     */
    @Test
    public void testSelectLambda02() {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.like(User::getName, "王")
                .and(lqw -> lqw.lt(User::getAge, 40).or().isNotNull(User::getEmail));
        List<User> users = userDAO.selectList(userLambdaQueryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * WHERE (name LIKE ? AND age >= ?)
     */
    @Test
    public void testSelectLambda03() {
        List<User> users = new LambdaQueryChainWrapper<User>(userDAO).
                like(User::getName, "雨").ge(User::getAge, 20).list();
        users.forEach(System.out::println);
    }

    // =============== 自定义SQL =====================

    /**
     * 自定义SQL
     */
    @Test
    public void testSelectAll() {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.like(User::getName, "王")
                .and(lqw -> lqw.lt(User::getAge, 40).or().isNotNull(User::getEmail));
        List<User> users = userDAO.selectAll(userLambdaQueryWrapper);
        users.forEach(System.out::println);
    }

    // =============== 物理分页查询 =======================

    @Test
    public void testSelectPage01() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.ge("age", 26);
        Page page = new Page(1, 2);

        IPage<User> p = userDAO.selectPage(page, wrapper);
        System.out.println("total:" + p.getTotal());
        System.out.println("size:" + p.getSize());
        p.getRecords().forEach(System.out::println);
    }

    @Test
    public void testSelectPage02() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.ge("age", 26);
        Page page = new Page(1, 2);

        IPage<Map<String, Object>> p = userDAO.selectMapsPage(page, wrapper);
        System.out.println("total:" + p.getTotal());
        System.out.println("size:" + p.getSize());
        p.getRecords().forEach(System.out::println);
    }

    @Test
    public void testSelectPage03() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.ge("age", 26);
        // false 不查询总记录数
        Page page = new Page(1, 2, false);

        IPage<User> p = userDAO.selectPage(page, wrapper);
        System.out.println("total:" + p.getTotal());
        System.out.println("size:" + p.getSize());
        p.getRecords().forEach(System.out::println);
    }

    @Test
    public void testSelectPage04() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.ge("age", 26);
        // false 不查询总记录数
        Page page = new Page(1, 2);

        IPage<User> p = userDAO.selectUserPage(page, wrapper);
        System.out.println("total:" + p.getTotal());
        System.out.println("size:" + p.getSize());
        p.getRecords().forEach(System.out::println);
    }

    // ================================= update =======================================
    @Test
    public void testUpdateById() {
        User u = new User();
        u.setId(1088248166370832385L);
        u.setAge(26);
        u.setEmail("wangtianfeng2@baomidou.com");

        int result = userDAO.updateById(u);
        System.out.println("更新了：" + result + "条数据.");
    }

    @Test
    public void testUpdateByWrapper() {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("name", "李艺伟").eq("age", 28);

        User u = new User();
        u.setAge(29);
        u.setEmail("liyiwei2020@baomidou.com");

        int result = userDAO.update(u, updateWrapper);
        System.out.println("更新了：" + result + "条数据.");
    }
}