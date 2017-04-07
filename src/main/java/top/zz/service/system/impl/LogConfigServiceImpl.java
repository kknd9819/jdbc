package top.zz.service.system.impl;


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import top.zz.model.admin.vo.LogConfig;
import top.zz.service.system.LogConfigService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 日志配置服务层实现
 * @Date 2014-12-30
 * @author 欧志辉
 * @version 1.0
 */
@Service("logConfigServiceImpl")
public class LogConfigServiceImpl implements LogConfigService {

	@SuppressWarnings("unchecked")
	@Cacheable("logConfig")
	public List<LogConfig> getAll() {
		try {
			File logConfigXmlFile = new ClassPathResource(LogConfig.LOG_CONFIG_XML_PATH).getFile();
			Document document = new SAXReader().read(logConfigXmlFile);
			List<Element> elements = document.selectNodes("/logConfigs/logConfig");
			List<LogConfig> logConfigs = new ArrayList<LogConfig>();
			for (Element element : elements) {
				String operation = element.attributeValue("operation");
				String urlPattern = element.attributeValue("urlPattern");
				LogConfig logConfig = new LogConfig();
				logConfig.setOperation(operation);
				logConfig.setUrlPattern(urlPattern);
				logConfigs.add(logConfig);
			}
			return logConfigs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}