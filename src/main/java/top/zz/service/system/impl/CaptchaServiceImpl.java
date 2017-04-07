package top.zz.service.system.impl;


import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import top.zz.service.system.CaptchaService;
import top.zz.util.Setting;

import java.awt.image.BufferedImage;


/**
 * 验证码服务层实现
 * @Date 2014-01-03
 * @author 欧志辉
 * @version 1.0
 */
@Service("captchaServiceImpl")
public class CaptchaServiceImpl implements CaptchaService {

	 
	public BufferedImage buildImage(String captchaId) {
		return  null;
	}

	public boolean isValid(Setting.CaptchaType captchaType, String captchaId, String captcha) {
		//TODO captchaTypes从shopxx.xml读取
		Setting.CaptchaType[] captchaTypes = new Setting.CaptchaType[] {Setting.CaptchaType.memberLogin, Setting.CaptchaType.memberRegister,
				Setting.CaptchaType.adminLogin, Setting.CaptchaType.review, Setting.CaptchaType.consultation, Setting.CaptchaType.findPassword, Setting.CaptchaType.resetPassword, Setting.CaptchaType.other};
		if (captchaType == null || ArrayUtils.contains(captchaTypes, captchaType)) {
			if (StringUtils.isNotEmpty(captchaId) && StringUtils.isNotEmpty(captcha)) {
				try {
					return true;
				} catch (Exception e) {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

}