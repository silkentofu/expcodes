package exp.libs.warp.net.http;

/**
 * <PRE>
 * 封装了Apache-HttpClient.
 *  可以保持连接对象, 并介入获取连接过程中的请求/响应参数
 * </PRE>
 * 
 * <B>PROJECT：</B> exp-libs
 * <B>SUPPORT：</B> EXP
 * @version   1.0 2017-12-21
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class HttpClient extends _HttpClient {

	public HttpClient() {
		super();
	}
	
	public HttpClient(String charset, int connTimeout, int callTimeout) {
		super(charset, connTimeout, callTimeout);
	}

}
