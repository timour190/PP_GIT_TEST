package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {

    private static final String dbUrl = "jdbc:mysql://localhost:3306/usersbase";
    private static final String dbUserName = "root";
    private static final String dbPass = "123456789"; //"my179sql";
    Driver driver = new com.mysql.cj.jdbc.Driver();
    private static SessionFactory sessionFactory;

    public Util() throws SQLException {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = getConfiguration();

                configuration.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                System.out.println("Ошибка создания/настройки sessionFactory");
            }
        }
        return sessionFactory;
    }

    private static Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        Properties settings = new Properties();
        settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        settings.put(Environment.URL, "jdbc:mysql://localhost:3306/usersbase?useSSL=false");
        settings.put(Environment.USER, "root");
        settings.put(Environment.PASS, dbPass);
        settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");

        settings.put(Environment.SHOW_SQL, "true");

        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

        settings.put(Environment.HBM2DDL_AUTO, "none");

        configuration.setProperties(settings);
        return configuration;
    }


    private Connection connection;

    public Connection getConnection() {
        try {
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(dbUrl, dbUserName, dbPass);
        } catch (SQLException e) {
            System.out.println("Соединение не установлено или ошибка соединения");
        }
        return connection;
    }
}
