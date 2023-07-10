package com.gxcom.reggie.controller;

import com.gxcom.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;
    @PostMapping("/upload")
    /**
     * 文件上传
     */
    public R<String> upload(MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，不然请求结束后会自动删除
        log.info(file.toString());
        //获取源文件名
        String originalFilename = file.getOriginalFilename();
        //获取xxx.jsp的图片格式
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
       //UUID随机生成一个文件名
        String fileName = UUID.randomUUID().toString()+suffix;
        //创建一个目录对象
        File dir = new File(basePath);
        //判断当前目录是否存在
        if (!dir.exists()){
            //目录不存在，创建目录
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void  downlaod(String name, HttpServletResponse response){
        try {
            //通过输入的方法读取文件，俗称输入流
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
           //通过输出的方法将文件写回浏览器，在浏览器展示图片,俗称输出流
            ServletOutputStream outputStream = response.getOutputStream();

            //("/image/jpeg");固定写法
            response.setContentType("/image/jpeg");
            int len=0;
            byte [] bytes = new byte[1024];
            while ((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭资源
            fileInputStream.close();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
