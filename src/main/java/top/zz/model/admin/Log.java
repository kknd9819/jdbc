package top.zz.model.admin;

import cn.shengyuan.basic.dao.annotation.Entity;
import cn.shengyuan.basic.dao.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 配送员实体
 * @Date 2015-1-16
 * @author 欧志辉
 * @version 1.0
 */
@Entity(name = "xx_log")
public class Log implements Serializable {

	private static final long serialVersionUID = -4494144902110236826L;

	/** "日志内容"属性名称 */
	public static final String LOG_CONTENT_ATTRIBUTE_NAME = Log.class.getName() + ".CONTENT";
	
	@Id
	private Long id;
	
	/** 创建时间 */
	private Date createDate;
	
	/** 修改时间 */
	private Date modifyDate;
	
	/** 操作 */
	private String operation;

	/** 操作员 */
	private String operator;

	/** 内容 */
	private String content;

	/** 请求参数 */
	private String parameter;

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

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}