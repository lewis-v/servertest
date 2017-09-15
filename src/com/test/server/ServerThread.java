package com.test.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.test.server.api.LoginApi;

/**
 * 处理线程
 * @author ude
 *
 */
public class ServerThread extends Thread{
	Socket socket;
	public ServerThread(Socket socket){
		this.socket = socket;
	}
	public void run() {
		String log = "---------start-----------\n";//传输的数据
		BufferedReader bd;
		try {
			bd = new BufferedReader(new
					InputStreamReader(socket.getInputStream()));
			/**

			 * 接受HTTP请求

			 */

			String requestHeader;

			int contentLength=0;
			log = log + "GET:\n";
			while
				((requestHeader=bd.readLine())!=null
				&&!requestHeader.isEmpty()){
				log = log + requestHeader+"\n";

				/**

				 * 获得GET参数

				 */

				if(requestHeader.startsWith("GET")){
					int begin = requestHeader.indexOf("?")+1;
					String condition = "";
					if (begin != 0){
						int end = requestHeader.indexOf("HTTP/");
						condition =requestHeader.substring(begin, end);//传来的数据,以&分隔
					}
					if(requestHeader.substring(4).startsWith("/Login")){
						log = log + "登录接口:"+condition+"\n";
						LoginApi loginApi = new LoginApi(condition,socket);
						loginApi.handler();
						log = log + loginApi.getLog();
					}
				}

				/**

				 * 获得POST参数
				 * 1.获取请求内容长度

				 */

				if
				(requestHeader.startsWith("Content-Length")){

					int begin=requestHeader.indexOf("Content-Lengh:")+"Content-Length:".length()+1;
					String postParamterLength =requestHeader.substring(begin).trim();
					contentLength =Integer.parseInt(postParamterLength);
				}
			}
			StringBuffer sb =new StringBuffer();

			if(contentLength>0){
				for(int i = 0; i < contentLength; i++) {
					sb.append((char)bd.read());
				}
			}

			//发送回执

			if(!socket.isClosed()){
				ServerResult.result(socket,0,"无此接口访问", "");
			}
		} catch (IOException e) {
			log = log + "err:"+e.getMessage();
		}
		log = log + "---------end-----------\n";
		System.out.println(log);
	}
}
