package com.example.trade.goods.API;

import com.alibaba.fastjson.JSON;
import com.example.trade.goods.db.model.Goods;
import com.example.trade.goods.service.GoodsService;
import com.example.trade.goods.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
public class GoodsAPI {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SearchService searchService;

    @RequestMapping("/api/goods/insertGoods")
    @ResponseBody
    public boolean insertGoods(@RequestBody Goods goods) {
        log.info("insertGoods goods:{}", JSON.toJSON(goods));
        return goodsService.insertGoods(goods);
    }

    /**
     * 查询商品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/api/goods/queryGoodsById")
    @ResponseBody
    public Goods queryGoodsById(long id) {
        log.info("queryGoodsById id:{}", id);
        return goodsService.queryGoodsById(id);
    }


    /**
     * 根据关键词搜索
     *
     * @param keyword
     * @param from
     * @param size
     * @return
     */
    @GetMapping("/api/goods/searchGoodsList")
    @ResponseBody
    public List<Goods> searchGoodsList(String keyword, int from, int size) {
        log.info("searchGoodsList keyword:{} from:{} size:{}", keyword, from, size);
        return searchService.searchGoodsList(keyword, from, size);
    }

    @GetMapping("/api/goods/addGoodsToES")
    @ResponseBody
    void addGoodsToES(Goods goods){
        log.info("addGoodsToES Goods title:{}",goods.getTitle());
        searchService.addGoodsToES(goods);
    }


    /**
     * 锁定商品的库存
     *
     * @param id
     * @return
     */
    @GetMapping("/api/goods/lockStock")
    @ResponseBody
    public boolean lockStock(long id,int num) {
        log.info("lockStock id:{}", id);
        return goodsService.lockStock(id,num);
    }

    /**
     * 库存扣减
     *
     * @param id
     * @return
     */
    @GetMapping("/api/goods/deductStock")
    @ResponseBody
    public boolean deductStock(long id,int num) {
        log.info("deductStock id:{}", id);
        return goodsService.deductStock(id,num);
    }

    /**
     * 锁定的库存回补
     *
     * @param id
     * @return
     */
    @GetMapping("/api/goods/revertStock")
    @ResponseBody
    public boolean revertStock(long id,int num) {
        log.info("revertStock id:{}", id);
        return goodsService.revertStock(id,num);
    }
}
