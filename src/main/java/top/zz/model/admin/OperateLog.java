package top.zz.model.admin;

import cn.shengyuan.basic.dao.annotation.Entity;

import java.util.Date;

@Entity(name = "xx_operate_log")
public class OperateLog {
	/** ID */
	private Long id;

	/** 创建日期 */
	private Date createDate;

	/** 修改日期 */
	private Date modifyDate;

	/** 操作 */
	private String operation;

	/** 操作员 */
	private String operator;

	/** 操作内容 */
	private String content;

	/** IP */
	private String ip;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public OperateLog(Date createDate,Date modifyDate,String content,String ip,String operation,String operator) {
		this.createDate = createDate;
		this.modifyDate = modifyDate;
		this.content = content;
		this.ip = ip;
		this.operation = operation;
		this.operator = operator;
	}
	public OperateLog(String content,String ip,String operation,String operator) {
		this.createDate = new Date();
		this.modifyDate = new Date();
		this.content = content;
		this.ip = ip;
		this.operation = operation;
		this.operator = operator;
	}
}
