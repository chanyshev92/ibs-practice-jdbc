package repositories;

import models.Food;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class FoodsRepositoryImplementation implements FoodsRepository{

    private final DataSource dataSource;

    public FoodsRepositoryImplementation(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final Function<ResultSet,Food> rowMapper=row->{
        try {
            return Food.builder()
                    .food_name(row.getString("food_name"))
                    .food_type(row.getString("food_type"))
                    .food_exotic(row.getInt("food_exotic"))
                    .build();
        }
        catch (SQLException e){
            throw new IllegalStateException(e);
        }
    };

    @Override
    public void save(Food product) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, product.getFood_name());
            preparedStatement.setString(2, product.getFood_type());
            preparedStatement.setInt(3,product.getFood_exotic());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Не получилось добавить продукт");
            }
            try (ResultSet generatedId = preparedStatement.getGeneratedKeys()) {
                if (generatedId.next()){
                    product.setFood_id(generatedId.getInt("food_id"));
                }else {
                    throw new SQLException("Не получилось добавить сгенерированный id");
                }
            }
        }catch (SQLException e){
            throw new IllegalStateException("Ошибка при сохранении продукта",e);
        }
    }

    @Override
    public List<Food> findAll() {
        List<Food> foodList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()){
            try (ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {
                while (resultSet.next()) {
                    Food product = rowMapper.apply(resultSet);
                    foodList.add(product);
                }
            }
        }catch (SQLException e){
            throw new IllegalStateException(e);
        }

        return foodList;
    }

    @Override
    public void delete(Food product) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BY_DESCRIPTION, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, product.getFood_name());
            preparedStatement.setString(2, product.getFood_type());
            preparedStatement.setInt(3,product.getFood_exotic());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Не получилось удалить продукт");
            }
        }catch (SQLException e){
            throw new IllegalStateException(e);
        }
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
