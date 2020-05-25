package com.chinare.auth.domain;

import java.sql.Timestamp;
import java.util.List;

public class RoleDO {
	
	private Long roleId;
	private Long applId;
	private String applName;
	private String roleName;
	private int roleSign;
	private String remark;
	private Long userIdCreate;
	private Timestamp gmtCreate;
	private Timestamp gmtModified;
	private List<Long> menuIds;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	public Long getApplId() {
		return applId;
	}

	public void setApplId(Long applId) {
		this.applId = applId;
	}
	
	public String getApplName() {
		return applName;
	}

	public void setApplName(String applName) {
		this.applName = applName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getRoleSign() {
		return roleSign;
	}

	public void setRoleSign(int roleSign) {
		this.roleSign = roleSign;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getUserIdCreate() {
		return userIdCreate;
	}

	public void setUserIdCreate(Long userIdCreate) {
		this.userIdCreate = userIdCreate;
	}

	public Timestamp getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Timestamp gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Timestamp getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Timestamp gmtModified) {
		this.gmtModified = gmtModified;
	}

	public List<Long> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(List<Long> menuIds) {
		this.menuIds = menuIds;
	}

	@Override
	public String toString() {
		return "RoleDO{" +
				" roleId=" + roleId +
				", applId='" + applId + '\'' +
				", applName='" + applName + '\'' +
				", roleName='" + roleName + '\'' +
				", roleSign=" + roleSign +
				", remark='" + remark + '\'' +
				", userIdCreate=" + userIdCreate +
				", gmtCreate=" + gmtCreate +
				", gmtModified=" + gmtModified +
				", menuIds=" + menuIds +
				'}';
	}
}
