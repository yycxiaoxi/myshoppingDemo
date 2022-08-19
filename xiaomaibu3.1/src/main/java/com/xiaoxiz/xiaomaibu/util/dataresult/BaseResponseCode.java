package com.xiaoxiz.xiaomaibu.util.dataresult;

public enum BaseResponseCode implements ResponseCodeInterface{
    /**
     * 这个要和前段约定好
     *code=0：服务器已成功处理了请求。 通常，这表示服务器提供了请求的网页。
     *code=4010001：（授权异常） 请求要求身份验证。 客户端需要跳转到登录页面重新登录
     *code=4010002：(凭证过期) 客户端请求刷新凭证接口
     *code=4030001：没有权限禁止访问
     *code=400xxxx：系统主动抛出的业务异常
     *code=5000001：系统异常
     *
     */

    SUCCESS(200,"操作成功");



    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误消息
     */
    private final String msg;

    BaseResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }

}
