package com.wxq.web;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

@SuppressWarnings("deprecation")
public class WebServicePost {

    private static String IP = "192.168.199.201:8080";

    //Post方法传递信息
    public static String executeHttpPost(String username, String password) {
        try {
            String path = "http://" + IP + "/WebTools/UserLogin";
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("password", password);

            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 发送post消息
    private static String sendPOSTRequest(String path, Map<String, String> params, String encoding) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, encoding);
        HttpPost post = new HttpPost(path);
        post.setEntity(entity);
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
        	System.out.println("�ɹ���ȡ��Ϣ...");
            return getInfo(response);
        }
        System.out.println("��ȡ��Ϣʧ��...");
        return null;
    }

    private static String getInfo(HttpResponse response) throws Exception {
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        byte[] data = WebService.read(is);
        return new String(data, "UTF-8");
    }
}
 