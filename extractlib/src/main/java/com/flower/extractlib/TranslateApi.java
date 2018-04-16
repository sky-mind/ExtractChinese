package com.flower.extractlib;

/**
 * Created by nicolee on 2016/9/29.
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author quanmin
 */
public class TranslateApi {
    private static Map<String, String> stringMap;
    private static String stringFile = "D:\\use\\work\\testStringExtract\\String.properties";
    public static String outFile = "D:\\use\\work\\testStringExtract\\en_String.properties";
    public static final String FROM = "zh";
    public static final String TO = "en";
    public static final String URL = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    public static final String APPID = "20160929000029527";
    public static final String KEY = "fzh1tMDAtHq6l7OfgOPn";

    public static void main(String[] args) {
        try {
            InputStream in = new FileInputStream(stringFile);
            PrintWriter output = new PrintWriter(new FileWriter(new File(outFile)));
            stringMap = DomParser.parse(in);
            for (String key : stringMap.keySet()) {
                get(key, output);
            }
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeTranslateRes(String key ,String name, PrintWriter output){
        String [] arrs = name.split(" ");
        if( arrs != null && arrs.length> 0){
            StringBuffer sb = new StringBuffer("");
            for(int i = 0; i< arrs.length; i++){
                String s = arrs[i];
                if(arrs.length > 3 && s.length() > 20){
                    s = s.substring(0, 20);
                }
                if(sb.toString().length() > 40){
                    break;
                }
                s = cleanStr(s);
                if(i > 0 && s != null && s.length() > 0){
                    sb.append("_");
                    sb.append(s);
                }else {
                    sb.append(s);
                }
            }
            name = sb.toString();
        }
        if(name == null || name.length() == 0){
            name = "please_give_name";
        }
        String preFix = "<string name=\"" + name + "\">";
        String endFix = "</string>";
        String res = preFix + key + endFix + "\n";
        System.out.println(preFix + key + endFix);
        try {
            output.write(new String(res.getBytes(),Config.charset));
        }catch (Exception e){
            e.printStackTrace();
        }
        output.flush();
    }



    public static String get(String query, PrintWriter output) {
        CloseableHttpClient hc = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet();
        CloseableHttpResponse response;
        try {
            String q = URLEncoder.encode(query, "UTF-8");
            String from = FROM;
            String to = TO;
            String salt = getRandom();

            //appid+q+salt+密钥
            String sign = toMD5(TranslateApi.APPID + query + salt + TranslateApi.KEY);
            httpGet = new HttpGet(TranslateApi.URL
                    + "?q=" + q
                    + "&from=" + from
                    + "&to=" + to
                    + "&appid=" + TranslateApi.APPID
                    + "&salt=" + salt
                    + "&sign=" + sign);
            response = hc.execute(httpGet);
            JSONObject obj = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            JSONArray tranArr = obj.getJSONArray("trans_result");
            String res = tranArr.getJSONObject(0).getString("dst");
            System.out.println(res);
            writeTranslateRes(query, res, output);
            return res;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpGet.releaseConnection();
        }
        return null;
    }

    private static String getRandom() {
        StringBuilder str = new StringBuilder();//定义变长字符串
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }


    public static void post(String query) {

        CloseableHttpClient hc = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(TranslateApi.URL);

        CloseableHttpResponse response;
        try {
            String salt = getRandom();
            //appid+q+salt+密钥
            String sign = toMD5(TranslateApi.APPID + query + salt + TranslateApi.KEY);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("q", query));
            params.add(new BasicNameValuePair("from", FROM));
            params.add(new BasicNameValuePair("to", TO));
            params.add(new BasicNameValuePair("appid", TranslateApi.APPID));
            params.add(new BasicNameValuePair("salt", salt));
            params.add(new BasicNameValuePair("sign", sign));

            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            response = hc.execute(httpPost);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
        }
    }

    public static String toMD5(String plainText) {
        try {
            //生成实现指定摘要算法的 MessageDigest 对象。
            MessageDigest md = MessageDigest.getInstance("MD5");
            //使用指定的字节数组更新摘要。
            md.update(plainText.getBytes());
            //通过执行诸如填充之类的最终操作完成哈希计算。
            byte b[] = md.digest();
            //生成具体的md5密码到buf数组
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
//            System.out.println("32位: " + buf.toString());// 32位的加密
//            System.out.println("16位: " + buf.toString().substring(8, 24));// 16位的加密，其实就是32位加密后的截取String
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String cleanStr(String str){
        // 清除掉所有特殊字符
        String regEx="[`~!@#$%^&*()-+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim().toLowerCase();
    }
}

