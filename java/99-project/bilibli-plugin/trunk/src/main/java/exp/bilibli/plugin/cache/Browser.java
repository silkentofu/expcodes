package exp.bilibli.plugin.cache;

import java.io.File;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;

import exp.bilibli.plugin.Config;
import exp.bilibli.plugin.bean.ldm.BrowserDriver;
import exp.bilibli.plugin.envm.WebDriverType;
import exp.libs.utils.other.ObjUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * 浏览器驱动管理器
 * </PRE>
 * <B>PROJECT：</B> exp-libs
 * <B>SUPPORT：</B> EXP
 * @version   1.0 2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Browser {

	private final static String COOKIE_DIR = Config.getInstn().COOKIE_DIR();
	
	private final static int WAIT_ELEMENT_TIME = Config.getInstn().WAIT_ELEMENT_TIME();
	
	private BrowserDriver browser;
	
	private static volatile Browser instance;
	
	private Browser() {}
	
	private static Browser INSTN() {
		if(instance == null) {
			synchronized (Browser.class) {
				if(instance == null) {
					instance = new Browser();
				}
			}
		}
		return instance;
	}
	
	public static void reset(boolean loadImages) {
		INSTN()._reset(loadImages);
	}
	
	/**
	 * 重置浏览器驱动
	 * @param loadImages
	 * @return
	 */
	private void _reset(boolean loadImages) {
		backupCookies();
		quit();
		browser = new BrowserDriver(WebDriverType.PHANTOMJS, 
				loadImages, WAIT_ELEMENT_TIME);
		recoveryCookies();
	}
	
	public static void open(String url) {
		INSTN()._open(url);
	}
	
	private void _open(String url) {
		if(browser == null ){
			_reset(false);
		}
		browser.open(url);
	}
	
	@Deprecated
	public static void close() {
		INSTN()._close();
	}
	
	/**
	 * 关闭当前页面(若是最后一个页面, 则会关闭浏览器)
	 */
	@Deprecated
	private void _close() {
		if(browser != null) {
			browser.close();
		}
	}
	
	public static void quit() {
		INSTN()._quit();
	}
	
	/**
	 * 退出浏览器
	 */
	private void _quit() {
		if(browser != null) {
			browser.quit();
			browser = null;
		}
	}
	
	public static String getCurURL() {
		return INSTN()._getCurURL();
	}
	
	private String _getCurURL() {
		return (browser == null ? "" : browser.getCurURL());
	}
	
	public static void clearCookies() {
		INSTN()._clearCookies();
	}
	
	private void _clearCookies() {
		if(browser != null) {
			browser.clearCookies();
		}
	}
	
	public static boolean addCookie(Cookie cookie) {
		return INSTN()._addCookie(cookie);
	}
	
	private boolean _addCookie(Cookie cookie) {
		return (browser == null ? false : browser.addCookie(cookie));
	}
	
	public static void backupCookies() {
		INSTN()._backupCookies();
	}
	
	private void _backupCookies() {
		if(browser != null) {
			int idx = 0;
			Set<Cookie> cookies = browser.getCookies();
			for(Cookie cookie : cookies) {
				String sIDX = StrUtils.leftPad(String.valueOf(idx++), '0', 2);
				String savePath = StrUtils.concat(COOKIE_DIR, "/cookie-", sIDX, ".dat");
				ObjUtils.toSerializable(cookie, savePath);
			}
		}
	}
	
	public static int recoveryCookies() {
		return INSTN()._recoveryCookies();
	}
	
	private int _recoveryCookies() {
		int cnt = 0;
		if(browser != null) {
			File dir = new File(COOKIE_DIR);
			File[] files = dir.listFiles();
			for(File file : files) {
				try {
					Cookie cookie = (Cookie) ObjUtils.unSerializable(file.getPath());
					cnt += (browser.addCookie(cookie) ? 1 : 0);
				} catch(Throwable e) {}
			}
		}
		return cnt;
	}
	
	public static boolean existElement(By by) {
		return INSTN()._existElement(by);
	}
	
	private boolean _existElement(By by) {
		return (_findElement(by) != null);
	}
	
	public static WebElement findElement(By by) {
		return INSTN()._findElement(by);
	}
	
	private WebElement _findElement(By by) {
		return (browser == null ? null : browser.findElement(by));
	}
	
	public static void click(WebElement element) {
		INSTN()._click(element);
	}
	
	private void _click(WebElement element) {
		if(browser != null) {
			browser.click(element);
		}
	}
	
	/**
	 * 使浏览器跳转到指定页面后截图
	 * @param driver 浏览器驱动
	 * @param url 跳转页面
	 * @param imgPath 图片保存路径
	 */
	public static void screenshot(String imgPath) {
		INSTN()._screenshot(imgPath);
	}
	
	/**
	 * 对浏览器的当前页面截图
	 * @param imgPath 图片保存路径
	 */
	private void _screenshot(String imgPath) {
		if(browser != null) {
			browser.screenshot(imgPath);
		}
	}
	
}