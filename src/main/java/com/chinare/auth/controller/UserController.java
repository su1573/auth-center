package com.chinare.auth.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.chinare.auth.domain.DeptDO;
import com.chinare.auth.domain.MenuDO;
import com.chinare.auth.domain.RoleDO;
import com.chinare.auth.domain.UserDO;
import com.chinare.auth.exception.BizValidationException;
import com.chinare.auth.exception.ErrorCode;
import com.chinare.auth.service.UserService;

@RequestMapping("/sys/user")
@Controller
public class UserController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	UserService userService;

	
	
	/**
	 * 根据deptId查询该部门，以及子部门的人员。
	 * 
	 * @param params
	 * @return
	 */
	
	@GetMapping("/listUserByDeptId")
	@ResponseBody
	List<UserDO> listUserByDeptId(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		logger.info("listUserByDeptId方法执行,参数"+JSON.toJSONString(params));
		List<UserDO> sysUserList = userService.list(params);
		logger.info("listUserByDeptId方法执行完毕：返回："+sysUserList.toString());
		return sysUserList;
	}
	
	
	/**
	 * 根据deptId查询该部门下的子部门。
	 * 
	 * @param params
	 * @return
	 */
	
	@GetMapping("/listSubDeptByDeptId")
	@ResponseBody
	List<DeptDO> listSubDeptByDeptId(@RequestParam Map<String, Object> params) {
		
		if (null == params.get("deptId") || "".equals(params.get("deptId"))) {
			logger.error("请求失败，deptId字段不能为空");
			throw new BizValidationException(ErrorCode.PARAMETER_MISSING, "deptId字段不能为空");

		}
		// 查询列表数据
		logger.info("listSubDeptByDeptId方法执行,参数"+JSON.toJSONString(params));
		List<DeptDO> sysUserList = userService.listSubDeptByDeptId(params);
		logger.info("listSubDeptByDeptId方法执行完毕：返回："+sysUserList.toString());
		return sysUserList;
	}
	
	@PostMapping("/getUserListByUserNameList")
	@ResponseBody
	List<UserDO> getUserListByUserNameList(@RequestBody List<String> params){
		
	List<UserDO> UserList=userService.getUserListByUserNameList(params);
		
		return UserList;
		
	}
	
	
	
	
	@GetMapping("/listBossByDeptId")
	@ResponseBody
	List<UserDO> listBossByDeptId(@RequestParam Map<String,Object> params){
		logger.info("listBossByDeptId方法执行,参数"+JSON.toJSONString(params));
		List<UserDO> sysUserList = userService.listBossByDeptId(params);
		logger.info("listBossByDeptId方法执行完毕：返回："+sysUserList.toString());
		return sysUserList;
	}
	
	/**
	 * 根据员工的id 查询所处的部门
	 * 
	 * @param params
	 * @return
	 * 
	 */
	
	@GetMapping("/findDeptByUser")
	@ResponseBody
	UserDO findDeptByUser(@RequestParam Map<String, Object> params) {
		logger.info("findDeptByUser方法执行,参数"+JSON.toJSONString(params));
		UserDO userdo = userService.findDeptByUser(params);
		logger.info("findDeptByUser方法执行完毕：返回："+userdo.toString());
		return userdo;
	}
	
	
	@GetMapping("/getUserByUserName")
	@ResponseBody
	UserDO getUserByUserName(@RequestParam Map<String, Object> params) {
		logger.info("getUserByUserName方法执行,参数"+JSON.toJSONString(params));
		// 查询列表数据
		UserDO userDo  = userService.getUserByUserName(params);
		logger.info("getUserByUserName方法执行完毕：返回："+userDo.toString());
		return userDo;
	}
	
	/**
	 * 根据传回的参数形式为“xxx_server”,将xxx取到后根据xxx返回user
	 * @param params
	 * @return
	 */
	@GetMapping("/getUserByServer")
	@ResponseBody
	List<UserDO> getUserByServer(@RequestParam Map<String,Object> params) {
		logger.info("getUserByServer方法执行,参数"+JSON.toJSONString(params));
		//查询用户信息
		String str=(String) params.get("app_name");
		String value=str.replace("_server", "");
		params.put("app_name", value);
		List<UserDO> list=userService.getUserByServer(params);
		logger.info("getUserByServer方法执行完毕：返回："+JSON.toJSONString(list));
		return list;
	}
	
	@GetMapping("/getUserRoleListById")
	@ResponseBody
	 List<RoleDO> getUserRoleListById(@RequestParam Map<String, Object> params) {
		logger.info("getUserRoleListById方法执行,参数"+JSON.toJSONString(params));
		// 查询列表数据
		List<RoleDO> role = userService.getUserRoleListById(params);
		logger.info("getUserRoleListById方法执行完毕：返回："+JSON.toJSONString(role));
		return role;
	}
	
	@GetMapping("/getMenuList")
	@ResponseBody
	List<MenuDO> getMenuList(@RequestParam Map<String, Object> params) {
		System.out.println("Feign调用getMenuList！");
		logger.info("getMenuList方法执行,参数"+JSON.toJSONString(params));
		System.out.println("params:"+params);
		if(params.get("appEglName")==null||"".equals(params.get("appEglName"))) {
			logger.info("参数不全！");
			List<MenuDO> menus = new ArrayList<MenuDO>();
			return menus;
		}
		if(params.get("userId")==null||"".equals(params.get("userId"))) {
			logger.info("参数不全！");
			List<MenuDO> menus = new ArrayList<MenuDO>();
			return menus;
		}
		// 查询列表数据
		List<MenuDO> menus = userService.getMenuList(params);
		logger.info("Feign调用getMenuList完毕！");
		logger.info("getMenuList方法执行完毕：返回："+JSON.toJSONString(menus));
		return menus;
	}
	
	@GetMapping("/getUserPermsOfAppl")
	@ResponseBody
	List<String> getUserPermsOfAppl(@RequestParam Map<String, Object> params) {
		System.out.println("Feign调用getUserPermsOfAppl！");
		logger.info("getUserPermsOfAppl方法执行,参数"+JSON.toJSONString(params));
		System.out.println("params:"+params);
		if(params.get("appEglName")==null||"".equals(params.get("appEglName"))) {
			logger.info("参数不全！");
			List<String> perms = new ArrayList<String>();
			return perms;
		}
		if(params.get("userId")==null||"".equals(params.get("userId"))) {
			logger.info("参数不全！");
			List<String> perms = new ArrayList<String>();
			return perms;
		}
		// 查询列表数据
		List<String> perms = userService.getUserPermsOfAppl(params);
		logger.info("Feign调用getUserPermsOfAppl完毕！");
		logger.info("getUserPermsOfAppl方法执行完毕：返回："+JSON.toJSONString(perms));
		return perms;
	}
	
	//新增LADP用户信息
	@GetMapping("/addUserLadp")
	@ResponseBody
	void addUserLadp(@RequestParam Map<String, Object> params) {
		System.out.println("Feign调用添加  Ladp 用户！");
		logger.info("addUserLadp方法执行,参数"+JSON.toJSONString(params));
		try {
			userService.addUserLadp(params);
		} catch (Exception e) {
			System.out.println("添加 Ladp 用户失败");
			logger.error("addUserLadp方法错误："+JSON.toJSONString(params));
			e.printStackTrace();
		}
		
	}

	/**
	 * activiti-server使用，查询下一级办理人
	 * @param params
	 * @return
	 */
	@GetMapping(value = "/listUserByRoleName")
	@ResponseBody
	public List<UserDO> listUserByRoleName(@RequestParam Map<String, Object> params){
		
		logger.info("listUserByRoleName方法执行,参数"+JSON.toJSONString(params));
		List<UserDO> list=null;
		if (
				params.containsKey("roleName")&&params.get("roleName")!=null&&!"".equals(params.get("roleName"))&&
						params.containsKey("applEglName")&&params.get("applEglName")!=null&&!"".equals(params.get("applEglName"))&&
								params.containsKey("userStatus")&&params.get("userStatus")!=null&&!"".equals(params.get("userStatus"))
				) {
			
			list=userService.listUserByRoleName(params);
			logger.info("Feign调用listUserByRoleName完毕！");
			
		}else {
			logger.info("参数不全！");
			list=new ArrayList<UserDO>();
		}
		logger.info("listUserByRoleName方法执行完毕：返回："+JSON.toJSONString(list));
		return list;
	}

}
