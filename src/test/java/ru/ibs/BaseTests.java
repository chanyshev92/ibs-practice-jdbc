package ru.ibs;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import repositories.FoodsRepository;
import repositories.FoodsRepositoryImplementationJdbc;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Класс для доступа к БД
 */
public class BaseTests {

    private static DataSource dataSource;

    protected FoodsRepository foodsRepository;

    /**
     * Выполняется перед всеми тестами
     */
    @BeforeAll
    public static void beforeAll() {
        /*dataSource=new DriverManagerDataSource(
                "jdbc:h2:tcp://localhost:9092/mem:testdb",
                "user","pass");*/
        Properties dbProperties = new Properties();

        try {
            dbProperties.load(new BufferedReader(
                    new InputStreamReader(BaseTests.class.getResourceAsStream("/db.properties"))));
        } catch (IOException e) {
            throw new IllegalArgumentException("Не удалось загрузить файл db.properties",e);
        }

        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setPassword(dbProperties.getProperty("db.password"));
        hikariDataSource.setUsername(dbProperties.getProperty("db.username"));
        hikariDataSource.setJdbcUrl(dbProperties.getProperty("db.url"));
        hikariDataSource.setMaximumPoolSize(Integer.parseInt(dbProperties.getProperty("db.hikari.maxPoolSize")));

        dataSource=hikariDataSource;
    }

    /**
     * Выполняется перед каждым тестом
     */
    @BeforeEach
    public void before() {
        foodsRepository=new FoodsRepositoryImplementationJdbc(dataSource);
    }

    /**
     * Выполняется после каждого теста
     */
    @AfterEach
    public void after() {
        foodsRepository=null;
    }

    /**
     * Выполняется после всех тестов
     */
    @AfterAll
    public static void afterAll() {
        dataSource=null;
    }
}
