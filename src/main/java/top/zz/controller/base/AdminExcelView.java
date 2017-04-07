package top.zz.controller.base;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

public class AdminExcelView extends AbstractExcelView {
	/** 导出文件名称 */
	private String filename;

	/** 模板路径(相对根路径) */
	private String templateUrl;

	/** 结果集<行-列,数据> */
	private Map<String, Object> data;

	public AdminExcelView(String filename, String templateUrl, Map<String, Object> data) {
		this.filename = filename;
		this.templateUrl = templateUrl;
		this.data = data;
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 读取模板
		String path = request.getSession().getServletContext().getRealPath("/") + templateUrl;
		File fi = new File(path);
		// 创建文件
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fi));
		// 得到表格
		workbook = new HSSFWorkbook(fs);

		HSSFSheet sheet = workbook.getSheetAt(0);
		// 插入值
		for (Entry<String, Object> entry : data.entrySet()) {
			String key = entry.getKey();
			if (null == entry.getValue()) {
				data.put(key, " ");
			}
			String value = entry.getValue().toString();
			String[] array = key.split("-");
			int row = Integer.valueOf(array[0]) - 1;
			int cell = Integer.valueOf(array[1]) - 1;
			if (sheet.getRow(row) == null) {
				sheet.createRow(row).createCell(cell).setCellValue(value);
			} else if (sheet.getRow(row).getCell(cell) == null) {
				sheet.getRow(row).createCell(cell).setCellValue(value);
			} else {
				sheet.getRow(row).getCell(cell).setCellValue(value);
			}
		}
		response.setContentType("application/force-download");
		if (StringUtils.isNotEmpty(filename)) {
			response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
		} else {
			response.setHeader("Content-disposition", "attachment");
		}
	}

}
