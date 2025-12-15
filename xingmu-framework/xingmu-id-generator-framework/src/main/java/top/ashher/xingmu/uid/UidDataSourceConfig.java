package top.ashher.xingmu.uid;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        basePackages = "com.baidu.fsg.uid.worker.dao",
        sqlSessionFactoryRef = "uidSqlSessionFactory"
)
public class UidDataSourceConfig {

    /**
     * 创建辅助数据源 Bean
     * 对应 application.yml 中的 spring.datasource.uid
     */
    @Bean(name = "uidDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.uid")
    public DataSource uidDataSource() {
        // 使用 HikariCP 连接池
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "uidSqlSessionFactory")
    public SqlSessionFactory uidSqlSessionFactory(@Qualifier("uidDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:META-INF/mybatis/mapper/*.xml")
        );
        return bean.getObject();
    }
}