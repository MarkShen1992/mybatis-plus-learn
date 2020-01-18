package io.markshen.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("ad_user")
public class User {
    // 主键
    private Long id;

    // 姓名
    private String name;

    // 年龄
    private Integer age;

    // 邮箱
    private String email;

    // 直属上级
    private Long managerId;

    // 创建时间
    private LocalDateTime createTime;

    // 修改时间
    private LocalDateTime updateTime;

    // 版本
    private Integer version;

    // 逻辑删除标识(0: not deleted, 1: deleted)
    @TableLogic
    private Integer deleted;
}
