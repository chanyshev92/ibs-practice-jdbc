package repositories;

import models.Food;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;
public class FoodsRepositoryImplementationJdbc implements FoodsRepository {

    private static final RowMapper<Food> rowMapper = (row, rowNum) -> new Food(row.getInt("food_id"),
            row.getString("food_name"),
            row.getString("food_type"),
            row.getInt("food_exotic"));

    JdbcTemplate jdbcTemplate;

    public FoodsRepositoryImplementationJdbc(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(Food product) {
        jdbcTemplate.update(SQL_INSERT, product.getFood_name(), product.getFood_type(), product.getFood_exotic());
        Object[] inserts = {product.getFood_name(), product.getFood_type(), product.getFood_exotic()};
        product.setFood_id(jdbcTemplate.queryForObject("select max(FOOD_ID) from FOOD where FOOD_NAME=? and FOOD_TYPE=? and FOOD_EXOTIC =? ",Integer.TYPE,inserts));
    }

    @Override
    public List<Food> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, rowMapper);
    }

    @Override
    public void delete(Food product) {
        jdbcTemplate.update(SQL_DELETE_BY_DESCRIPTION, product.getFood_name(), product.getFood_type(), product.getFood_exotic());
    }

    @Override
    public boolean checkByDescription(Food product) {
        return findAll()
                .stream()
                .anyMatch(food -> food.getFood_name().equals(product.getFood_name())
                        && food.getFood_type().equals(product.getFood_type())
                        && food.getFood_exotic() == product.getFood_exotic());
    }
}
