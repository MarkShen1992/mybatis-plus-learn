package io.markshen.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_user")
public class User2 {

    // 指定主键
    @TableId
    private Long userId;

    @TableField("name")
    private String realName;
    private Integer age;
    private String email;

    /**
     * 直属上级
     */
    private Long managerId;
    // since JDK1.8
    private LocalDateTime createTime;

    /**
     * 备注
     */
    // 第一种方式
    // private transient String remark;

    // 第二种方式
//    private static String remark;
//
//    public static String getRemark() {
//        return remark;
//    }
//
//    public static void setRemark(String remark) {
//        User2.remark = remark;
//    }

    @TableField(exist = false)
    private String remark;
}