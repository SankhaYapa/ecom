package com.app.db;


import com.app.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Properties;

public class HibernateUtil {
    private static final SessionFactory SESSION_FACTORY=buildSessionFactory();
    private static SessionFactory buildSessionFactory(){
        Properties properties = new Properties();
        try{
            properties.load(HibernateUtil.class.getClassLoader()
                    .getResourceAsStream("hibernate.properties"));
        }catch (Exception e){
            e.printStackTrace();
        }
        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .applySettings(properties)
                .build();

        Metadata metadata = new MetadataSources(standardRegistry)
                .addAnnotatedClass(User.class)
                .getMetadataBuilder()
                .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
                .build();

        return metadata.getSessionFactoryBuilder()
                .build();
    }

    public static Session getSession(){
        return SESSION_FACTORY.openSession();
    }
}
