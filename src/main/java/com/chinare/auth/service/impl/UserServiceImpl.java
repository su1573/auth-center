package com.chinare.auth.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import com.chinare.auth.dao.UserDao;
import com.chinare.auth.domain.ApplDO;
import com.chinare.auth.domain.DeptDO;
import com.chinare.auth.domain.MenuDO;
import com.chinare.auth.domain.RoleDO;
import com.chinare.auth.domain.UserDO;
import com.chinare.auth.filter.LadpManager;
import com.chinare.auth.filter.LdapUserAttributeMapper;
import com.chinare.auth.service.UserService;
import com.chinare.auth.utils.MD5Utils;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserDao userDao;

	@Autowired
	LdapTemplate ldapTemplate;
	
	private String URL = "ldap://172.16.5.53:389/";
    private String BASEDN = "DC=chinare,DC=local";
	private String FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	private Hashtable<String, String> env=null;
	private LdapContext ctx = null;
	private Control[] connCtls = null;
	
	
	@Override
	public List<UserDO> list(Map<String, Object> map) {
		String deptLevle=userDao.getDeptLevel(map);
		map.put("deptLevel", deptLevle);
		return userDao.list(map);
	}
	
	
	@Override
	public List<DeptDO> listSubDeptByDeptId(Map<String, Object> map) {
		String deptLevle=userDao.getDeptLevel(map);
		map.put("deptLevel", deptLevle);
		return userDao.listSubDeptByDeptId(map);
	}
	
	@Override
	public UserDO findDeptByUser(Map<String, Object> map) {
		UserDO deptLevle=userDao.findDeptByUser(map);
		return deptLevle;
		
	}
	
	@Override
	public UserDO getUserByUserName(Map<String, Object> params) {
		return userDao.getUserByUserName(params);
	}
	
	@Override
	public  List<RoleDO> getUserRoleListById(Map<String, Object> params) {
		return userDao.getUserRoleListById(params);
	}

	@Override
	public String getJWTtoken() {
		return null;
	}

	@Override
	public List<MenuDO> getMenuList(Map<String, Object> params) {
		return userDao.getMenuList(params);
	}

	@Override
	public List<String> getUserPermsOfAppl(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return userDao.getUserPermsOfAppl(params);
	}

	@Override
	public void addUserLadp(Map<String, Object> params) {
		
		 userDao.addUserLadp(params);
	}

	@Override
	public List<UserDO> listUserByRoleName(Map<String, Object> params) {
		return userDao.listUserByRoleName(params);
	}

	@Override
	// Ladp身份验证
	public boolean AuthLadp(String username, String password) {
		env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
		env.put(Context.PROVIDER_URL, URL + BASEDN);// LDAP server
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "devops-jira@chinare.local");
		env.put(Context.SECURITY_CREDENTIALS, "abcd@123");
		try {
			connCtls = new Control[] { new LadpManager() };
			ctx = new InitialLdapContext(env, connCtls);
			System.out.println("认证成功");

		} catch (Exception e) {
			System.out.println("LADP-认证失败");
		}

		String userDN = username + "@chinare.local";

		try {
			ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, userDN);
			ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
			ctx.reconnect(connCtls);
			System.out.println("授权成功");

			return true;

		} catch (Exception e) {
			return false;

		}
	}
/*
	@SuppressWarnings("unchecked")
	@Override
	public UserDO saveUserLadp(String sAMAccountName, String password) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		List<UserDO> list = new ArrayList<UserDO>();
		// 查询过滤条件
		AndFilter andFilter = new AndFilter();
		andFilter.and(new EqualsFilter("objectCategory", "person"));

		andFilter.and(new EqualsFilter("sAMAccountName", sAMAccountName));

		list = ldapTemplate.search("", andFilter.encode(),
				new LdapUserAttributeMapper());

		UserDO user = list.get(0);
		map.put("sAMAccountName", user.getsAMAccountName());
		map.put("displayName", user.getDisplayName());
		map.put("password", MD5Utils.encrypt(sAMAccountName, password));
		map.put("deptId", 12);// 默认部门
		map.put("mail", user.getMail());
		map.put("status", 1);// 默认状态

		Map<String, Object> params = new HashMap<>(16);
		params.put("username", sAMAccountName);

		// 授权成功，先去看看本地是否已经进行同步过
		UserDO user2 = userDao.getUserByUserName(params);

		// 授权成功，但是本数据库没有，则同步
		if (user2 == null) {
			userDao.addUserLadp(map);
			// 同步完，再查询，进行返回
			user2 = userDao.getUserByUserName(params);
		}

		return user2;
	}
	*/
	@SuppressWarnings("unchecked")
	@Override
	public UserDO saveUserLadp(String sAMAccountName, String password) {
		

		Map<String, Object> params = new HashMap<>(16);
		params.put("username", sAMAccountName);

		// 授权成功，先去看看本地是否已经进行同步过
		UserDO user2 = userDao.getUserByUserName(params);

		// 授权成功，但是本数据库没有，则同步
		if (user2 == null) {
			
			HashMap<String, Object> map = new HashMap<String, Object>();

			List<UserDO> list = new ArrayList<UserDO>();
			// 查询过滤条件
			AndFilter andFilter = new AndFilter();
			andFilter.and(new EqualsFilter("objectCategory", "person"));

			andFilter.and(new EqualsFilter("sAMAccountName", sAMAccountName));

			list = ldapTemplate.search("", andFilter.encode(),
					new LdapUserAttributeMapper());

			UserDO user = list.get(0);
			map.put("sAMAccountName", user.getsAMAccountName());
			map.put("displayName", user.getDisplayName());
			map.put("password", MD5Utils.encrypt(sAMAccountName, password));
			map.put("deptId", 12);// 默认部门
			map.put("mail", user.getMail());
			map.put("status", 1);// 默认状态
			userDao.addUserLadp(map);
			// 同步完，再查询，进行返回
			user2 = userDao.getUserByUserName(params);
		}

		return user2;
	}
	
	
	public List<ApplDO> getUserApp(Long userId) {
		return  userDao.getUserApp(userId);
		
	}
	
	public List<ApplDO> getUserAuthApp(Long userId) {
		return  userDao.getUserAuthApp(userId);
		
	}


	@Override
	public List<UserDO> getUserByServer(Map<String, Object> params) {
		 return userDao.getUserByServer(params);
	}


	@Override
	public List<UserDO> listBossByDeptId(Map<String, Object> params) {
		return userDao.listBossByDeptId(params);
	}


	@Override
	public List<UserDO> getUserListByUserNameList(List<String> params) {
		
		return userDao.getUserListByUserNameList(params);
	}
	
}
