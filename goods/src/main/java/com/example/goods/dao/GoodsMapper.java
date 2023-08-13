package com.example.goods.dao;

import com.example.goods.type.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsMapper {

    List<Goods> select(@Param("goods") Goods goods);

}
