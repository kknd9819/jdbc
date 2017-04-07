package top.zz.dao.admin;


import top.zz.model.admin.OperateLog;

public interface OperateLogDao {
	
	/**保存操作日志*/
	public void saveOperateLog(OperateLog log);

}
