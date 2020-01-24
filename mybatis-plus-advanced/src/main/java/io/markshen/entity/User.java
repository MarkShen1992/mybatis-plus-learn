package io.markshen.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@TableName("ad_user")
@Accessors(chain = true)
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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 修改时间
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    // 版本
    @Version
    private Integer version;

    // 逻辑删除标识(0: not deleted, 1: deleted)
    @TableLogic
    @TableField(select = false)  // 去掉查询结果中的不显示 deleted 字段
    private Integer deleted;
}
