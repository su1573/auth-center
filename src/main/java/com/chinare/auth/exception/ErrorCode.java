/**
 * @Title: ErrorCode.java
 * @Package com.chinare.eidi.common.utils
 * @Description: 
 * Copyright: Copyright (c) 2018
 * Website: www.panzhijie.cn
 * 
 * @Author songsn
 * @DateTime 2019年8月7日 上午10:58:41
 * @version V1.0
 */

package com.chinare.auth.exception;

/**
 * @ClassName: ErrorCodeMsg
 * @Description: 错误代码
 * @Author songsn
 * @DateTime 2019年8月7日 上午10:58:41 
 */

public enum ErrorCode {

	NOT_FOUND(110001,"请求的资源不存在或url错误，或httpRequest解析异常。"),
	PARAMETER_MISSING(110002,"参数错误，必传参数不能为空。"),
	FORMAT_ERROR(110003,"参数错误，参数格式有误。"),
	FORBIDDEN(120001,"权限不足，无权访问。"),
	NETWORK_ERROR_CLIENT(130001,"网络错误异常，客户端。"),
	SQL_RUNTIME(210001,"SQL执行异常。"),
	SQL_CONNECTION(210002,"数据库连接异常。"),
	FILE_SIZE(220001,"文件大小超出限制。"),
	FILE_RW(220002,"读写异常。"),
	NETWORK_ERROR_SERVER(230001,"网络错误异常,服务端。"),
	UNKNOW(900000,"其他异常。"),
	;
	


	private int code;
	private String message;
	
	ErrorCode(int code , String message){
		this.code = code;
		this.message = message;
	}
	
	/**
	 * getter method
	 * @return the code
	 */
	
	public int getCode() {
		return code;
	}

	/**
	 * getter method
	 * @return the message
	 */
	
	public String getMessage() {
		return message;
	}
}
