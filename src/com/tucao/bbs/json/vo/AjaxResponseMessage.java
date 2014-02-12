package com.tucao.bbs.json.vo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Star
 * Date: 12-8-21
 * Time: 下午4:12
 * To change this template use File | Settings | File Templates.
 */
public class AjaxResponseMessage {
    private Boolean success = true;
    private String msg;
    private Object Data;
    private List datas;
    public void setData(Object data){
        this.Data = data;
    }
    public Object getData(){
        return Data;
    }
    public AjaxResponseMessage(){}

    public AjaxResponseMessage(Boolean success ,String msg){
        this.success = success;
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List getDatas() {
        return datas;
    }

    public void setDatas(List datas) {
        this.datas = datas;
    }
}
