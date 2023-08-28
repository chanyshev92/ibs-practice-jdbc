package repositories;

import models.Food;

import java.util.List;

public interface FoodsRepository {
    //language=SQL
    String SQL_SELECT_ALL = "select * from FOOD order by food_id";

    //language=SQL
    String SQL_INSERT = "insert into FOOD(FOOD_NAME,FOOD_TYPE,FOOD_EXOTIC) values (?,?,?)";

    //language=SQL
    String SQL_DELETE_BY_DESCRIPTION = "delete from FOOD where FOOD_ID = (select max(FOOD_ID) from FOOD where FOOD_NAME=? and FOOD_TYPE=? and FOOD_EXOTIC =?)";

    void save(Food product);
    List<Food> findAll();
    void delete(Food product);
    boolean checkByDescription(Food product);
}
