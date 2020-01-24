package io.markshen.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * https://mp.baomidou.com/guide/page.html
 */
@Configuration
@MapperScan("io.markshen.dao")
public class MybatisPlusConfig {

    @Bean
    public OptimisticLockerInterceptor getOptimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    // 3.2 版本以下可以使用，3.2以上版本使用插件来解决
    // https://mp.baomidou.com/guide/performance-analysis-plugin.html

    /**
     * 3.3.1 以下版本， 需要配置
     *
     * @return
     */
//    public ISqlInjector sqlInjector() {
//        return new LogicSqlInjector();
//    }
}
