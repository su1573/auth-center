package com.chinare.auth.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.chinare.auth.annotation.Log;
import com.chinare.auth.domain.ApplDO;
import com.chinare.auth.domain.UserDO;
import com.chinare.auth.service.UserService;
import com.chinare.auth.utils.CookieUtils;
import com.chinare.auth.utils.JwtUtils;
import com.chinare.auth.utils.MD5Utils;
import com.chinare.auth.utils.R;


@Controller
public class AuthController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	

	@Value("${authLocal.enable}")
	private  String authLocal;
	
	@Autowired
	UserService userService;
	
	@Value("${spring.application.name}")
	private  String applEglName;
	
	@Autowired
	UserController usercontroller;
	
	@Autowired
	RedisTemplate redisTemplate;
	
	@Autowired
	StringRedisTemplate stringredisTemplate;
	
	
	@Resource
	private CacheManager cacheManager;

	@GetMapping("/login")
	public ModelAndView login(@CookieValue(value = "CASTOKEN", required = false) String token, String service,
			Model model, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		// 先判断统一认证有没有登录。
		boolean login = false;
		String username = null;
		Cookie CASTOKEN_cookie = CookieUtils.getCookieByName(request, "CASTOKEN");
		if (CASTOKEN_cookie != null) {
			login = true;
			username = CASTOKEN_cookie.getValue();
		}
		if (login) {
			// 有登陆，发tiket.重定向到service对应的地址。
			if(service!=null&&!"".equals(service)) {
				System.out.println("有登录 ，直接跳转！！！！！！！！！！！");
				goTargetSystem(response, service, username);
				logger.info("---有登录 ，直接跳转！！！！！！！！！！");
				return null;
			}else {
				Map<String, Object> params = new HashMap<>(16);
				params.put("username", username);
				UserDO user = null;
				if (username != null && !"".equals(username)) {
					// 有登陆信息，获取用户
					params.put("username", username);
					user = userService.getUserByUserName(params);
				}
				// 查询有多少个系统的权限，并展示到前端。
				List<ApplDO> userapps = userService.getUserApp(user.getUserId());
				mav.addObject("userapps", userapps);
				mav.setViewName("index");
				return mav;
			}
		}
		// 如果没有登录，跳转到统一认证的登录页。进行登录
		System.out.println("没有登录进行登录。。。。。。");
		mav.addObject("toUrl", service);
		mav.setViewName("login");
		logger.info("---login----执行完毕； 没有登录进行登录");
		return mav;
	}
	@GetMapping("/logout")
	public ModelAndView logout(String service, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		
		ModelAndView mav = new ModelAndView();
		//将  统一认证的CASTOKEN 过期。
		CookieUtils.addCookie(response, "CASTOKEN","", 0);
		
		
			System.out.println("开始清理Cookie。。。。。。。");
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(cookie);
				}
			}
			
			mav.addObject("toUrl", service);
			mav.setViewName("login");
			return mav;
	}
	
	/**
	 * 主页面 进行退出的时候，service就能拼到url后面，
	 * 而权限中心 退出的时候，service 也有值 但是 就不能拼到 url后面。
	 * 所以写了 logout2 方法，仅仅用于 权限中心进行退出的时候。 把service直接转发。
	 * @param service
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	
	@GetMapping("/logout2")
	public ModelAndView logout2(String service, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		
		ModelAndView mav = new ModelAndView();
		//将  统一认证的CASTOKEN 过期。
		CookieUtils.addCookie(response, "CASTOKEN","", 0);
		
		HttpSession session = request.getSession();
		service = (String) session.getAttribute("toUrl");
			logger.info("开始清理Cookie。。。。。。。");
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(cookie);
				}
			}
			//mav.addObject("toUrl", service);
			mav.setViewName("login");
			return mav;
	}
	

	/**
	 * 登录验证，验证成功，生成全局CASTOKEN,并存放到cookie中。
	 * 
	 * @param username
	 * @param password
	 * @param toUrl
	 * @param model
	 * @param response
	 * @return
	 */
	@PostMapping("/login")
	public ModelAndView login(String username, String password, String toUrl, Model model, HttpServletResponse response,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		Cookie CASTOKEN_cookie = CookieUtils.getCookieByName(request, "CASTOKEN");
		
		if (CASTOKEN_cookie != null) {
			logger.info("切勿重复登录,需退出当前账户");
			mav.addObject("msg", "切勿重复登录,需退出当前账户");
			mav.setViewName("login");
			logger.error("切勿重复登录,需退出当前账户");
			return mav;
		}
		
		Map<String, Object> params = new HashMap<>(16);
		params.put("username", username);

		boolean isLadpAuth = false;
		String password2 = MD5Utils.encrypt(username, password);

		UserDO user = null;
		// 首先判断ldap是否验证通过。
		isLadpAuth = userService.AuthLadp(username, password);
		
		logger.info("---验证用户："+username);

		if (authLocal.equals("false")) {
			logger.info("---只能通过ladp进行验证。");
			if (!isLadpAuth) {
				logger.error("LADP验证失败。。。。。。。。。。");
				mav.addObject("toUrl", toUrl);
				mav.addObject("msg", "账号或者密码错误，请重新输入");
				mav.setViewName("login");
				return mav;
			}else {
				user = userService.saveUserLadp(username, password);
			}

		} else {
			logger.info("---通过ladp进行验证，还可以通过本地验证");
			if (isLadpAuth) {
				user = userService.saveUserLadp(username, password);
			} else {
				params.put("username", username);
				user = userService.getUserByUserName(params);
			}

		}

		// 账号不存在
		if (user == null && !isLadpAuth) {
			logger.info("账号:" + username + "不存在!");
			mav.addObject("toUrl", toUrl);
			mav.addObject("msg", "账号或者密码错误，请重新输入");
			mav.setViewName("login");
			logger.error("账号不存在");
			return mav;
		}
		// 密码错误
		if (!password2.equals(user.getPassword()) && !isLadpAuth) {
			mav.addObject("toUrl", toUrl);
			mav.addObject("msg", "账号或者密码错误，请重新输入");
			mav.setViewName("login");
			logger.error("密码错误");
			return mav;

		}

		// 账号锁定，逻辑还有问题。目前在统一认证还无法锁定ldap用户。应该加上
		if (user.getStatus() == 0 && !isLadpAuth) {
			logger.info("账号:" + username + "已被锁定,请联系管理员!");
			mav.addObject("toUrl", toUrl);
			mav.addObject("msg", "账号已被锁定,请联系管理员!");
			mav.setViewName("login");
			logger.error("账号锁定");
			return mav;
		}

		// 如果登录成功。添加统一认证本身的Cookie.并设置生效时间。
		// 1.设置统一认证的cookie,这个cookie是对称加密(此处采用AES)，是统一认证的，私钥不用暴露给别人.
		CookieUtils.addCookie(response, "CASTOKEN", username, 60 * 600);
		
		// 登录成功.如果有跳转的页面，直接跳转。
		if (toUrl != null && !"".equals(toUrl)) {
			System.out.println("登录完之后，再进行跳转了!!!!");
			HttpSession session = request.getSession();
			session.setAttribute("toUrl", toUrl);
			goTargetSystem(response, toUrl, username);
			return null;

		} else {
			// 如果没有跳转的页面，查询有多少个系统的权限，并展示到前端。
			List<ApplDO> userapps = userService.getUserApp(user.getUserId());
			mav.addObject("userapps", userapps);
			mav.setViewName("index");
			return mav;
		}
	}
	
	/**
	 * 根据Jwttoken换取UserName
	 * 
	 * @return
	 */
	@GetMapping("/sys/auth/getUserNameByToken")
	@ResponseBody
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            String username =  jwt.getClaim("username").asString();
            return username;
        } catch (JWTDecodeException e) {
            return null;
        }
    }
	
	//有哪些登录系统权限
	@Log("访问主页")
	@GetMapping("/index")
	public ModelAndView index(Model model, HttpServletRequest request, HttpServletResponse response) {
		// 先判断统一认证有没有登录。
		String username = null;
		ModelAndView mav = new ModelAndView();
		
		Cookie CASTOKEN_cookie = CookieUtils.getCookieByName(request, "CASTOKEN");
		if (CASTOKEN_cookie != null) {
			// 获取cookie中的登录信息
			username = CASTOKEN_cookie.getValue();
		
		
		Map<String, Object> params = new HashMap<>(16);
		params.put("username", username);
		UserDO user = null;
		List<ApplDO> userapps = null;
		if (username != null && !"".equals(username)) {
			// 有登陆信息，获取用户
			params.put("username", username);
			user = userService.getUserByUserName(params);
			userapps = userService.getUserApp(user.getUserId());
		}
		// 查询有多少个系统的权限，并展示到前端。
		mav.addObject("userapps", userapps);
		mav.setViewName("index");
		}else {
			mav.setViewName("login");
		}
		return mav;
	}
	
		//有哪些能配置数据系统 权限
		@Log("访问主页")
		@GetMapping("/indexauth")
		public ModelAndView indexauth(Model model,String name, HttpServletRequest request, HttpServletResponse response) {
			// 先判断统一认证有没有登录。
			String username = null;
			ModelAndView mav = new ModelAndView();

			Cookie CASTOKEN_cookie = CookieUtils.getCookieByName(request, "CASTOKEN");
			if (CASTOKEN_cookie != null) {
				// 获取cookie中的登录信息
				username = CASTOKEN_cookie.getValue();
			
			
			Map<String, Object> params = new HashMap<>(16);
			params.put("username", username);
			UserDO user = null;
			List<ApplDO> userapps = null;
			if (username != null && !"".equals(username)) {
				// 查询有多少个  配置系统的权限，并展示到前端。
				params.put("username", username);
				user = userService.getUserByUserName(params);
				userapps = userService.getUserAuthApp(user.getUserId());
			}
			// 展示到前端。
			mav.addObject("userapps", userapps);
			mav.setViewName("indexauth");
			}else {
				mav.setViewName("login");
			}
			return mav;
		}
	

	/**
	 * 验证子系统ticket，并发放jwttoken.
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@GetMapping("/sys/auth/getJwtToken")
	@ResponseBody
	public R getJwtToken(@RequestParam Map<String, Object> params, Model model, HttpServletResponse response,HttpServletRequest request) {
		// 获取ticket.并进行验证。
		String ticket = params.get("ticket").toString();
		String applEglName = params.get("applEglName").toString();
		
		
		if(params.get("ticket") == null ||  params.get("applEglName") == null) {
			logger.error("---------------ticket为空，或者applEglName为空---------");
		}
		
		String username = null;
		username = stringredisTemplate.opsForValue().get(ticket);
		logger.info("-----------ticket为:"+ticket+"换取的username为："+username+"----------");

		// 发放token之前 进行权限校验，权限校验成功才可以发放jwtoken
		if (username != null && !"".equals(username)) {
			applEglName = applEglName.replace("_server", "").trim();
			applEglName = applEglName.replace("-server", "").trim();
			params.put("username", username);
			params.put("app_name", applEglName);
			UserDO user = null;
			user = usercontroller.getUserByUserName(params);
						if (user == null) {
				//权限校验不成功，跳转index页面。
				this.index(model,request,response);
				logger.error("-----------权限校验失败，获取user对象为空 ,返回index页面，jwttoken为空。。");
				return null;
			}
		}
		
		// 如果Ticket没有问题,并且权限校验通过，发放jwtTocken
		if (username != null && !"".equals(username)) {
			String jwttoken = JwtUtils.generateJwtToken(username);
			logger.info("----------------ticket换取的jwttoken为："+jwttoken);
			 stringredisTemplate.delete(ticket);
			return R.ok(jwttoken);
		} else {
			// 如果不匹配，返回空
			logger.error("----获取jwttoken为空。username="+username+"ticket="+ticket);
			return null;
		}
	}

	/**
	 * 发放ticket.跳转至目标系统。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param targetUrl
	 *            目标系统的URL
	 * @param username
	 */
	private void goTargetSystem(HttpServletResponse response, String targetUrl, String username) {
		String ticket = UUID.randomUUID().toString();
		String URl = targetUrl + "?ticket=" + ticket;
		
		// 过期时间30分钟。
		stringredisTemplate.opsForValue().set(ticket, username,60*30,TimeUnit.SECONDS);
		logger.info("-----------goTargetSystem方法执行跳转目标系统,URL参数:"+URl);
		try {
			response.sendRedirect(URl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    @GetMapping({"/", ""})
    void welcome(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	response.sendRedirect("/login");        
    }
    
}
