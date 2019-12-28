package io.markshen.entity;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class User {

    private Long id;
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