package com.tigerjoys.shark.supersign.configuration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.tigerjoys.shark.supersign.constant.Const;

/**
 * 解析配置获取Datasource
 *
 */
@Configuration
@EnableConfigurationProperties(DruidProperties.class)
public class DruidDataSourceConfiguration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DruidDataSourceConfiguration.class);

	@Autowired
	private DruidProperties properties;

    /**
     * 生成DruidDataSource数据源
     * @return DataSource
     * @throws SQLException 
     */
	@Bean()
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource writeDataSource() throws SQLException {
    	LOGGER.info("注入druid！！！");
        
        DruidDataSource datasource = new DruidDataSource();
        
        //如果是测试test环境，则读取配置文件
        datasource.setUrl(properties.getUrl());
        datasource.setUsername(properties.getUsername());
        datasource.setPassword(properties.getPassword());
        datasource.setDriverClassName(properties.getDriverClassName());
        datasource.setMaxActive(properties.getMaxActive());
        datasource.setMinIdle(properties.getMinIdle());
        datasource.setInitialSize(properties.getInitialSize());
        datasource.setMaxWait(properties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
        datasource.setConnectionProperties(properties.getConnectionProperties());
        datasource.setValidationQuery(properties.getValidationQuery());
        datasource.setConnectionInitSqls(properties.getConnectionInitSqls());
        datasource.setTestWhileIdle(properties.isTestWhileIdle());
        datasource.setTestOnBorrow(properties.isTestOnBorrow());
        datasource.setTestOnReturn(properties.isTestOnReturn());
        
        List<Filter> filters = new ArrayList<>(2);
        //测试环境需要打印SQL查询
        if(Const.IS_TEST) {
        	filters.add(logFilter());
        }
        //SQL状态
    	filters.add(statFilter());
    	
    	if(!Const.IS_TEST) {
    		//SQL防火墙
        	filters.add(wallFilter(datasource));
    	}
        
        //记录日志，SQL状态,wall等
        datasource.setProxyFilters(filters);
        
        return datasource;
    }
    
    /**
     * Druid log filter设置
     * @return Slf4jLogFilter
     */
    @Bean
    public Slf4jLogFilter logFilter() {
    	Slf4jLogFilter filter = new Slf4jLogFilter();
    	filter.setConnectionLogEnabled(false);
    	filter.setStatementLogEnabled(true);
    	filter.setResultSetLogEnabled(false);
    	filter.setStatementExecutableSqlLogEnable(false);
    	
    	return filter;
    }
    
    /**
     * Druid stat filter设置
     * @return StatFilter
     */
    @Bean
    public StatFilter statFilter() {
    	StatFilter filter = new StatFilter();
    	//合并sql 对相似sql归并到一个sql进行检测统计
    	filter.setMergeSql(properties.isMergeSql());
    	//慢查询的毫秒数
    	filter.setSlowSqlMillis(properties.getSlowSqlMillis());
    	//是否打印慢查询
    	filter.setLogSlowSql(properties.isLogSlowSql());
    	
    	return filter;
    }
    
    /**
     * SQL wall Filter设置
     * @return WallFilter
     */
    @Bean
    public WallFilter wallFilter(DataSourceProxy dataSource) {
    	WallFilter filter = new WallFilter();
    	filter.init(dataSource);
    	//初始化完成后获得WallConfig
    	
    	//因为如果配置@Bean WallConfig有一个问题，就是dbType得写死mysql后者oracle等，不优雅。
    	WallConfig config = filter.getConfig();
    	//SELECT查询中是否允许INTO字句
    	config.setSelectIntoAllow(false);
    	//truncate语句是危险，缺省打开，若需要自行关闭
    	config.setTruncateAllow(false);
    	//是否允许执行Alter Table语句
    	config.setAlterTableAllow(false);
    	//是否允许删除表
    	config.setDropTableAllow(false);
    	//是否允许执行mysql的use语句，缺省打开
    	config.setUseAllow(false);
    	//是否允许执行mysql的describe语句，缺省打开
    	config.setDescribeAllow(false);
    	//是否允许执行mysql的show语句，缺省打开
    	config.setShowAllow(false);
    	//检查DELETE语句是否无where条件，这是有风险的，但不是SQL注入类型的风险
    	config.setDeleteWhereNoneCheck(true);
    	//检查UPDATE语句是否无where条件，这是有风险的，但不是SQL注入类型的风险
    	config.setUpdateWhereNoneCheck(true);
    	//是否允许调用Connection.getMetadata方法，这个方法调用会暴露数据库的表信息[JPA不能禁用]
    	//config.setMetadataAllow(false);
    	//是否允许调用Connection/Statement/ResultSet的isWrapFor和unwrap方法，这两个方法调用，使得有办法拿到原生驱动的对象，绕过WallFilter的检测直接执行SQL。
    	config.setWrapAllow(false);
    	//批量sql
    	config.setMultiStatementAllow(true);
    	return filter;
    }

}
