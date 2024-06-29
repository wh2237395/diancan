package com.cslg.controller;

import com.cslg.api.FoodRes;
import com.cslg.api.LeimuVO;
import com.cslg.api.ResultVO;
import com.cslg.bean.*;
import com.cslg.enumeration.FoodStatusEnum;
import com.cslg.repository.FoodRepository;
import com.cslg.repository.LeiMuRepository;
import com.cslg.repository.OrderDetailRepository;
import com.cslg.repository.OrderRootRepository;
import com.cslg.utils.ApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 小程序买家端,菜品列表
 */
@RestController
@Slf4j
public class WxFoodController {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private LeiMuRepository leiMuRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRootRepository orderRootRepository;

    /*
     * 返回菜单和菜品列表
     * */
    @GetMapping("/buyerfoodList")
    public ResultVO list(@RequestParam("searchKey") String searchKey) {
        log.info("搜索词={}", searchKey);
        List<Food> foodList = new ArrayList<>();
        if (StringUtils.pathEquals("all", searchKey)) {
            //返回所有菜品
            foodList = foodRepository.findByFoodStatus(FoodStatusEnum.UP.getCode());
        } else {
            //查询菜品
            foodList = foodRepository.findByFoodStatusAndFoodNameContaining(FoodStatusEnum.UP.getCode(), searchKey);
            log.info("搜索结果={}", foodList);
        }

        return zuZhuang(foodList);
    }

    /*
     * 返回菜单推荐列表
     * */
    @GetMapping("/buyerrecommendedfoodList")
    public ResultVO buyerrecommendedfoodList(String openId) {
        List<Food> foodList = new ArrayList<>();
        if (StringUtils.isEmpty(openId)) {
            foodList = foodRepository.findByFoodStatus(FoodStatusEnum.UP.getCode());

        }else {
            //查询推荐的菜品
            foodList = recommendGoods(openId);
            if(foodList.size()==0){
                foodList = foodRepository.findByFoodStatus(FoodStatusEnum.UP.getCode());
            }
      foodList =  foodList.stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Food::getFoodId)))
 ,ArrayList ::new   ));

        }

        return zuZhuang(foodList);
    }

    public ResultVO zuZhuang(List<Food> foodList) {
        List<Integer> categoryTypeList = foodList.stream()
                .map(e -> e.getLeimuType())
                .collect(Collectors.toList());
        List<Leimu> leimuList = leiMuRepository.findByLeimuTypeIn(categoryTypeList);

        //3. 数据拼装
        List<LeimuVO> leimuVOList = new ArrayList<>();
        for (Leimu leimu : leimuList) {
            LeimuVO leimuVO = new LeimuVO();
            leimuVO.setLeimuType(leimu.getLeimuType());
             leimuVO.setLeimuName(leimu.getLeimuName());

            List<FoodRes> foodResList = new ArrayList<>();
            for (Food food : foodList) {
                if (food.getLeimuType().equals(leimu.getLeimuType())) {
                    FoodRes foodRes = new FoodRes();
                    BeanUtils.copyProperties(food, foodRes);
                    foodResList.add(foodRes);
                }
            }
            leimuVO.setFoodResList(foodResList);
            leimuVOList.add(leimuVO);
        }

        return ApiUtil.success(leimuVOList);
    }



    /**推荐算法*/
    public List<Food> recommendGoods(String openId){
        List<Food> recommendGoods = new ArrayList<>();
        //协同过滤算法
        CoreMath coreMath = new CoreMath();
        //获取商品数据
        List<RelateDTO> relateDTOList = getRelateData(openId);
        if(relateDTOList.size()==0){
            return  recommendGoods;
        }
        //执行算法，返回推荐商品id
        List<Integer> recommendIdLists = coreMath.recommend(openId, relateDTOList);
        if(recommendIdLists.size()>0){
            for(int i = 0 ;i <recommendIdLists.size();i++){
                if(i>10){
                    return recommendGoods;
                }
                Optional<Food> food = foodRepository.findById(recommendIdLists.get(i));
                recommendGoods.add(food.get());
            }
        }
        return recommendGoods;
    }



    public List<RelateDTO> getRelateData(String openId){
        List<RelateDTO> relateDTOList = new ArrayList<>();
        List<WxOrderDetail> wxOrderDetailList = new ArrayList<>();
        List<WxOrderRoot> wxOrderRoots = orderRootRepository.findByBuyerOpenid(openId);
        wxOrderRoots.stream().forEach(wxOrderRoot -> {
            List<WxOrderDetail> wxOrderDetails = new ArrayList<>();
            wxOrderDetails = orderDetailRepository.findByOrderId(wxOrderRoot.getOrderId());
            wxOrderDetailList.addAll(wxOrderDetails);
        });
        Map<Integer, List<WxOrderDetail>> collect = wxOrderDetailList.stream().collect(Collectors.groupingBy(WxOrderDetail::getFoodId));
        //
        for (WxOrderDetail wxOrderDetail : wxOrderDetailList) {
            RelateDTO relateDTO = new RelateDTO();
            relateDTO.setUserId(openId);
            relateDTO.setProductId(wxOrderDetail.getFoodId());
            //购买商品的次数
            relateDTO.setIndex(collect.get(wxOrderDetail.getFoodId()).size());
            //不能设置为1，否则皮尔森系数计算为0.0
            relateDTOList.add(relateDTO);
        }
        return relateDTOList;
    }


    static <T> Iterable<T> iterableReverseList(final List<T> l) {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    ListIterator<T> listIter = l.listIterator(l.size());
                    public boolean hasNext() { return listIter.hasPrevious(); }
                    public T next() { return listIter.previous(); }
                    public void remove() { listIter.remove(); }
                };
            }
        };
    }
}

