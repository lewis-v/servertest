package com.test.server.api;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import com.test.server.ServerResult;
import com.test.server.sql.SqlConnection;

import net.sf.json.JSONObject;

public class LoginApi {
	private String log = "";//log��Ϣ
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
		log = log + "json����:"+js.toString()+"\n";
	}
	
	public void handler(){
		SqlConnection sql = new SqlConnection(); 
		sql.TheSqlConnection();
		if (!js.containsKey("token")){//token�Ƿ�Ϊ��
			if(!js.containsKey("username") || !js.containsKey("password")){//�˺������Ƿ�Ϊ��,Ϊ��ʱ���ش�����ʾ
				try {
					ServerResult.result(socket, -1, "�������˺�����", "");
				} catch (IOException e) {
					log = log + e.getMessage() +"\n";
				}
			}else{
				List<JSONObject> jslist = sql.search("password", "username='"+js.getString("username")+"'", "user");
				if(jslist.size() ==0){//���ݿ����޴�����
					try {
						ServerResult.result(socket, -1, "�˺Ų�����", "");
					} catch (IOException e) {
						log = log + e.getMessage() +"\n";
					}
				}else{
					if(jslist.get(0).getString("password").equals(js.getString("password"))){//������ȷ
						try {
							ServerResult.result(socket, 1, "��¼�ɹ�", "");
						} catch (IOException e) {
							log = log + e.getMessage() +"\n";
						}
					}else{//���벻��ȷ
						try {
							ServerResult.result(socket, -1, "�������", "");
						} catch (IOException e) {
							log = log + e.getMessage() +"\n";
						}
					}
				}
			}
		
		}else{
			List<JSONObject> jslist = sql.search("token", "", "user");
			if (jslist.size() != 0){//token����,���Ե�¼
				try {
					ServerResult.result(socket, 1, "��¼�ɹ�", "");
				} catch (IOException e) {
					log = log + e.getMessage() +"\n";
				}
			}else{//token�����ڻ��쳣
				try {
					ServerResult.result(socket, -1, "��¼ʧ��", "");
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
