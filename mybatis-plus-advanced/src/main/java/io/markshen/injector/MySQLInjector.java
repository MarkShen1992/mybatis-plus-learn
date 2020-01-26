package io.markshen.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.injector.methods.LogicDeleteByIdWithFill;
import io.markshen.method.DeleteAllDataMethod;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MySQLInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> abstractMethods = super.getMethodList(mapperClass);
        abstractMethods.add(new DeleteAllDataMethod());
        abstractMethods.add(new InsertBatchSomeColumn(t -> !t.isLogicDelete() && !t.getColumn().equals("age")));
        abstractMethods.add(new LogicDeleteByIdWithFill());
        return abstractMethods;
    }
}
