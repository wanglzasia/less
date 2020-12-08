package com.less.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Spring {

    public static DataSource dataSource() throws Exception{
        //1.
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        //2.创建SqlSessionFactory工厂
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory1 = sqlSessionFactoryBuilder.build(in);
        DataSource dataSource = sqlSessionFactory1.getConfiguration().getEnvironment().getDataSource();
        return dataSource;
    }

    public static void main(String[] args) throws Exception {

        //1、配置dataSourxe
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());

        //2、配置mapperLocations
        String mapperLocations = "classpath*:com/less/mybatis/UserDao.xml";
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

        List<Resource> resources = new ArrayList();
        Resource[] mappers = resourceResolver.getResources(mapperLocations);
        resources.addAll(Arrays.asList(mappers));
        factoryBean.setMapperLocations(resources.toArray(new Resource[resources.size()]));

        //2.1配置事务
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());



        //3、生成SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = factoryBean.getObject();

        //4、拿到sqlSessionTemplate
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);

        //TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        User user = new User();
        user.setUsername("小强-spring");
        user.setBirthday(new Date());
        user.setAddress("sadfsafsafs");
        user.setSex("2");
        int i = sqlSessionTemplate.insert("test.insertUser", user);

        //transactionManager.rollback(txStatus);

        //sqlSessionTemplate.commit();

    }
}
