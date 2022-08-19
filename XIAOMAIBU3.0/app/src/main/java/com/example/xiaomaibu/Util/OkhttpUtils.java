package com.example.xiaomaibu.Util;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.xiaomaibu.Dao.TemploginDao;
import com.example.xiaomaibu.Main2Activity;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class OkhttpUtils {
    public static final String IP="http://192.168.56.1:8080";
    //public static final String imgIP="http://192.168.137.1:8080/img/";
    public static final String imgIP="http://192.168.56.1:8080/img/";
    public static final OkHttpClient client=new OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5,TimeUnit.SECONDS)
            .connectTimeout(5,TimeUnit.SECONDS)
            .addNetworkInterceptor(new LoginInterceptor())
            .build();
    public static final MediaType JsonType = MediaType.parse("application/json; charset=utf-8");
    private static Context mContext;

    /**
     *okHttp post返回request对象
     * @param url
     * @param body
     * @return
     */
    private static Request getRequest(String url, RequestBody body,Context context){
        mContext=context;
        GetSQLite getSQLite=new GetSQLite();
        String token = getSQLite.getToken(mContext);
        return new  Request.Builder()
                .url(url)
                .addHeader("Authorization",token)
                .post(body)
                .build();

    }
    /**
     * okHttp get方法的url拼接参数,并返回request对象
     * @param url

     * @return
     */
    private static Request getRequest(String url, Context context) throws UnsupportedEncodingException {
        mContext = context;
        GetSQLite getSQLite=new GetSQLite();
        String token = getSQLite.getToken(mContext);
        return new Request.Builder()
                .url(url)
                .addHeader("Authorization",token)
                .get()
                .build();
    }

    /*
   JSON同步POST请求
   * */
    public static String httpJsonPost(String url, String json,Context context) throws IOException {
        RequestBody body=RequestBody.create(JsonType,json);
        Response response=client.newCall(getRequest(url,body,context)).execute();
        return response.body().string();
    }
    /*
     * 同步GET请求
     * */
    public static String httpGet(String url,Context context) throws IOException{
        Response response= client.newCall(getRequest(url,context)).execute();
        return response.body().string();
    }
    /*
     * 异步GET请求
     *
     * */
    public static void httpGet(String url,Context context,Callback callback)throws IOException{
        client.newCall(getRequest(url,context))
                .enqueue(callback);
    }
    /**
     * okHttp post异步请求(json方式提交)
     * @param url
     * @param json
     * @param callback
     * @return
     * @throws IOException
     */
    public static void httpJsonPost(String url, String json,Context context, Callback callback) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        client.newCall(getRequest(url, body,context))
                .enqueue(callback);
    }

    /**
     * 判断登录拦截器
     */
    private static class LoginInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request =chain.request();
            Log.e("LoginInterceptor","被调用了");
            Response response=chain.proceed(request);

            ResponseBody responseBody=response.body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // request the entire body.
            Buffer buffer = source.buffer();
            // clone buffer before reading from it
            String responseBodyString = buffer.clone().readString(Charset.forName("UTF-8"));
            JSONObject jsonObject= JSON.parseObject(responseBodyString);
            String status = jsonObject.getString("code");
            if ("10000".equals(status)){
                TemploginDao temploginDao= new TemploginDao();
                temploginDao.delete(mContext);
            }
            Log.e("LoginInterceptor",responseBodyString);
            return response;
        }
    }
    /**
     * 获取data 方法
     */
    public static JSONArray getData(String body){
        JSONObject jsonObject = JSON.parseObject(body);
        JSONArray jsonArray= jsonObject.getJSONArray("data");
        return jsonArray;
    }
    /**
     * 获取code方法
     */
    public static String getCode(String body){
        JSONObject jsonObject = JSON.parseObject(body);
        String code= jsonObject.getString("code");
        return code;
    }
}
