package com.excel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Class to manage XSLX excel files
 * @author Irlan
 *
 */
public class ExcelXLS {
	
	public static final String[] FILE_TYPES = new String[] { "xls", "xlsx" };
	
	private Sheet sheet;
	private static Workbook excelWBook;
	private static Cell cell;
	private static Row row;
	private String path;

	public ExcelXLS(String path) {
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
		excelWBook = WorkbookFactory.create(excelFile);
		sheet = excelWBook.getSheetAt(sheetNumber);

	}

	/**
	 * @param RowNum
	 * @param ColNum
	 * @return
	 * @throws Exception
	 */
	public String getCellData(int RowNum, int ColNum) {
		cell = sheet.getRow(RowNum).getCell(ColNum);
		String CellData = cell.getStringCellValue();
		return CellData;
	}

    /**
     * @throws IOException 
     * 
     */
	public void readingData() throws IOException{
		Iterator rows = sheet.rowIterator();

		while (rows.hasNext())
		{
			row = (HSSFRow) rows.next();
			Iterator cells = row.cellIterator();

			while (cells.hasNext())
			{
				cell = (HSSFCell) cells.next();

				if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
					System.out.print(cell.getStringCellValue() + " ");
					
				}
				else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
					System.out.print(cell.getNumericCellValue() + " ");
				}
				FileWriter writer = new FileWriter("D://Deutch/papers/IEC2Vocabularies/Material/IECSeriesExport/IEC61360-CDD/AAA001Component/Properties/a.cvs");
			}
			System.out.println();
		}
	}


	public void parse() throws FileNotFoundException, IOException {
		BufferedWriter writer;
		Workbook workbook;
		if (path.toLowerCase().endsWith(FILE_TYPES[0])) {
			workbook = new HSSFWorkbook(new FileInputStream(path));
		} else {
			workbook = new XSSFWorkbook(new FileInputStream(path));
		}

		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		Iterator<Cell> cells = null;
		while (rows.hasNext()) {
			Row row = rows.next();
			cells = row.cellIterator();
			while (cells.hasNext()) {
				Cell cell = cells.next();
				System.out.println(cell.toString() + row.getRowNum());
			}
		}
	}

}
