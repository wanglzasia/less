package com.less.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * 演示mybatis普通配置过程
 */
public class Common {


    public static void main(String[] args) throws Exception {
        Dao dao = new Dao();
        dao.insertUser();
    }
}
