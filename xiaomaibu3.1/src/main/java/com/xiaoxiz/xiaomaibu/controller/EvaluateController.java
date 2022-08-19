package com.xiaoxiz.xiaomaibu.controller;

import com.xiaoxiz.xiaomaibu.bean.Ddanxiz;
import com.xiaoxiz.xiaomaibu.bean.Order;
import com.xiaoxiz.xiaomaibu.bean.Pingjia;
import com.xiaoxiz.xiaomaibu.service.DdanxizService;
import com.xiaoxiz.xiaomaibu.service.OrderService;
import com.xiaoxiz.xiaomaibu.service.PingjiaService;
import com.xiaoxiz.xiaomaibu.util.dataresult.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eva")
public class EvaluateController {
    @Autowired
    PingjiaService pingjiaService;
    @Autowired
    DdanxizService ddanxizService;
    @Autowired
    OrderService orderService;
    @PostMapping("/findEvaByOrder/{xizehao}")
    public DataResult findPingjia(@PathVariable String xizehao){
        Pingjia pingjia=pingjiaService.findEvaByOrder(xizehao);
        return DataResult.success(pingjia);
    }
    @PostMapping("/insert")
    public DataResult inserEva(@RequestBody Pingjia pingjia){
        pingjiaService.insertEva(pingjia);
        Ddanxiz ddanxiz = ddanxizService.find(pingjia.getXizehao());
        ddanxiz.setStatus("1");//已经评价
        ddanxizService.update(ddanxiz);
        List<Ddanxiz> ddanxizList =ddanxizService.findall(ddanxiz.getOrderno());
        boolean flag=true;
        for(Ddanxiz ddan : ddanxizList){
            if ("0".equals(ddan.getStatus())){
                flag=false;
                break;
            }
        }
        if(flag==true){//全部商品评价后该订单标记为已评价
            Order order=orderService.find(ddanxiz.getOrderno());
            order.setStatus(5);
            orderService.update(order);
        }

        return DataResult.success();
    }
    @PostMapping("/find/gid/{gid}")
    public DataResult findGEva(@PathVariable String gid){
        List<Pingjia> pingjiaList= pingjiaService.findGEva(gid);
        return DataResult.success(pingjiaList);
    }
}
