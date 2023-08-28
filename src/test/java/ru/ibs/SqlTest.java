package ru.ibs;

import models.Food;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Класс с проверками
 */

public class SqlTest extends BaseTests{
    @DisplayName("Проверка добавления продукта с валидными данными в таблицу food")
    @ParameterizedTest
    @CsvSource(value={"Ананас, FRUIT,1"})
    public void testAddValidFood(String foodName, String foodType, int exotic){
        //Предусловие: соединение с БД установлено,в БД есть непустая таблица food
        Assertions.assertFalse(foodsRepository.findAll().isEmpty(),
                "Получена пустая таблица Food");
        System.out.println(foodsRepository.findAll());
        //Добавить в таблицу food продукт с параметрами
        Food newProduct= Food.builder()
                .food_name(foodName)
                .food_type(foodType)
                .food_exotic(exotic).build();
        System.out.println(newProduct);
        foodsRepository.save(newProduct);
        System.out.println(newProduct);
        System.out.println(foodsRepository.findAll());
        //Проверить, что добавленный продукт присутствует в таблице
        Assertions.assertTrue(foodsRepository.checkByDescription(newProduct),
                "Продукт с введенными данными не найден в таблице");

        //Постусловие: удалить добавленный продукт через запрос
        foodsRepository.delete(newProduct);
        //Проверить, что добавленный продукт удалился
        Assertions.assertFalse(foodsRepository.checkByDescription(newProduct),
                "Продукт с введенными данными не удалился");
        System.out.println(foodsRepository.findAll());
    }
}
