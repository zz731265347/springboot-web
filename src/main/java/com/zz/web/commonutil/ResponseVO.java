package com.zz.web.commonutil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseVO   implements Serializable {
    private String code ;
    private String msg ;
    private  Object data ;


    public   ResponseVO success(Object object){

        ResponseVO result = new ResponseVO();
        result.setCode("200");
        result.setMsg("成功");
        result.setData(object);
        return  result;

    }



    public   ResponseVO fail(String msg){
        ResponseVO result = new ResponseVO();
        result.setCode("400");
        result.setMsg(msg);
        return  result;

    }

    public  ResponseVO error500(){
        ResponseVO result = new ResponseVO();
        result.setCode("500");
        result.setMsg("服务器内部错误，请稍后重试");
        return  result;

    }







}
