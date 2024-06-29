package com.cslg.bean;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CoreMath {

    /**
     * 根据前三个最相似的用户进行推荐
     * @param userId 用户id
     * @param list 推荐的商品idList集合
     * @return
     */
    public List<Integer> recommend(String userId, List<RelateDTO> list) {
        //相关强度： 相关系数 0.8-1.0 极强相关 0.6-0.8 强相关 0.4-0.6 中等程度相关 0.2-0.4 弱相关 0.0-0.2 极弱相关或无相关
        Map<Double, String> distances = computeNearestNeighbor(userId, list);
        // 找出前三个相似的用户
        List<String> similarUserIdList = new ArrayList<>();
        List<String> values = new ArrayList<>(distances.values());
        int size = values.size();
        if(size >= 3) {
            Collections.sort(values,Collections.reverseOrder());
            similarUserIdList = values.stream().limit(3).collect(Collectors.toList());
//            List<Integer> top3 = Arrays.asList(values.get(size - 1), values.get(size - 2), values.get(size - 3));
//            similarUserIdList.addAll(top3);
        }
        //对每个用户的购买商品记录进行分组
        Map<String, List<RelateDTO>> userMap =list.stream().collect(Collectors.groupingBy(RelateDTO::getUserId));
        //前三名相似用户购买过的商品
        List<Integer> similarProductIdList = new ArrayList<>();
        for (String similarUserId : similarUserIdList) {
            //获取相似用户购买商品的记录
            List<Integer> collect = userMap.get(similarUserId).stream().map(e -> e.getProductId()).collect(Collectors.toList());
            //过滤掉重复的商品
            List<Integer> collect1 = collect.stream().filter(e -> !similarProductIdList.contains(e)).collect(Collectors.toList());
            similarProductIdList.addAll(collect1);
        }
        //当前登录用户购买过的商品
        List<Integer> userProductIdList = userMap.get(userId).stream().map(e -> e.getProductId()).collect(Collectors.toList());
        //相似用户买过，但是当前用户没买过的商品作为推荐
        List<Integer> recommendList = new ArrayList<>();
        for (Integer similarProduct : similarProductIdList) {
            if(!userProductIdList.contains(similarProduct)){
                recommendList.add(similarProduct);
            }
        }
        Collections.sort(recommendList);
        //如果相识性为0
        if(recommendList.size()==0){
            Collections.sort(userProductIdList);
            return userProductIdList;
        }
        return recommendList;
    }

    /**
     * 在给定userId的情况下，计算其他用户和它的相关系数并排序
     * @param userId
     * @param list
     * @return
     */
    private Map<Double, String> computeNearestNeighbor(String userId, List<RelateDTO> list) {
        Map<String, List<RelateDTO>> userMap = list.stream().collect(Collectors.groupingBy(RelateDTO::getUserId));
        Map<Double, String> distances = new TreeMap<>();
        userMap.forEach((k,v)->{
            if( !k.equals(userId)){
                double distance = pearson_dis(v,userMap.get(userId));
                distances.put(distance, k);
            }
        });
        return distances;
    }

    /**
     * 计算两个序列间的相关系数
     *
     * @param xList
     * @param yList
     * @return
     */
    private double pearson_dis(List<RelateDTO> xList, List<RelateDTO> yList) {
        List<Integer> xs= Lists.newArrayList();
        List<Integer> ys= Lists.newArrayList();
        xList.forEach(x->{
            yList.forEach(y->{
                //购买的商品相同，交集
                if(x.getProductId().equals(y.getProductId())){
                    //TODO 将购买次数大于5的加入统计计算
                    xs.add(x.getIndex());
                    ys.add(y.getIndex());
                }
            });
        });
        return getRelate(xs,ys);
    }

    /**
     * 方法描述: 皮尔森（pearson）相关系数计算
     * 余弦相似度：越接近于 1 ，说明两个用户的浏览行为越相似
     * @param xs
     * @param ys
     * @Return {@link Double}
     * @throws
     */
    public static Double getRelate(List<Integer> xs, List<Integer> ys){
        int n=xs.size();
        double Ex= xs.stream().mapToDouble(x->x).sum();
        double Ey=ys.stream().mapToDouble(y->y).sum();
        double Ex2=xs.stream().mapToDouble(x-> Math.pow(x,2)).sum();
        double Ey2=ys.stream().mapToDouble(y-> Math.pow(y,2)).sum();
        double Exy= IntStream.range(0,n).mapToDouble(i->xs.get(i)*ys.get(i)).sum();
        double numerator=Exy-Ex*Ey/n;
        double denominator= Math.sqrt((Ex2- Math.pow(Ex,2)/n)*(Ey2- Math.pow(Ey,2)/n));
        if (denominator==0) {
            return 0.0;
        }
        return numerator/denominator;
    }

}

