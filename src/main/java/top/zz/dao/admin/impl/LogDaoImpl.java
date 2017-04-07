package top.zz.dao.admin.impl;



import cn.shengyuan.basic.dao.base.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;
import top.zz.dao.admin.LogDao;
import top.zz.model.admin.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志持久层实现
 * @Date 2014-12-29
 * @author 欧志辉
 * @version 1.0
 */
@Repository("logDaoImpl")
public class LogDaoImpl extends BaseDaoImpl<Log, Long> implements LogDao {

	public void removeAll() {
		String sql = "delete from xx_log";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		super.delete(sql, paramMap);
	}

}