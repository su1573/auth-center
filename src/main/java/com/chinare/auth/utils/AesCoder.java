package com.chinare.auth.utils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * AES对称加密算法，java6实现，bouncycastle也支持AES对称加密算法
 * 我们可以以AES算法实现为参考，完成RC2，RC4和Blowfish算法的实现
 * @author kongqz
 * */
public class AesCoder {
	static final Logger logger = LoggerFactory.getLogger(AesCoder.class);
	/**
	 * 密钥算法
	 * java6支持56位密钥，bouncycastle支持64位
	 * */
	public static final String KEY_ALGORITHM="AES";
	
	/**
	 * 加密/解密算法/工作模式/填充方式
	 * 
	 * JAVA6 支持PKCS5PADDING填充方式
	 * Bouncy castle支持PKCS7Padding填充方式
	 * */
	public static final String CIPHER_ALGORITHM="AES/ECB/PKCS5Padding";
	
	public static final String KEYSTRING="FbVFXsdfssll1wsssERMKw==";
	
//	private static final char C1 = '0';
//	private static final char C2 = 'z';
//	private static final char C3 = 'A';
//	private static final char C4 = (char)65536;
	
	/**
	 * 
	 * 生成密钥，java6只支持56位密钥，bouncycastle支持64位密钥
	 * @return byte[] 二进制密钥
	 * */
	public static byte[] initkey() throws Exception{
		
		//实例化密钥生成器
		KeyGenerator kg=KeyGenerator.getInstance(KEY_ALGORITHM);
		//初始化密钥生成器，AES要求密钥长度为128位、192位、256位
		kg.init(128);
		//生成密钥
		SecretKey secretKey=kg.generateKey();
		//获取二进制密钥编码形式
		return secretKey.getEncoded();
	}
	/**
	 * 转换密钥
	 * @param key 二进制密钥
	 * @return Key 密钥
	 * */
	public static Key toKey(byte[] key) throws Exception{
		//实例化DES密钥
		//生成密钥
		SecretKey secretKey=new SecretKeySpec(key,KEY_ALGORITHM);
		return secretKey;
	}
	
	/**
	 * 加密数据
	 * @param data 待加密数据
	 * @param key 密钥
	 * @return byte[] 加密后的数据
	 * */
	public static byte[] encrypt(byte[] data,byte[] key) throws Exception{
		//还原密钥
		Key k=toKey(key);
		/**
		 * 实例化
		 * 使用 PKCS7PADDING 填充方式，按如下方式实现,就是调用bouncycastle组件实现
		 * Cipher.getInstance(CIPHER_ALGORITHM,"BC")
		 */
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
		//初始化，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, k);
		//执行操作
		return cipher.doFinal(data);
	}
	/**
	 * 解密数据
	 * @param data 待解密数据
	 * @param key 密钥
	 * @return byte[] 解密后的数据
	 * */
	public static byte[] decrypt(byte[] data,byte[] key) throws Exception{
		//欢迎密钥
		Key k =toKey(key);
		/**
		 * 实例化
		 * 使用 PKCS7PADDING 填充方式，按如下方式实现,就是调用bouncycastle组件实现
		 * Cipher.getInstance(CIPHER_ALGORITHM,"BC")
		 */
		Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
		//初始化，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, k);
		//执行操作
		return cipher.doFinal(data);
	}
	public static String encode(String str){
		byte[] key=Base64.decodeBase64(KEYSTRING);
		try
		{
			//return getNewString(Base64.encodeBase64String(AesCoder.encrypt(str.getBytes(), key)),21);
			return  Base64.encodeBase64String(AesCoder.encrypt(str.getBytes(), key));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.toString(), e);
		}
		return null;
	}
	/** URL格式加密，解决windows、linux方式
	 * @param str 需要编码的字符串
	 * @param charSet 编码格式：如UTF-8、GBK等
	 * @return
	 */
	public static String URLEncode(String str,String charSet){
		String result = "";
		try {
			result =  URLEncoder.encode(encode(str),charSet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.toString(), e);
			return "";
		}
		return result;
	}
	/**
	 * @param str 需要进行URL格式的加密字符串，默认编码方式：UTF-8
	 * @return
	 */
	public static String URLEncode(String str){
		String result = "";
		try {
			result =  URLEncoder.encode(encode(str),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.toString(), e);
			return "";
		}
		return result;
	}
	/**
	 * 
	 * @param str
	 * @param charSet
	 * @return
	 */
	public static String URLDecode(String str,String charSet){
		String result = "";
		try {
			result =  decode(URLDecoder.decode(str,charSet));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.toString(), e);

			return "";
		}
		return result;
	}
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String URLDecode(String str){
		String result = "";
		try {
			result =  decode(URLDecoder.decode(str,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.toString(), e);

			return "";
		}
		return result;
	}
	public static String decode(String data){
		byte[] key=Base64.decodeBase64(KEYSTRING);
		try
		{
			return new String(AesCoder.decrypt(Base64.decodeBase64(data), key));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.toString(), e);

		}
		return null;
	}
	
	/**
	  * 对字符串进行简单的移位加密
	  * @param str
	  * @param key
	  * @return
	  */
