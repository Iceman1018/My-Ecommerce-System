package com.example.trade.goods;

import com.alibaba.fastjson.JSON;
import com.example.trade.goods.db.dao.GoodsDao;
import com.example.trade.goods.db.model.Goods;
import com.example.trade.goods.service.GoodsService;
import com.example.trade.goods.service.SearchService;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsTest {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SearchService searchService;

    @Test
    public void goodsInsertTest(){
        System.out.println("hello");
    }
    @Test
    public void insertGoodsTest(){
        Goods goods=new Goods();
        goods.setTitle("iphone 14 pro max");
        goods.setBrand("Apple");
        goods.setCategory("cell phone");
        goods.setNumber("NO123456");
        goods.setImage("test");
        goods.setDescription("newest apple phone");
        goods.setKeywords("Apple phone");
        goods.setLockStock(0);
        goods.setSaleNum(0);
        goods.setPrice(9999);
        goods.setAvailableStock(9999);
        goods.setStatus(1);
        goods.setCreateTime(new Date());
        boolean insertResult= goodsService.insertGoods(goods);
        System.out.println(insertResult);
    }
    @Test
    public void queryGoodsTest(){
        Goods goods=goodsService.queryGoodsById(17);
        System.out.println(goods);
    }
    @Test
    public void esTest(){
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("http://127.0.0.1/",9200,"http")));
        System.out.println(JSON.toJSONString(client));
    }

    @Test
    public void addGoodsToES(){
        Goods goods= new Goods();
        goods.setTitle("iphone 14 pro max");
        goods.setBrand("Apple");
        goods.setCategory("phone");
        goods.setNumber("NO1234567");
        goods.setImage("test");
        goods.setDescription("iphone 14 pro max is pretty good");
        goods.setKeywords("apple iphone");
        goods.setSaleNum(68);
        goods.setAvailableStock(10000);
        goods.setPrice(999999);
        goods.setStatus(1);
        searchService.addGoodsToES(goods);
    }

    @Test
    public void searchGoodTest(){
        List<Goods> goodsList=searchService.searchGoodsList("apple",0,2);
        System.out.println(JSON.toJSONString(goodsList));
    }
}
