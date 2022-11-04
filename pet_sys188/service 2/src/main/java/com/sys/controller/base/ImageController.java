package com.sys.controller.base;

import com.sys.common.HttpBaseRequest;
import com.sys.common.RespData;
import com.sys.common.interceptor.admin.AdminAnnotation;
import com.sys.modules.JsonModel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.Date;

@Controller
@RequestMapping("/base")
@AdminAnnotation()
public class ImageController {

    public static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return " ";
    }

    public static String basePath = "H:/Job/date/2022/0410_1/android物业APPapp/源代码/property_sys/upload/";
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    @ResponseBody
    public JsonModel upload(@RequestPart("file") MultipartFile file) {
        File uploadDir = new File(basePath);
        String fileName = new Date().getTime() + "." + getFileExtension(file.getOriginalFilename());
        File uploadFile = new File(uploadDir.getAbsolutePath() + "/" + fileName);
        try {
            //通过transferTo方法进行上传
            file.transferTo(uploadFile);
            return RespData.JsonOK("/base/image?file="+fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RespData.JsonError("上传失败");
    }
    //预览图片
    @RequestMapping(method = RequestMethod.GET, path = "/image",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] image(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        String file = baseRequest.getString("file");
        if(file.equals("")){
            return new byte[0];
        }
        file = URLDecoder.decode(file,"utf-8");
        File f = new File(basePath+file);
        InputStream is = new FileInputStream(f);
        //response.setContentType("application/txt;charset=utf-8");
        response.setContentType("image/png");
        //headers.add("Content-Disposition", "attchement;filename=" + mettingFiles.getFileName());
        //HttpStatus statusCode = HttpStatus.OK;
        OutputStream outputStream = response.getOutputStream();
        byte[] bytes = new byte[is.available()];
        is.read(bytes, 0, is.available());
        return bytes;
    }
}
