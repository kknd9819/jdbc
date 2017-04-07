package top.zz.service.system;


import cn.shengyuan.basic.model.Page;
import cn.shengyuan.basic.service.BaseService;
import top.zz.model.admin.Log;
import top.zz.util.Pageable;

/**
 * 日志服务层接口
 * @Date 2014-12-30
 * @author 欧志辉
 * @version 1.0
 */
public interface LogService extends BaseService<Log, Long> {

	/**
	 * 清空日志
	 */
	public void clear();
	
	/**
	 * 分页查询日志
	 * @param pageable
	 * @return Page<Log>
	 */
	public Page<Log> findPage(Pageable pageable);

}