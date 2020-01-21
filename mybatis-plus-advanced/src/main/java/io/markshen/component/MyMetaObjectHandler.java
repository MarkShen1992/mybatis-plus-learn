package io.markshen.component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入时如何填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasSetter("createTime")) {
            setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        }
    }

    /**
     * 更新时如何填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 自己设置值的时候就不会执行下面的语句
        Object val = getFieldValByName("updateTime", metaObject);
        if (val == null) {
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }
    }
}