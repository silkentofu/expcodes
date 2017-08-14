package exp.libs.mrp.envm;

/**
 * <PRE>
 * 占位符名称定义。
 * 要求所有模板文件中用到的占位符都要先在此处定义，以便管理。
 * 
 * </PRE>
 * <B>项    目：</B>凯通J2SE开发平台(KTJSDP)
 * <B>技术支持：</B>广东凯通软件开发技术有限公司 (c) 2014
 * @version   1.0 2014-09-12
 * @author    廖权斌：liaoquanbin@gdcattsoft.com
 * @since     jdk版本：jdk1.6
 */
public class Placeholders {

	/** 项目名称 */
	public final static String PROJECT_NAME = "project_name";

	/** 线程后缀, 附加在启动脚本/停止脚本的项目名称后面 */
	public final static String THREAD_SUFFIX = "thread_suffix";
	
	/** 项目版本 */
	public final static String PROJECT_VERSION = "project_version";
	
	/** 项目编码 */
	public final static String PROJECT_CHARSET = "project_charset";
	
	/** 变量声明 */
	public final static String VARIABLE_DECLARATION = "variable_declaration";
	
	/** JDK路径 */
	public final static String JDK_PATH = "jdk_path";
	
	/** JDK版本 */
	public final static String JDK_VERSION = "jdk_version";
	
	/** JDK参数表 */
	public final static String JDK_PARAMS = "jdk_params";
	
	/** 依赖包 */
	public final static String CLASSPATH = "classpath";
	
	/** main方法 */
	public final static String MAIN_METHOD = "main_method";
	
	/** main方法参数 */
	public final static String MAIN_METHOD_PARAMS = "main_method_params";
	
	/** 标准流输出控制 */
	public final static String STDOUT_CTRL = "stdout_ctrl";
	
	/** 异常流输出控制 */
	public final static String ERROUT_CTRL = "errout_ctrl";
	
	/** 异常流输出控制 */
	public final static String RUN_IN_BACKGROUND = "run_in_background";
	
	/** 源码目录 */
	public final static String SRC_DIR = "src_dir";
	
	/** 收尾操作 */
	public final static String END_OP = "end_op";
	
	/** 禁止外部构造，避免误用 */
	private Placeholders() {}
	
}
