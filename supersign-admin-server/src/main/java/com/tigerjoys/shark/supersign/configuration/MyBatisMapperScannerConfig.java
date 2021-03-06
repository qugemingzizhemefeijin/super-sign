package com.tigerjoys.shark.supersign.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import com.tigerjoys.nbs.mybatis.core.utils.SpringBeanApplicationContext;

@Configuration
public class MyBatisMapperScannerConfig implements EnvironmentAware , ApplicationContextAware {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisMapperScannerConfig.class);
	
    private ApplicationContext applicationContext;
    
    private String entityBasepackage;
    
    private String mapperBasepackage;
	
    @Override
    public void setEnvironment(Environment env) {
    	this.entityBasepackage = env.getProperty("application.entity-basepackage");
    	this.mapperBasepackage = env.getProperty("application.mapper-basepackage");
    }
    
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Bean(name = "sqlSessionFactory")
	@ConditionalOnMissingBean(SqlSessionFactory.class)
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfigLocation(new ClassPathResource("/mybatis/supersign/mapper.xml"));
        //bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("/mybatis/pay/mapper.xml"));

        try {
            return bean.getObject();
        } catch (Exception e) {
        	LOGGER.error(e.getMessage() , e);
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * ?????? SqlSessionTemplate ???
	 * @param sqlSessionFactory - SqlSessionFactory
	 * @return SqlSessionTemplate
	 */
	@Bean(name="sqlSession")
	@ConditionalOnMissingBean(SqlSessionTemplate.class)
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
	
	/**
	 * ??????MapperScannerConfigurer???????????????
	 * @return MapperScannerConfigurer
	 */
	@Bean
	@ConditionalOnMissingBean(MapperScannerConfigurer.class)
    public MapperScannerConfigurer mapperScannerConfigurer() {
		SpringBeanApplicationContext context = new SpringBeanApplicationContext();
		context.setEntityBasepackage(this.entityBasepackage);
		context.setApplicationContext(this.applicationContext);
		context.init();
		
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //?????????????????????beanName???sqlSessionFactory?????????
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        //??????xml?????????????????????
        mapperScannerConfigurer.setBasePackage(this.mapperBasepackage);
        return mapperScannerConfigurer;
    }

}
