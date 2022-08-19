package com.xiaoxiz.xiaomaibu.controller;

import com.xiaoxiz.xiaomaibu.util.dataresult.DataResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

@RestController
public class UploadController {
    private String filename;
    @PostMapping("/Upload")
    public DataResult springUpload(HttpServletRequest request) throws IllegalStateException, IOException {
        long  startTime=System.currentTimeMillis();
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request))
        {

            //将request变成多部分request
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
            //获取multiRequest 中所有的文件名
            Iterator iter=multiRequest.getFileNames();
            System.out.println(iter.hasNext());
            while(iter.hasNext())
            {
                //一次遍历所有文件
                MultipartFile file=multiRequest.getFile(iter.next().toString());
                System.out.println(file);
                if(file!=null)
                {

                    //获取图片后缀名
                    String format=file.getOriginalFilename()
                            .substring(file.getOriginalFilename().lastIndexOf("."));
                    //图片命名
                    filename = UUID.randomUUID().toString().replaceAll("-", "")+format;
                    // 上传文件/图像到指定文件夹（这里可以改成你想存放地址的相对路径）
                    File savePos = new File("src/main/resources/static/img");
                    if(!savePos.exists()){  // 不存在，则创建该文件夹
                        savePos.mkdir();
                    }
                    String realPath = savePos.getCanonicalPath();
                    System.out.println(realPath);
                    //上传
                    file.transferTo(new File(realPath+"/"+filename));
                    System.out.println(file);
                }

            }
        }
        long  endTime=System.currentTimeMillis();
        System.out.println("方法三的运行时间："+String.valueOf(endTime-startTime)+"ms");
        return DataResult.success(filename);
    }

}