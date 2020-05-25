/**
 * @Title: BizValidationException.java
 * @Package com.chinare.eidi.common.exception
 * @Description: 
 * Copyright: Copyright (c) 2018
 * Website: www.panzhijie.cn
 * 
 * @Author songsn
 * @DateTime 2019年8月7日 上午9:51:22
 * @version V1.0
 */

package com.chinare.auth.exception;

import javax.validation.ValidationException;

/**
 * @ClassName: BizValidationException
 * @Description: 
 * @Author songsn
 * @DateTime 2019年8月7日 上午9:51:22 
 */


public class BizValidationException extends ValidationException{
	
	/**
	  * @Fields serialVersionUID : 
	  */
	
	private static final long serialVersionUID = 1L;
	private int code ;
	private String description ;



	public BizValidationException(ErrorCode errorCode, String description) {
		
		super( errorCode.getMessage() );
		this.code=errorCode.getCode();
		this.description=description;
	}

	public BizValidationException() {
		super();
	}

	public BizValidationException(int code ,String message, Throwable cause) {
		super( message, cause );
		this.code=code;
	}

	public BizValidationException(int code ,Throwable cause) {
		super( cause );
		this.code=code;
	}

	/**
	 * getter method
	 * @return the code
	 */
	
	public int getCode() {
		return code;
	}

	/**
	 * setter method
	 * @param code the code to set
	 */
	
	public void setCode(int code) {
		this.code = code;
	}
	
	/**
	 * getter method
	 * @return the description
	 */
	
	public String getDescription() {
		return description;
	}

	/**
	 * setter method
	 * @param description the description to set
	 */
	
	public void setDescription(String description) {
		this.description = description;
	}
}
