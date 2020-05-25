package com.chinare.auth.domain;

public class DeptDO {
	
	private int deptId;
	private int parentId;
	private String deptLevel;
	private String name;
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getDeptLevel() {
		return deptLevel;
	}
	public void setDeptLevel(String deptLevel) {
		this.deptLevel = deptLevel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	

}
