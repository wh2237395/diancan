package com.cslg.repository;

import com.cslg.bean.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 编程小石头：2501902696（微信）
 * 菜品repository
 */
public interface FoodRepository extends JpaRepository<Food, Integer> {

    List<Food> findByFoodStockLessThan(int num);//查询库存少于num的菜品

    List<Food> findByFoodStatusAndFoodNameContaining(Integer foodStatus, String name);

    List<Food> findByFoodStatus(Integer foodStatus);

}
