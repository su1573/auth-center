package com.chinare.auth.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chinare.auth.domain.ApplDO;
import com.chinare.auth.domain.DeptDO;
import com.chinare.auth.domain.MenuDO;
import com.chinare.auth.domain.RoleDO;
import com.chinare.auth.domain.UserDO;

@Service
public interface UserService {
	
	UserDO getUserByUserName(Map<String, Object> params);
	
	List<UserDO> list(Map<String, Object> map);
	
	List<DeptDO> listSubDeptByDeptId(Map<String, Object> map);
	
	UserDO findDeptByUser(Map<String, Object> map);
	
	List<RoleDO> getUserRoleListById(Map<String, Object> params);
	
	void addUserLadp(Map<String, Object> params);
	
	String getJWTtoken();
	
	List<MenuDO> getMenuList(Map<String, Object> params);

	List<String> getUserPermsOfAppl(Map<String, Object> params);

	List<UserDO> listUserByRoleName(Map<String, Object> params);
	 
	boolean AuthLadp(String username, String password);
	
	UserDO saveUserLadp(String sAMAccountName, String password);
	
	List<ApplDO> getUserApp(Long userId);
	
	List<ApplDO> getUserAuthApp(Long userId);

	List<UserDO> getUserByServer(Map<String, Object> params);

	List<UserDO> listBossByDeptId(Map<String, Object> params);

	List<UserDO> getUserListByUserNameList(List<String> params);
	
}
