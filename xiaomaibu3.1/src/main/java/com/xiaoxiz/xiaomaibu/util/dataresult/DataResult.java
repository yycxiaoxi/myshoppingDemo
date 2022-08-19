package com.xiaoxiz.xiaomaibu.util.dataresult;


public class DataResult<T>{
    /**
     * 请求响应code，0为成功 其他为失败
     */
    private int code = 0;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 响应异常码详细信息
     */
    private String msg;
    /**
     * 响应内容 ， code 0 时为 返回 数据
     */
    private T data;

    public DataResult(int code, T data) {
        this.code = code;
        this.data = data;
        this.msg=null;
    }

    public DataResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public DataResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data=null;
    }
    public DataResult() {
        this.code=BaseResponseCode.SUCCESS.getCode();
        this.msg=BaseResponseCode.SUCCESS.getMsg();
        this.data=null;
    }

    public DataResult(T data) {
        this.data = data;
        this.code=BaseResponseCode.SUCCESS.getCode();
        this.msg=BaseResponseCode.SUCCESS.getMsg();
    }

    public DataResult(ResponseCodeInterface responseCodeInterface) {
        this.data = null;
        this.code = responseCodeInterface.getCode();
        this.msg = responseCodeInterface.getMsg();
    }

    public DataResult(ResponseCodeInterface responseCodeInterface, T data) {
        this.data = data;
        this.code = responseCodeInterface.getCode();
        this.msg = responseCodeInterface.getMsg();
    }
    /**
     * 操作成功 data为null
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param
     * @return       com.xh.lesson.utils.DataResult<T>
     * @throws
     */
    public static DataResult success(){
        return new DataResult();
    }
    /**
     * 操作成功 data 不为null
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param data
     * @return       com.xh.lesson.utils.DataResult<T>
     * @throws
     */
    public static <T> DataResult success(T data){
        return new DataResult(data);
    }
    /**
     * 自定义 返回操作 data 可控
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param code
     * @param msg
     * @param data
     * @return       com.xh.lesson.utils.DataResult
     * @throws
     */
    public static <T> DataResult getResult(int code, String msg, T data){
        return new DataResult(code,msg,data);
    }
    /**
     *  自定义返回  data为null
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param code
     * @param msg
     * @return       com.xh.lesson.utils.DataResult
     * @throws
     */
    public static DataResult getResult(int code, String msg){
        return new DataResult(code,msg);
    }
    /**
     * 自定义返回 入参一般是异常code枚举 data为空
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param responseCode
     * @return       com.xh.lesson.utils.DataResult
     * @throws
     */
    public static DataResult getResult(BaseResponseCode responseCode){
        return new DataResult(responseCode);
    }
    /**
     * 自定义返回 入参一般是异常code枚举 data 可控
     * @Author:      小霍
     * @UpdateUser:
     * @Version:     0.0.1
     * @param responseCode
     * @param data
     * @return       com.xh.lesson.utils.DataResult
     * @throws
     */
    public static <T> DataResult getResult(BaseResponseCode responseCode, T data){

        return new DataResult(responseCode,data);
    }

    public static <T> DataResult ERROR (){
        return new DataResult(500,"error");
    }
    public static <T> DataResult ERROR (String msg){
        return new DataResult(500,msg);
    }
    public static <T> DataResult ERROR (int code, String msg){
        return new DataResult(code,msg);
    }


}
