package top.zz.service.system.impl;


import cn.shengyuan.basic.model.Page;
import cn.shengyuan.basic.service.impl.BaseServiceImpl;
import cn.shengyuan.tools.util.StringUtil;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import top.zz.dao.admin.AdminDao;
import top.zz.dao.admin.AdminRoleDao;
import top.zz.model.admin.Admin;
import top.zz.model.admin.AdminRole;
import top.zz.model.admin.vo.AdminRoleAuthority;
import top.zz.model.admin.vo.Principal;
import top.zz.service.system.AdminService;
import top.zz.util.Pageable;

import javax.annotation.Resource;
import java.util.*;

/**
 * 后台管理员服务层实现
 * @Date 2014-12-29
 * @author 欧志辉
 * @version 1.0
 */
@Service("adminServiceImpl")
public class AdminServiceImpl extends BaseServiceImpl<Admin, Long> implements AdminService {
	
	@Resource(name = "adminDaoImpl")
	private AdminDao adminDao;

	@Resource(name = "adminRoleDaoImpl")
	private AdminRoleDao adminRoleDao;
	
	@Resource(name = "adminDaoImpl")
	public void setAdminDao(AdminDao adminDao) {
		super.setDao(adminDao);
	}
	
	@Override
	public boolean usernameExists(String username) {
		List<Admin> list = adminDao.findByUsername(username);
		return list != null && list.size() > 0;
	}

	@Override
	public Admin findByUsername(String username) {
		List<Admin> list = adminDao.findByUsername(username);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public List<String> findAuthorities(Long id) {
		List<AdminRoleAuthority> list = adminDao.findAuthoritys(id);
		List<String> authorities = new ArrayList<String>();
		for (AdminRoleAuthority adminRoleAuthority : list) {
			authorities.add(adminRoleAuthority.getAuthority());
		}
		return authorities;
	}
	
	@Override
	public boolean isAuthenticated() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			return subject.isAuthenticated();
		}
		return false;
	}
	
	@Override
	public Admin getCurrent() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			Principal principal = (Principal) subject.getPrincipal();
			if (principal != null) {
				return adminDao.get(principal.getId());
			}
		}
		return null;
	}

	@Override
	public String getCurrentUsername() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			Principal principal = (Principal) subject.getPrincipal();
			if (principal != null) {
				return principal.getUsername();
			}
		}
		return null;
	}
	
	@Override
	public String getCurrentStoreCode() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			Principal principal = (Principal) subject.getPrincipal();
			if (principal != null) {				
				Admin admin =  adminDao.get(principal.getId());
				if (admin != null && admin.getmId() != null) {
					return admin.getmId();
				}
				return "";
			}
		}
		return "";
	}

	@Override
	public List<Admin> findCustomServiceAdmins() {
		
		List<String> roleCodes = new ArrayList<String>();
		roleCodes.add("KFZZG");
		roleCodes.add("SQKF");
		roleCodes.add("SHKF");
		return adminDao.findByRoleCode(roleCodes);
	}

	@Override
	public Long saveAdmin(Admin admin, Long[] roleIds) {
		admin.setCreateDate(new Date());
		admin.setModifyDate(new Date());
		Long adminId = adminDao.save(admin);
		List<AdminRole> adminRoles = new ArrayList<AdminRole>();
		for (Long roleId : roleIds) {
			AdminRole adminRole = new AdminRole();
			adminRole.setAdminId(adminId);
			adminRole.setRoleId(roleId);
			adminRoles.add(adminRole);
		}
		adminRoleDao.batchSaveAdminRole(adminRoles);
		return adminId;
	}

	@Override
	public void updateAdmin(Admin admin, Long[] roleIds) {
		admin.setModifyDate(new Date());
		adminDao.update(admin);
		if (roleIds != null) {
			adminRoleDao.deleteByAdminId(admin.getId());
			List<AdminRole> adminRoles = new ArrayList<AdminRole>();
			for (Long roleId : roleIds) {
				AdminRole adminRole = new AdminRole();
				adminRole.setAdminId(admin.getId());
				adminRole.setRoleId(roleId);
				adminRoles.add(adminRole);
			}
			adminRoleDao.batchSaveAdminRole(adminRoles);
		}
	}

	@Override
	public void batchDelete(List<Admin> admins) {
		adminDao.batchDelete(admins);
	}
	
	@Override
	public Page<Admin> findPage(Pageable pageable) {
		int pageSize = pageable.getPageSize();
		int pageNo = pageable.getPageNumber();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(pageable.getSearchValue())) {
			paramMap.put(pageable.getSearchProperty(), pageable.getSearchValue());
		}
		return adminDao.queryForPage(pageSize, pageNo, paramMap);
	}

}