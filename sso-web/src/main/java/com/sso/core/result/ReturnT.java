package com.sso.core.result;

import java.io.Serializable;

/**
 * 返回值对象
 * @author suning 2020-12-6 15:45:31
 * @param <T>
 */
public class ReturnT<T> implements Serializable {

	private static final long serialVersionUID = 3259275813768090918L;

	public static final int SUCCESS_CODE = 200;
	public static final int FAIL_CODE = 500;
	public static final ReturnT<String> SUCCESS = new ReturnT<String>(null);
	public static final ReturnT<String> FAIL = new ReturnT<String>(FAIL_CODE, null);

	private int code;
	private String msg;
	private T data;
	
	public ReturnT(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public ReturnT(T data) {
		this.code = SUCCESS_CODE;
		this.data = data;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

}
