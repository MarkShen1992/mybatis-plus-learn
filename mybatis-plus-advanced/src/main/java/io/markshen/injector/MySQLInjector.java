package io.markshen.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import io.markshen.method.DeleteAllDataMethod;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MySQLInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> abstractMethods = super.getMethodList(mapperClass);
        abstractMethods.add(new DeleteAllDataMethod());
        return abstractMethods;
    }
}
