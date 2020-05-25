package com.chinare.auth.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.chinare.auth.domain.ApplDO;
import com.chinare.auth.domain.DeptDO;
import com.chinare.auth.domain.MenuDO;
import com.chinare.auth.domain.RoleDO;
import com.chinare.auth.domain.UserDO;

/**
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 09:45:11
 */
@Mapper
public interface UserDao {

	UserDO findDeptByUser(Map<String,Object> params);
	
	UserDO getUserByUserName(Map<String,Object> params);
	
	String getDeptLevel(Map<String,Object> params);
	
	List<UserDO> list(Map<String, Object> map);
	
	List<DeptDO> listSubDeptByDeptId(Map<String, Object> map);
	
	List<RoleDO> getUserRoleListById(Map<String,Object> params);

	List<MenuDO> getMenuList(Map<String, Object> params);

	List<String> getUserPermsOfAppl(Map<String, Object> params);

	List<UserDO> listUserByRoleName(Map<String, Object> params);
	
	List<ApplDO> getUserApp(Long userId);
	
	List<ApplDO> getUserAuthApp(Long userId);
	
	void addUserLadp(Map<String, Object> params);

	List<UserDO> getUserByServer(Map<String, Object> params);

	List<UserDO> listBossByDeptId(Map<String, Object> params);

	List<UserDO> getUserListByUserNameList(List<String> params);
	
}
