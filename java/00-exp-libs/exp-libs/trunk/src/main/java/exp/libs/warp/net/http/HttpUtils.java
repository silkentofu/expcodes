package exp.libs.warp.net.http;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.envm.Charset;
import exp.libs.utils.encode.Base64;
import exp.libs.utils.encode.CharsetUtils;
import exp.libs.utils.io.FileUtils;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * HTTP工具
 * </PRE>
 * 
 * <B>PROJECT：</B> exp-libs
 * <B>SUPPORT：</B> EXP
 * @version   1.0 2017-12-21
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class HttpUtils {

	/** 日志器 */
	protected final static Logger log = LoggerFactory.getLogger(HttpURLUtils.class);
	
	/** 默认编码 */
	public final static String DEFAULT_CHARSET = Charset.UTF8;
	
	/** 连接超时, 默认1分钟 */
	public final static int CONN_TIMEOUT = 60000;

	/** 响应/读取超时 , 默认1分钟 */
	public final static int CALL_TIMEOUT = 60000;
	
	/** URL协议类型:HTTPS */
	private final static String HTTPS = "https";
	
	/** SSL实例名称 */
	private final static String TLS = "tls";
	
	/** GET请求方法名 */
	public final static String METHOD_GET = "GET";
	
	/** POST请求方法名 */
	public final static String METHOD_POST = "POST";
	
	/**
	 * <PRE>
	 * HTTP请求头参数枚举, 请求头样例:
	 * 
	 * Accept:application/json, text/javascript;
	 * Accept-Encoding:gzip, deflate, br
	 * Accept-Language:zh-CN,zh;q=0.8,en;q=0.6
	 * Connection:keep-alive
	 * Content-Length:68
	 * Content-Type:application/x-www-form-urlencoded; charset=UTF-8
	 * Cookie:l=v; sid=891ab0o9; fts=1497517922; buvid3=0EE9E160-55FF-4EA6-9B8A-D37EAB81B76927368infoc; UM_distinctid=15d88e8e6da42-01bb282a267083-414a0229-100200-15d88e8e6db5a; pgv_pvi=958242816; rpdid=olwiqlmmxldoswoipwsxw; LIVE_BUVID=ebf2cce7237945227c579bec3e986459; LIVE_BUVID__ckMd5=08a5ee5cfdbdd99f; biliMzIsnew=1; biliMzTs=0; im_seqno_1650868=9548; DedeUserID=1650868; DedeUserID__ckMd5=686caa22740f2663; SESSDATA=e6e4328c%2C1515920104%2C162b21cd; bili_jct=9db6a9c26d414e848430dac8f7c2ea9b; finger=81df3ec0; Hm_lvt_8a6d461cf92ec46bd14513876885e489=1513755792; _dfcaptcha=f63bb4f803bb08dc6b7427b0afee793e; Hm_lvt_8a6e55dbd2870f0f5bc9194cddf32a02=1513749373,1513755780,1513756902,1513817560; Hm_lpvt_8a6e55dbd2870f0f5bc9194cddf32a02=1513820331
	 * Host:api.live.bilibili.com
	 * Origin:http://live.bilibili.com
	 * Referer:http://live.bilibili.com/269706
	 * User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36
	 * </PRE>
	 */
	public class HEAD {
		
		public class KEY {
			public final static String ACCEPT = "Accept";
			public final static String ACCEPT_ENCODING = "Accept-Encoding";
			public final static String ACCEPT_LANGUAGE = "Accept-Language";
			public final static String CONNECTION = "Connection";
			public final static String CONTENT_TYPE = "Content-Type";
			public final static String COOKIE = "Cookie";
			public final static String HOST = "Host";
			public final static String ORIGIN = "Origin";
			public final static String REFERER = "Referer";
			public final static String USER_AGENT = "User-Agent";
			
			public final static String CONTENT_ENCODING = "Content-Encoding";
			public final static String SET_COOKIE = "Set-Cookie";
		}
		
		public class VAL {
			
			/** 浏览器代理头标识: 假装是Mac，可避免被反爬 */
			public final static String USER_AGENT = 
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
//					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";
			
			/** 请求POST的数据是xml */
			public final static String POST_XML = 
					"application/x-javascript text/xml; charset=";
			
			/** 请求POST的数据是json */
			public final static String POST_JSON = 
					"application/x-javascript; charset=";
			
			/** 请求POST的数据是表单 */
			public final static String POST_FORM = 
					"application/x-www-form-urlencoded; charset=";
			
			/** 响应数据编码: gizp */
			public final static String GZIP = "gzip";
			
		}
		
	}
	
	/** 页面使用BASE64存储的图像信息正则 */
	private final static String RGX_BASE64_IMG = "data:image/([^;]+);base64,(.*)";
	
	/** 私有化构造函数 */
	protected HttpUtils() {}
	
	/**
	 * 测试URL是否有效
	 * @param url url路径
	 * @return true:有效; false:无效
	 */
	public static boolean testValid(final String url) {
		boolean isValid = false;
		try {
			HttpURLConnection conn = 
					(HttpURLConnection) new URL(url).openConnection();
			isValid = isResponseOK(conn);
		} catch (Exception e) {
			log.error("测试URL失败", e);
		}
		return isValid;
	}
	
	/**
	 * 判断HTTP请求是否响应成功
	 * @param conn
	 * @return
	 */
	protected static boolean isResponseOK(HttpURLConnection conn) {
		boolean isOk = false;
		try {
			isOk = (conn.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			log.error("提取HTTP状态码失败", e);
		}
		return isOk;
	}
	
	/**
	 * 判断HTTP响应状态码是否为成功
	 * @param responseCode 响应状态码
	 * @return
	 */
	protected static boolean isResponseOK(int httpStatus) {
		return (httpStatus == HttpStatus.SC_OK);
	}
	
	/**
	 * 构造HTTP/HTTPS连接
	 * @param url 目标地址
	 * @param method 请求方法：GET/POST
	 * @param header 请求头参数
	 * @return HTTP连接(失败返回null)
	 */
	public static HttpURLConnection createHttpConn(URL url, 
			String method, Map<String, String> header) {
		return createHttpConn(url, method, header, 
				CONN_TIMEOUT, CALL_TIMEOUT);
	}
	
	/**
	 * 构造HTTP/HTTPS连接
	 * @param url 目标地址
	 * @param method 请求方法：GET/POST
	 * @param header 请求头参数
	 * @param connTimeout 连接超时(ms)
	 * @param readTimeout 读取超时(ms)
	 * @return HTTP连接(失败返回null)
	 */
	public static HttpURLConnection createHttpConn(URL url, String method, 
			Map<String, String> header, int connTimeout, int readTimeout) {
		HttpURLConnection conn = null;
		try {
			conn = _createHttpConn(url, method, header, connTimeout, readTimeout);
			
		} catch(Exception e) {
			log.error("创建HTTP连接失败", e);
		}
		return conn;
	}
	
	/**
	 * 构造HTTP/HTTPS连接
	 * @param url 目标地址
	 * @param method 请求方法：GET/POST
	 * @param header 请求头参数
	 * @param connTimeout 连接超时(ms)
	 * @param readTimeout 读取超时(ms)
	 * @return HTTP连接(失败返回null)
	 * @throws Exception
	 */
	private static HttpURLConnection _createHttpConn(URL url, String method, 
			Map<String, String> header, int connTimeout, int readTimeout) 
					throws Exception {
		HttpURLConnection conn = null;
		if (url == null) {
			return conn;
		}
		
		// HTTPS连接(若依然报错 protocol_version， 则调用此方法的程序需切换到JDK1.8以上)
		if(HTTPS.equals(url.getProtocol())) {
			SSLContext ssl = SSLContext.getInstance(TLS);
			ssl.init(new KeyManager[0], new TrustManager[] { new _X509TrustManager() }, new SecureRandom());
			HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
			httpsConn.setSSLSocketFactory(ssl.getSocketFactory());
			httpsConn.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			conn = httpsConn;
			
		// HTTP连接
		} else {
			conn = (HttpURLConnection) url.openConnection();
		}

		// 设置固有请求参数
		conn.setRequestMethod(method);
		conn.setConnectTimeout(connTimeout);
		conn.setReadTimeout(readTimeout);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		
		// 设置自定义请求头参数
		if(header != null) {
			Iterator<String> keyIts = header.keySet().iterator();
			while(keyIts.hasNext()) {
				String key = keyIts.next();
				String val = header.get(key);
				if(StrUtils.isNotEmpty(key, val)) {
					conn.setRequestProperty(key, val);
				}
			}
		}
		return conn;
	}

	/**
	 * 关闭HTTP/HTTPS连接
	 * @param httpClient
	 */
	public static void close(HttpURLConnection conn) {
		if(conn != null) {
			conn.disconnect();
		}
	}
	
	/**
	 * 创建HttpClient会话
	 * @return
	 */
	public static HttpClient createHttpClient() {
		return createHttpClient(CONN_TIMEOUT, CALL_TIMEOUT);
	}

	/**
	 * 创建HttpClient会话
	 * @param connTimeout
	 * @param callTimeout
	 * @return
	 */
	public static HttpClient createHttpClient(int connTimeout, int callTimeout) {
		HttpConnectionManagerParams managerParams = new HttpConnectionManagerParams();
		managerParams.setConnectionTimeout(connTimeout);
		managerParams.setDefaultMaxConnectionsPerHost(2);
		managerParams.setSoTimeout(callTimeout);
		
		HttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
		httpConnectionManager.setParams(managerParams);

		HttpClient httpClient = new HttpClient(new HttpClientParams());
		httpClient.setHttpConnectionManager(httpConnectionManager);
		return httpClient;
	}
	
	/**
	 * 关闭HttpClient会话
	 * @param httpClient
	 */
	public static void close(HttpClient httpClient) {
		if(httpClient != null) {
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
		}
	}
	
	/**
	 * 对URL进行编码
	 * @param url url路径
	 * @return 编码后URL
	 */
	public static String encodeURL(final String url) {
		return encodeURL(url, DEFAULT_CHARSET);
	}
	
	/**
	 * 对URL进行编码
	 * @param url url路径
	 * @param charset 编码字符集
	 * @return 编码后URL
	 */
	public static String encodeURL(final String url, final String charset) {
		String encodeURL = "";
		try {
			encodeURL = URLEncoder.encode(url, charset);
			
		} catch (Exception e) {
			log.error("对URL以 [{}] 编码失败: {}", charset, url, e);
		}
		return encodeURL;
	}

	/**
	 * 对URL进行解码
	 * @param url url路径
	 * @return 解码后URL
	 */
	public static String decodeURL(final String url) {
		return decodeURL(url, DEFAULT_CHARSET);
	}
	
	/**
	 * 对URL进行解码
	 * @param url url路径
	 * @param charset 编码字符集
	 * @return 解码后URL
	 */
	public static String decodeURL(final String url, final String charset) {
		String decodeURL = "";
		try {
			decodeURL = URLDecoder.decode(url, charset);
			
		} catch (Exception e) {
			log.error("对URL以 [{}] 解码失败: {}", charset, url, e);
		}
		return decodeURL;
	}
	
	/**
	 * 把请求参数转换成URL的KV串形式并进行编码
	 * @param request 请求参数集
	 * @return ?&key1=val1&key2=val2&key3=val3
	 */
	public static String encodeRequests(Map<String, String> request) {
		return encodeRequests(request, DEFAULT_CHARSET);
	}
	
	/**
	 * 把请求参数转换成URL的KV串形式并进行编码
	 * @param request 请求参数集
	 * @param charset 参数字符编码
	 * @return ?key1=val1&key2=val2&key3=val3
	 */
	public static String encodeRequests(
			Map<String, String> request, final String charset) {
		if(request == null || request.isEmpty() || 
				CharsetUtils.isInvalid(charset)) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder("?");
		Iterator<String> keyIts = request.keySet().iterator();
		while(keyIts.hasNext()) {
			String key = keyIts.next();
			String val = request.get(key);
			try {
				val = URLEncoder.encode(val, charset);
			} catch (Exception e) {
				val = "";
			}
			
			// 注意：
			//   第一个参数开头的&，对于POST请求而言是必须的
			//   但对于GET请求则是可有可无的（但存在某些网页会强制要求不能存在）
			if(StrUtils.isNotEmpty(key, val)) {
				sb.append("&").append(key).append("=").append(val);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 拼接GET请求的URL和参数(对于第一个参数开头的&, 强制去除)
	 * @param url GET请求URL
	 * @param requestKVs GET请求参数表 (需通过{@link encodeRequests}方法转码)
	 * @return GET请求URL
	 */
	protected static String concatGET(String url, String requestKVs) {
		url = StrUtils.isEmpty(url) ? "" : url;
		String _GETURL = url.concat(requestKVs);
		return _GETURL.replace(url.concat("?&"), url.concat("?"));	// 去掉第一个参数的&
	}
	
	/**
	 * 保存Base64编码的图片数据到本地
	 * @param dataUrl 图片数据编码地址，格式形如   data:image/png;base64,base64编码的图片数据
	 * @param saveDir 希望保存的图片目录
	 * @param imgName 希望保存的图片名称（不含后缀，后缀通过编码自动解析）
	 * @return 图片保存路径（若保存失败则返回空字符串）
	 */
	public static String convertBase64Img(String dataUrl, 
			String saveDir, String imgName) {
		String savePath = "";
		Pattern ptn = Pattern.compile(RGX_BASE64_IMG);  
        Matcher mth = ptn.matcher(dataUrl);      
        if(mth.find()) {
        	String ext = mth.group(1);	// 图片后缀
        	String base64Data = mth.group(2);	// 图片数据
            savePath = StrUtils.concat(saveDir, "/", imgName, ".", ext);
            
            try {
            	byte[] data = Base64.decode(base64Data);  
                FileUtils.writeByteArrayToFile(new File(savePath), data, false);
                
            } catch (Exception e) {  
                log.error("转换Base64编码图片数据到本地文件失败: [{}]", savePath, e);
            }
        }
        return savePath;  
    }
	
}
