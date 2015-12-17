package com.excel;

import java.io.File;
import java.util.Iterator;
import java.util.regex.PatternSyntaxException;

import org.apache.poi.hslf.record.ExVideoContainer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ReadExcel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String path="D://Deutch/papers/IEC2Vocabularies/Material/IECSeriesExport/IEC61360-CDD/AAA001Component/Properties/export_PROPERTY_DOMM-A4ZKJZ.xls";
		ExcelXLS excelUtil = new ExcelXLS(path);
		try {
			excelUtil.setExcelFile(0);
			excelUtil.parse();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}