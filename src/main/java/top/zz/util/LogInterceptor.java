package top.zz.util;

import cn.shengyuan.tools.util.IpUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.zz.model.admin.Log;
import top.zz.model.admin.vo.LogConfig;
import top.zz.service.system.AdminService;
import top.zz.service.system.LogConfigService;
import top.zz.service.system.LogService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.Map.Entry;

/**
 * 日志拦截器
 * @Date 2014-01-03
 * @author 欧志辉
 * @version 1.0
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

	/** 默认忽略参数 */
	private static final String[] DEFAULT_IGNORE_PARAMETERS = new String[] { "password", "rePassword", "currentPassword" };

	/** antPathMatcher */
	private static AntPathMatcher antPathMatcher = new AntPathMatcher();

	/** 忽略参数 */
	private String[] ignoreParameters = DEFAULT_IGNORE_PARAMETERS;

	@Resource(name = "logConfigServiceImpl")
	private LogConfigService logConfigService;
	
	@Resource(name = "logServiceImpl")
	private LogService logService;
	
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@SuppressWarnings("unchecked")
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		List<LogConfig> logConfigs = logConfigService.getAll();
		if (logConfigs != null) {
			String path = request.getServletPath();
			for (LogConfig logConfig : logConfigs) {
				if (antPathMatcher.match(logConfig.getUrlPattern(), path)) {
					String username = adminService.getCurrentUsername();
					String operation = logConfig.getOperation();
					String operator = username;
					String content = (String) request.getAttribute(Log.LOG_CONTENT_ATTRIBUTE_NAME);
					request.removeAttribute(Log.LOG_CONTENT_ATTRIBUTE_NAME);
					StringBuffer parameter = new StringBuffer();
					Map<String, String[]> parameterMap = request.getParameterMap();
					List<String> keys = new ArrayList<String>(parameterMap.keySet());
					Collections.sort(keys);
					Map<String, String[]> parameters = new LinkedHashMap<String, String[]>();
					for (int i = 0; i < keys.size(); i++) {
						parameters.put(keys.get(i), parameterMap.get(keys.get(i)));
					}
					if (parameters != null) {
						for (Entry<String, String[]> entry : parameters.entrySet()) {
							String parameterName = entry.getKey();
							if (!ArrayUtils.contains(ignoreParameters, parameterName)) {
								String[] parameterValues = entry.getValue();
								if (parameterValues != null) {
									for (String parameterValue : parameterValues) {
										parameter.append(parameterName + " = " + parameterValue + "\n");
									}
								}
							}
						}
					}
					Log log = new Log();
					log.setCreateDate(new Date());
					log.setModifyDate(new Date());
					log.setOperation(operation);
					log.setOperator(operator);
					log.setContent(content);
					log.setParameter(parameter.toString());
					log.setIp(IpUtils.getIpAddr(request));
					logService.save(log);
					break;
				}
			}
		}
	}

	/**
	 * 设置忽略参数
	 * @return 忽略参数
	 */
	public String[] getIgnoreParameters() {
		return ignoreParameters;
	}

	/**
	 * 设置忽略参数
	 * @param ignoreParameters 忽略参数
	 */
	public void setIgnoreParameters(String[] ignoreParameters) {
		this.ignoreParameters = ignoreParameters;
	}

}