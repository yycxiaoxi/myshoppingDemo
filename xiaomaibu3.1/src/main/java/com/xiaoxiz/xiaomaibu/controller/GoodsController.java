package com.xiaoxiz.xiaomaibu.controller;

import com.xiaoxiz.xiaomaibu.bean.Goods;
import com.xiaoxiz.xiaomaibu.service.GoodsService;
import com.xiaoxiz.xiaomaibu.util.dataresult.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController  {
    @Autowired
    private GoodsService goodsService;
    @PostMapping("/insert")
    @ResponseBody
    public DataResult insert(@RequestBody Goods goods) {
        goodsService.insert(goods);
        return DataResult.success();
    }
    @GetMapping("/findall")
    public DataResult findall() {
        List<Goods> goodsList =goodsService.findall();
        return DataResult.success(goodsList);
    }
    @GetMapping("/find/{gid}")
    public DataResult find(@PathVariable String gid) {
        Goods goods=goodsService.find(gid);
        return DataResult.success(goods);
    }

    @PostMapping("/update")
    public DataResult update(@RequestBody Goods goods) {
        goodsService.update(goods);
        return DataResult.success(goodsService.find(goods.getGid()));
    }

    @PostMapping("/delete/{gid}")
    public DataResult delete(@PathVariable String gid) {
        goodsService.delete(gid);
        return DataResult.success();
    }

    @GetMapping("/findLike/{like}")
    public DataResult findLike(@PathVariable String like) {
        return DataResult.success(goodsService.findLike(like));
    }

    @GetMapping("/findSort/{sort}")
    public DataResult findSort(@PathVariable String sort) {
        return DataResult.success(goodsService.findSort(sort));
    }

    @PostMapping("/find/cart")
    public DataResult findCart(@RequestBody List<String> gidList){
        List<Goods> goodsList=new ArrayList<>();
        for (String gid:
             gidList) {
            Goods goods=goodsService.find(gid);
            goodsList.add(goods);
        }
        return DataResult.success(goodsList);
    }
}
