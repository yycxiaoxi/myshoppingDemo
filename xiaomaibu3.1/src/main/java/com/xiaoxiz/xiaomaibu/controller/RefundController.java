package com.xiaoxiz.xiaomaibu.controller;

import com.xiaoxiz.xiaomaibu.bean.Refund;
import com.xiaoxiz.xiaomaibu.service.RefundService;
import com.xiaoxiz.xiaomaibu.util.dataresult.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/refund")
public class RefundController {
    @Autowired
    RefundService refundService;
    @PostMapping("/insert")
    public DataResult insert(@RequestBody Refund refund){
        refundService.insert(refund);
        return DataResult.success();
    }
    @PostMapping("/update")
    public DataResult update(@RequestBody Refund refund){
        System.out.println(refund.toString());
        refundService.update(refund);
        return DataResult.success();
    }
    @PostMapping("/delete/{orderno}")
    public DataResult delete(@PathVariable String orderno){
        refundService.delete(orderno);
        return DataResult.success();
    }
}
