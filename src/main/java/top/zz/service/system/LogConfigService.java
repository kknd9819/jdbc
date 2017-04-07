package top.zz.service.system;


import top.zz.model.admin.vo.LogConfig;

import java.util.List;

/**
 * 日志配置服务层接口
 * @Date 2014-12-30
 * @author 欧志辉
 * @version 1.0
 */
public interface LogConfigService {

	/**
	 * 获取所有日志配置
	 * @return 所有日志配置
	 */
	public List<LogConfig> getAll();

}