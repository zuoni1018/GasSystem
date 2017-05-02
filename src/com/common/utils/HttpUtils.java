package com.common.utils;

import com.common.mInterface.HttpUtilsListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by zangyi_shuai_ge on 2017/4/21
 * 网络访问封装
 *  compile 'com.zhy:okhttputils:2.6.2'
 */

public class HttpUtils {


    /**
     * 传入键值对的方法
     * 1、访问链接 2、请求参数
     * 3、是否需要签名 4、监听回调
     */
    public HttpUtils(String pUrl, Map<String, String> pMap, boolean needSign, final HttpUtilsListener pListener) {

        //生成签名
        if (needSign) {
            String sign = "";
            List<String> attributes = new ArrayList<>();
            //生成签名sign
            for (String obj : pMap.keySet()) {
                String value = pMap.get(obj);
                attributes.add(obj + "=" + value);
            }
            try {
                sign = sign(attributes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //向map里传入签名
            pMap.put("sign", sign);
        }

        //传入Url
        PostFormBuilder mBuilder = OkHttpUtils.post().url(pUrl);
        //传入键值对
        for (String obj : pMap.keySet()) {
            String value = pMap.get(obj);
            mBuilder.addParams(obj, value);
        }
        //设置监听回调
        mBuilder.build().execute(
                new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        pListener.OnError(call, e, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        pListener.OnResponse(response, id);
                    }
                });
    }


//    public interface OkUtilListener {
//
//        void mOnError(okhttp3.Call call, Exception e, int id);
//
//        void mOnResponse(String response, int id);
//    }


    /***
     * 生成 签名
     * @throws UnsupportedEncodingException
     */
    public static String sign(List<String> attributes) throws UnsupportedEncodingException {
        String key = "11";
        Object[] os = attributes.toArray();
        Arrays.sort(os);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < os.length; i++) {
            if (i > 0) {
                stringBuffer.append("&");
            }
            stringBuffer.append(os[i]);
        }
        //key 为约定字
        stringBuffer.append(key);
        String content = stringBuffer.toString();
        String sign = "";
        return sign;
    }

    //加密
    private static String md5LowerCase(String src) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(src.getBytes("utf-8"));
        byte b[] = md.digest();
        int i;
        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();// 32位的加密
    }

}
