package com.test.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.test.server.api.LoginApi;

/**
 * �����߳�
 * @author ude
 *
 */
public class ServerThread extends Thread{
	Socket socket;
	public ServerThread(Socket socket){
		this.socket = socket;
	}
	public void run() {
		String log = "---------start-----------\n";//���������
		BufferedReader bd;
		try {
			bd = new BufferedReader(new
					InputStreamReader(socket.getInputStream()));
			/**

			 * ����HTTP����

			 */

			String requestHeader;

			int contentLength=0;
			log = log + "GET:\n";
			while
				((requestHeader=bd.readLine())!=null
				&&!requestHeader.isEmpty()){
				log = log + requestHeader+"\n";

				/**

				 * ���GET����

				 */

				if(requestHeader.startsWith("GET")){
					int begin = requestHeader.indexOf("?")+1;
					String condition = "";
					if (begin != 0){
						int end = requestHeader.indexOf("HTTP/");
						condition =requestHeader.substring(begin, end);//����������,��&�ָ�
					}
					if(requestHeader.substring(4).startsWith("/Login")){
						log = log + "��¼�ӿ�:"+condition+"\n";
						LoginApi loginApi = new LoginApi(condition,socket);
						loginApi.handler();
						log = log + loginApi.getLog();
					}
				}

				/**

				 * ���POST����
				 * 1.��ȡ�������ݳ���

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

			//���ͻ�ִ

			if(!socket.isClosed()){
				ServerResult.result(socket,0,"�޴˽ӿڷ���", "");
			}
		} catch (IOException e) {
			log = log + "err:"+e.getMessage();
		}
		log = log + "---------end-----------\n";
		System.out.println(log);
	}
}
