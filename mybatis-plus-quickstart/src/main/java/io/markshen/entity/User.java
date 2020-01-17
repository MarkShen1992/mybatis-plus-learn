package io.markshen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class User extends Model<User> {

    // @TableId(type= IdType.AUTO)  // 局部主键Id
    // @TableId(type= IdType.NONE)  // 用户指定
    // @TableId(type= IdType.ID_WORKER)  // 默认雪花算法 ID_WORKER(number), ID_WORKER_STR(string)
    // @TableId(type= IdType.UUID) // UUID
    // @TableId(type= IdType.ASSIGN_ID)
    private Long id;

    @TableField(condition= SqlCondition.LIKE)
    private String name;
    private Integer age;
    private String email;

    /**
     * 直属上级
     */
    private Long managerId;
    // since JDK1.8
    private LocalDateTime createTime;
}