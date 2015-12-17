package com.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Class to manage XSLX excel files
 * @author Irlan
 *
 */
public class ExcelXLSX {
	private static XSSFSheet excelWSheet;
	private static XSSFWorkbook excelWBook;
	private static XSSFCell cell;
	private static XSSFRow row;
	private String path;
	
	public ExcelXLSX(String path) {
		super();
		this.path = path;
	}

	/**
	 * @param Path
	 * @param sheetNumber
	 * @throws Exception
	 */
	public void setExcelFile(int sheetNumber) throws Exception {
		File file = new File(this.path);
		FileInputStream excelFile = new FileInputStream(file);
		excelWBook = new XSSFWorkbook(excelFile);
		excelWSheet = excelWBook.getSheetAt(sheetNumber);
	}

	/**
	 * @param RowNum
	 * @param ColNum
	 * @return
	 * @throws Exception
	 */
	public static String getCellData(int RowNum, int ColNum) {
		cell = excelWSheet.getRow(RowNum).getCell(ColNum);
		String CellData = cell.getStringCellValue();
		return CellData;
	}
	
	/**
	 * @param Result
	 * @param RowNum
	 * @param ColNum
	 * @param Path
	 * @throws Exception
	 */
	public static void setCellData(String Result, int RowNum, int ColNum, File Path) throws Exception {
		try {
			row = excelWSheet.createRow(RowNum - 1);
			cell = row.createCell(ColNum - 1);
			cell.setCellValue(Result);
			FileOutputStream fileOut = new FileOutputStream(Path);
			excelWBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (Exception e) {
			throw (e);
		}
	}

}
