package com.chinare.auth.filter;

import org.springframework.ldap.core.AttributesMapper;

import com.chinare.auth.domain.UserDO;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

/**
 * 将ldap返回的结果，转成指定对象
 */
@SuppressWarnings("rawtypes")
public class LdapUserAttributeMapper implements AttributesMapper {

	/**
	 * 将单个Attributes转成单个对象
	 * 
	 * @param attrs
	 * @return
	 * @throws NamingException
	 */
	public UserDO mapFromAttributes(Attributes attr) throws NamingException {
		UserDO user = new UserDO();

		if (null != attr.get("sAMAccountName")) {
			System.out.println("1-------------------------------");
			user.setsAMAccountName((String) attr.get("sAMAccountName").get());
		}
		if (null != attr.get("displayName")) {
			System.out.println("2-------------------------------");
			user.setDisplayName((String) attr.get("displayName").get());
		}
		if (null != attr.get("mail")) {
			System.out.println("3-------------------------------");
			user.setMail((String) attr.get("mail").get());
		}
		if (null != attr.get("distinguishedName")) {
			System.out.println("3-------------------------------");
			user.setDistinguishedName((String) attr.get("distinguishedName").get());
		}

		return user;
	}

}