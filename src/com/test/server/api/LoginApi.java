package com.test.server.api;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import com.test.server.ServerResult;
import com.test.server.sql.SqlConnection;

import net.sf.json.JSONObject;

public class LoginApi {
	private String log = "";//log消息
	private Socket socket;
	private JSONObject js = new JSONObject();;
	
	public LoginApi(){}
	
	public LoginApi(String data,Socket socket){
		this.socket = socket;
		String [] str = data.split("&");
		for (String string : str){
			String [] strArr = string.split("=");
			if(strArr.length == 2){
				js.put(strArr[0].trim(), strArr[1].trim());
			}
			
		}
		log = log + "json数据:"+js.toString()+"\n";
	}
	
	public void handler(){
		SqlConnection sql = new SqlConnection(); 
		sql.TheSqlConnection();
		if (!js.containsKey("token")){//token是否为空
			if(!js.containsKey("username") || !js.containsKey("password")){//账号密码是否为空,为空时返回错误提示
				try {
					ServerResult.result(socket, -1, "请输入账号密码", "");
				} catch (IOException e) {
					log = log + e.getMessage() +"\n";
				}
			}else{
				List<JSONObject> jslist = sql.search("password", "username='"+js.getString("username")+"'", "user");
				if(jslist.size() ==0){//数据库中无此数据
					try {
						ServerResult.result(socket, -1, "账号不存在", "");
					} catch (IOException e) {
						log = log + e.getMessage() +"\n";
					}
				}else{
					if(jslist.get(0).getString("password").equals(js.getString("password"))){//密码正确
						try {
							ServerResult.result(socket, 1, "登录成功", "");
						} catch (IOException e) {
							log = log + e.getMessage() +"\n";
						}
					}else{//密码不正确
						try {
							ServerResult.result(socket, -1, "密码错误", "");
						} catch (IOException e) {
							log = log + e.getMessage() +"\n";
						}
					}
				}
			}
		
		}else{
			List<JSONObject> jslist = sql.search("token", "", "user");
			if (jslist.size() != 0){//token存在,可以登录
				try {
					ServerResult.result(socket, 1, "登录成功", "");
				} catch (IOException e) {
					log = log + e.getMessage() +"\n";
				}
			}else{//token不存在或异常
				try {
					ServerResult.result(socket, -1, "登录失败", "");
				} catch (IOException e) {
					log = log + e.getMessage() +"\n";
				}
			}
		}
		log = log + sql.sqlClose();
	}
	
	public String getLog(){
		return log;
	}
}
