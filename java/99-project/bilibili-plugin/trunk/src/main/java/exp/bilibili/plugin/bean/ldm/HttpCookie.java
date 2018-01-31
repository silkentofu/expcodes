package exp.bilibili.plugin.bean.ldm;

import java.util.LinkedList;
import java.util.List;

import exp.bilibili.plugin.envm.CookieType;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 单个账号的cookie集
 * </PRE>
 * <B>PROJECT：</B> bilibili-plugin
 * <B>SUPPORT：</B> EXP
 * @version   1.0 2018-01-31
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class HttpCookie {
	
	public final static HttpCookie NULL = new HttpCookie();
	
	private final static String LFCR = "\r\n";
	
	/** B站CSRF标识 */
	private final static String CSRF_KEY = "bili_jct";
	
	/** B站用户ID标识 */
	private final static String UID_KEY = "DedeUserID";
	
	/** 登陆类型 */
	private CookieType type;
	
	/** 自动投喂 */
	private boolean autoFeed;
	
	/** 该cookie对应的用户ID */
	private String uid;
	
	/** 该cookie对应的用户昵称 */
	private String nickName;
	
	private List<_HttpCookie> cookies;
	
	/** 多个cookies组合而成的NV串 */
	private String nvCookies;
	
	/** 从cookies提取的csrf token */
	private String csrf;
	
	/** cookies是否发生变化 */
	private boolean isChanged;
	
	public HttpCookie() {
		this.type = CookieType.UNKNOW;
		this.autoFeed = false;
		this.uid = "";
		this.nickName = "";
		this.cookies = new LinkedList<_HttpCookie>();
		this.nvCookies = "";
		this.csrf = "";
		isChanged = false;
	}
	
	public HttpCookie(String headerCookies) {
		this();
		
		if(StrUtils.isNotEmpty(headerCookies)) {
			String[] lines = headerCookies.split(LFCR);
			for(int i = 0; i < lines.length; i++) {
				add(lines[i]);
			}
		}
	}
	
	/**
	 * cookies是否有效
	 * @return true:有效; false:无效
	 */
	public boolean isVaild() {
		return (cookies.size() > 0 && StrUtils.isNotEmpty(uid, nickName));
	}
	
	public void add(String headerCookie) {
		add(new _HttpCookie(headerCookie));
	}
	
	public void add(_HttpCookie cookie) {
		if(cookie != null && cookie.isVaild()) {
			isChanged = true;
			cookies.add(cookie);
			
			if(CSRF_KEY.equalsIgnoreCase(cookie.getName())) {
				csrf = cookie.getValue();
				
			} else if(UID_KEY.equalsIgnoreCase(cookie.getName())) {
				uid = cookie.getValue();
			}
		}
	}
	
	public String toNVCookie() {
		if(isChanged == true) {
			
			StringBuilder kvs = new StringBuilder();
			for(_HttpCookie cookie : cookies) {
				kvs.append(cookie.toNV()).append("; ");
			}
			nvCookies = kvs.toString();
		}
		return nvCookies;
	}
	
	public String toHeaderCookie() {
		StringBuilder sb = new StringBuilder();
		for(_HttpCookie cookie : cookies) {
			sb.append(cookie.toString()).append(LFCR);
		}
		return sb.toString();
	}
	
	public CookieType TYPE() {
		return type;
	}
	
	public String CSRF() {
		return csrf;
	}
	
	public String UID() {
		return uid;
	}
	
	public String NICKNAME() {
		return nickName;
	}
	
	public void setType(CookieType type) {
		this.type = type;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public boolean isAutoFeed() {
		return autoFeed;
	}

	public void setAutoFeed(boolean autoFeed) {
		this.autoFeed = autoFeed;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof HttpCookie)) {
			return false;
		}
		
		HttpCookie other = (HttpCookie) obj;
		return this.uid.equals(other.uid);
	}
	
	@Override
	public int hashCode() {
		return uid.hashCode();
	}
	
	@Override
	public String toString() {
		return toHeaderCookie();
	}
	
}