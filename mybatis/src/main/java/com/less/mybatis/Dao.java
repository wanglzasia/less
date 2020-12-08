package com.less.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.Date;

public class Dao {

    /**
     * insert user
     * @throws Exception
     */
    public void insertUser() throws Exception{
        SqlSession sqlSession = null;
        try {
            //1.读取配置文件
            InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
            //2.创建SqlSessionFactory工厂
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(in);

            //3.使用工厂生产SqlSession对象
            sqlSession = sqlSessionFactory.openSession();
            //4.执行Sql语句
            User user = new User();
            user.setUsername("小强");
            user.setBirthday(new Date());
            user.setAddress("sadfsafsafs");
            user.setSex("2");
            int i = sqlSession.insert("test.insertUser", user);

            //5. 打印结果
            System.out.println("插入id:"+user.getId());//插入id:30
            //6.释放资源
            in.close();
        }catch (Exception e1){
            sqlSession.rollback();
            e1.printStackTrace();
        }finally {
            sqlSession.commit();
            sqlSession.close();
        }
    }
}
