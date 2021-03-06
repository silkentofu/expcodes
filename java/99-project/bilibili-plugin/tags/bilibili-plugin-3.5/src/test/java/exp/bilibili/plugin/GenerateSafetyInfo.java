package exp.bilibili.plugin;

import exp.bilibili.plugin.utils.SafetyUtils;
import exp.bilibili.plugin.utils.TimeUtils;
import exp.libs.utils.encode.CryptoUtils;
import exp.libs.utils.num.NumUtils;

/**
 * <PRE>
 * 生成授权信息
 * </PRE>
 * <B>PROJECT：</B> bilibili-plugin
 * <B>SUPPORT：</B> EXP
 * @version   1.0 2017-12-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class GenerateSafetyInfo {

	public static void main(String[] args) {
		updatePrivateTime();
	}
	
	/**
	 * 更新对私授权时间
	 *  对私时间用于对外出售，限制其使用期限（过期后不管对公时间如何，均无法启动）
	 */
	public static void updatePrivateTime() {
		int day = 90;	// 授权时间(从当前开始往后推N天)
		String code = SafetyUtils.certificateToFile(day);	// 授权码
		System.out.println(code);
		System.out.println(TimeUtils.toStr(NumUtils.toLong(CryptoUtils.deDES(code))));
	}
	
}
