package com.test.server.data;

public class Login {
	private String userName;
	private String passWord;
	private String token;
	
	public Login(String userName,String passWord){
		this.setUserName(userName);
		this.setPassWord(passWord);
	}

	public Login(String token){
		this.setToken(token);
	}
	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
	
}
