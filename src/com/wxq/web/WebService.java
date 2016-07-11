package com.wxq.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.wxq.model.ChatItem;
public class WebService {

    private static String IP ="15864239710.imwork.net:32846";// //"localhost:8080";//"192.168.43.95:8080";//"192.168.1.120:8080";////"192.168.199.201:8080";
    private static final String tag="w";
    //登录状态
    public static boolean executeHttpGet(String username, String password) {

        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            String path = "http://" + IP + "/WebTools/UserLogin";
            path = path + "?username=" + username + "&password=" + password;
            conn = (HttpURLConnection) new URL(path).openConnection();       
            conn.setConnectTimeout(10000); // 连接超时
            conn.setReadTimeout(10000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // 传输方法
            conn.setRequestProperty("Charset", "UTF-8"); //设置编码
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                 return parseInfo(is);
            }

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		return false;
    }
    //注册
    public static boolean executeHttpGetRegister(String username, String password) {

        HttpURLConnection conn = null;
        InputStream is = null;

        try {
        	String path = "http://" + IP + "/WebTools/UserRegister";
            path = path + "?username=" + username + "&password=" + password;
            conn = (HttpURLConnection) new URL(path).openConnection();       
            conn.setConnectTimeout(10000); 
            conn.setReadTimeout(10000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); 
            conn.setRequestProperty("Charset", "UTF-8"); 
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                 return parseInfo(is);
            }

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		return false;
    }
    //获取信息
    public static ArrayList<ChatItem> executeHttpGetMsg(String roomId,String username) {
    	   HttpURLConnection conn = null;
    	   ArrayList<ChatItem> chatList = new ArrayList<ChatItem>();
           try {
               String path = "http://" + IP + "/WebTools/GetMsgs";
               path = path + "?roomId=" + roomId +"&username="+ username;
               conn = (HttpURLConnection) new URL(path).openConnection();       
               conn.setConnectTimeout(5000); // 超时
               conn.setReadTimeout(5000);
               conn.setDoInput(true);
               conn.setRequestMethod("GET"); // 传输方法
               if (conn.getResponseCode() == 200) {
               		InputStream in = conn.getInputStream(); 
               		chatList = parseJSON(in);
               }
               return chatList;

           }catch (Exception e) {
               e.printStackTrace();
           } finally {
               //关闭连接
               if (conn != null) {
                   conn.disconnect();
               }     
           }
    	return null;
    }
   

    private static ArrayList<ChatItem> parseJSON(InputStream in)throws Exception{  
    	ArrayList<ChatItem> chatList = new ArrayList<ChatItem>();  
    	ChatItem chatItem = null;
        String str = read2(in);  
        JSONArray array = new JSONArray(str);  
        int length = array.length();
        for(int i =0;i < length;i++){
            JSONObject object = array.getJSONObject(i);  
            chatItem = new ChatItem(object.getInt("id"), object.getString("userName"), object.getString("roomId"), object.getString("content"),object.getBoolean("res")); 
			Log.v(tag, chatItem.getContent());
            chatList.add(chatItem);
        }
        return chatList;
    }  
    //读取字节流
    private static String read2(InputStream in) throws IOException {  
        byte[] data;  
        ByteArrayOutputStream bout = new ByteArrayOutputStream();  
        byte[]buf = new byte[1024];  
        int len = 0;  
        while((len = in.read(buf))!=-1){  
            bout.write(buf, 0, len);  
        }  
        data = bout.toByteArray();  
        return new String(data,"UTF-8");  
    }  
	// 获取字符串
 	private static boolean parseInfo(InputStream inStream) throws Exception {
 		byte[] data = read(inStream);
 		String state = new String(data, "UTF-8");
 		if(state.equals("pass"))
 			return true;
 		else
 			return false;
 	}

 	// 读取字节流
 	public static byte[] read(InputStream inStream) throws Exception {
 		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 		byte[] buffer = new byte[1024];
 		int len = 0;
 		while ((len = inStream.read(buffer)) != -1) {
 			outputStream.write(buffer, 0, len);
 		}
 		inStream.close();
 		return outputStream.toByteArray();
 	}
 	//改变用户状态
	public static boolean executeHttpCreateChange(String userLoginName, String i, String whom) {
		// TODO Auto-generated method stub
		 HttpURLConnection conn = null;
		 InputStream is = null;
         try {
        	 String path = "http://" + IP + "/WebTools/CreateRoom";
             path = path + "?username=" + userLoginName+"&roomId=" + i+"&whom=" + whom;
             conn = (HttpURLConnection) new URL(path).openConnection();       
             conn.setConnectTimeout(10000); 
             conn.setReadTimeout(10000);
             conn.setDoInput(true);
             conn.setRequestMethod("GET"); 
             if (conn.getResponseCode() == 200) {
             		is = conn.getInputStream(); 
             		 return parseInfo(is);
             }
         }catch (Exception e) {
             e.printStackTrace();
         } finally {
             if (conn != null) {
                 conn.disconnect();
             }     
         }
		return false;
	}
	public static boolean executeHttpSendMsg(String user, String roomId,String content) {
		 HttpURLConnection conn = null;
		 InputStream is = null;
		         try {
		             String path = "http://" + IP + "/WebTools/ReiceveMsg";
		             path = path + "?username=" + user+"&roomId=" + roomId +"&content=" + content;
		             conn = (HttpURLConnection) new URL(path).openConnection();       
		             conn.setConnectTimeout(1000); 
		             conn.setReadTimeout(1000);
		             conn.setDoInput(true);
		             conn.setRequestMethod("GET"); 
		             if (conn.getResponseCode() == 200) {
		             		is = conn.getInputStream(); 
		             		 return parseInfo(is);
		             }
		             return true;

		         }catch (Exception e) {
		             e.printStackTrace();
		         } finally {
		             if (conn != null) {
		                 conn.disconnect();
		             }     
		         }
				return false;
	}
 }