//	public static String getNewString(String str,int key){
//		
//		  char[] chars = str.toCharArray();
//		  int len=chars.length;
//		  for(int i = len - 1 ; i >= 0 ; i --){
//		   if(chars[i] >= C3 && chars[i] <= C4){
//		    chars[i] += key;
//		    if(chars[i] > C4){
//		     chars[i] = (char) ( chars[i] -C4 + C3 - 1);
//		    }else if(chars[i] < C3){
//		     chars[i] = (char) (C4 - (C3 -chars[i]) + 1);
//		    }
//		   } else if(chars[i] >= C1 && chars[i] <= C2){
//		    chars[i] += key;
//		    if(chars[i] > C2){
//		     chars[i] = (char) ( chars[i] - C2 + C1 - 1);
//		    }else if(chars[i] < C1){
//		     chars[i] = (char) (C2 - (C1 - chars[i]) + 1);
//		    }
//		   }
//		   char temp = chars[0];
//		   chars[0] = chars[len-2];
//			chars[len-2] = temp;
//		  }
//		  return new String( chars );
//		 
//	}
	
	/**
	 * 获取原始字符串
	 * @param str
	 * @param key
	 * @return
	 */
//	public static String getInitString(String str,int key){
//		
//		  char[] chars = str.toCharArray();
//		  int len=chars.length;
//		  for(int i = len - 1 ; i >= 0 ; i --){
//			  char temp = chars[0];
//			   chars[0] = chars[len-2];
//				chars[len-2] = temp;
//		   if(chars[i] >= C3 && chars[i] <= C4){
//		    chars[i] += key;
//		    if(chars[i] > C4){
//		     chars[i] = (char) ( chars[i] -C4 + C3 - 1);
//		    }else if(chars[i] < C3){
//		     chars[i] = (char) (C4 - (C3 -chars[i]) + 1);
//		    }
//		   } else if(chars[i] >= C1 && chars[i] <= C2){
//		    chars[i] += key;
//		    if(chars[i] > C2){
//		     chars[i] = (char) ( chars[i] - C2 + C1 - 1);
//		    }else if(chars[i] < C1){
//		     chars[i] = (char) (C2 - (C1 - chars[i]) + 1);
//		    }
//		   }
//		  
//		  }
//		  return new String( chars );
//		 
//	}
	
	public static String str2HexStr(String str) {    
        char[] chars = "0123456789ABCDEF".toCharArray();    
        StringBuilder sb = new StringBuilder("");  
        byte[] bs = str.getBytes();    
        int bit;    
        for (int i = 0; i < bs.length; i++) {    
            bit = (bs[i] & 0x0f0) >> 4;    
            sb.append(chars[bit]);    
            bit = bs[i] & 0x0f;    
            sb.append(chars[bit]);    
        }    
        return sb.toString();    
    }
}