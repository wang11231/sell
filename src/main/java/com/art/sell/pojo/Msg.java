package com.art.sell.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

/**
 * 返回信息的实体类
 * @author Administrator
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Msg implements Serializable {

	/**
	 * 状态码 0：表示成功
	 */
	public static final int SUCCESS_CODE=0;
	/**
	 * 状态码 1：表示失败
	 */
	public static final int FAILURE_CODE=1;

	/**
	 * 中间状态码 2：表示部分成功，部分失败，目前适用于批量支付部分成功
	 */
	public static final int MIDDLE_CODE=2;

	/**
	 * 该状态标识session会话过期
	 */
	public static final int SESSSION_TIME_OUT = 2;

	/**
	 * 非法session
	 */
	public static final int ILLEGAL_SESSSION = 3;

	/**
	 * 非法session
	 */
	public static final int OPENID_NULL = 4;

	/**
	 * 授权的不是同一个手机号
	 */
	public static final int MOBILE_CODE = 6;

	/**
	 * 其他
	 */
	public static final int OTHER = 5;

	private int code;
	private String msg;
	private String token;
	private Date date;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	private Object data;

	public Msg() {
	}


	public Msg(int code, String msg){
		this.code = code;
		this.msg = msg;
		this.date = new Date();
	}
	public Msg(int code, String msg, Date date){
		this.code = code;
		this.msg = msg;
		this.date = date;
	}

    public Msg(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.date = new Date();
        this.data = data;
    }
	public Msg(int code, String msg, Date date, Object data){
		this.code = code;
		this.msg = msg;
		this.date = date;
		this.data = data;
	}
	public Msg(int code, String msg, String token, Date date, Object data) {
		this.code = code;
		this.msg = msg;
		this.token = token;
		this.date = date;
		this.data = data;
	}

    public Msg(int code, Object data) {
        this.code = code;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}



	public boolean success(){
		return code == SUCCESS_CODE;
	}

	public boolean failure(){
		return code == FAILURE_CODE;
	}
}
