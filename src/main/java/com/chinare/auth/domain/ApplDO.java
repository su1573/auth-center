package com.chinare.auth.domain;


import java.io.Serializable;

public class ApplDO implements Serializable {
    private static final long serialVersionUID = 1L;
    //
    
	private Long userId;
	private String appEglName;
	private String appChnName;
	private String baseUrl;
	private String email;
	private String contact;
	private String tel;
	private String applicationDesc;
	private String dataUrl;
	
    
	
	
    public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

	public String getApplicationDesc() {
		return applicationDesc;
	}

	public void setApplicationDesc(String applicationDesc) {
		this.applicationDesc = applicationDesc;
	}

	public String getAppEglName() {
		return appEglName;
	}

	public void setAppEglName(String appEglName) {
		this.appEglName = appEglName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}


	public String getAppChnName() {
		return appChnName;
	}

	public void setAppChnName(String appChnName) {
		this.appChnName = appChnName;
	}

	

	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

  
}
