package io.markshen.config;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * https://mp.baomidou.com/guide/page.html
 */
@Configuration
@MapperScan("io.markshen.dao")
public class MybatisPlusConfig {

    /**
     * 多租户配置
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        List<ISqlParser> iSqlParserList = new ArrayList<>();
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId(boolean where) {
                // 实际manager_id对应的值, Session或配置文件中或静态变量取出来的配置信息
                return new LongValue(1088248166370832385L);
            }

            @Override
            public String getTenantIdColumn() {
                return "manager_id";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                if ("role".equals(tableName)) {
                    return true;
                }
                return false; // 加过滤
            }
        });
        iSqlParserList.add(tenantSqlParser);
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setSqlParserList(iSqlParserList);

        // 特定SQL过滤
        paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
            @Override
            public boolean doFilter(MetaObject metaObject) {
                MappedStatement ms = SqlParserHelper.getMappedStatement(metaObject);
                if ("io.markshen.dao.UserDAO.selectById".equals(ms.getId())) {
                    return true;
                }
                return false; // 不过滤
            }
        });
        return paginationInterceptor;
    }

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
