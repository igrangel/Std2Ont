package com.excel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import com.ontologies.Ontology;

public final class XLS2HTML implements ActionListener, Callback {

	/**
	 * Public constants
	 */
	public static final String[] FILE_TYPES = new String[] { "xls", "xlsx" };

	/**
	 * Singleton object
	 */
	private static XLS2HTML instance = null;

	/**
	 * Java Swing components
	 */
	private JFrame frame;
	private JPanel panel;
	private JFileChooser fileChooser;
	private JButton openBtn, exitBtn;

	/**
	 * Internal constants
	 */
	private static final String CONTAINER_TITLE = "XLS to HTML Parser";
	private static final String OPEN_BTN_ACTIVE_TEXT = "Open an excel file";
	private static final String OPEN_BTN_INACTIVE_TEXT = "Parsing th XLS file...";
	private static final String EXIT_BTN_ACTIVE_TEXT = "Exit now";
	private static final String FILE_FILTER_TITLE = "All Microsoft EXCEL files (*.xlsx, *.xls)";
	private static final String GAP = "   ";

	private XLS2HTML() {
		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FileNameExtensionFilter(
				FILE_FILTER_TITLE, FILE_TYPES));
		openBtn = new JButton(OPEN_BTN_ACTIVE_TEXT);
		openBtn.addActionListener(this);
		exitBtn = new JButton(EXIT_BTN_ACTIVE_TEXT);
		exitBtn.addActionListener(this);

		panel = new JPanel();
		panel.add(openBtn);
		panel.add(new JLabel(GAP));
		panel.add(exitBtn);

		frame = new JFrame(CONTAINER_TITLE);
		frame.setContentPane(panel);
		frame.setSize(300, 90);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable (false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/**
	 * Singleton class get method
	 * 
	 * @return
	 */
	public static XLS2HTML getInstance() {
		if (instance == null) {
			instance = new XLS2HTML();
		}
		return instance;
	}

	private void setOpenBtnEnabled(boolean isEnabled) {
		openBtn.setEnabled(isEnabled);
		openBtn.setText(isEnabled ? OPEN_BTN_ACTIVE_TEXT
				: OPEN_BTN_INACTIVE_TEXT);
		frame.pack();
	}

	private void parseXLS() {
		setOpenBtnEnabled(false);
		XLS2HTMLParser parser = new XLS2HTMLParser(
				fileChooser.getSelectedFile(), this);
		new Thread(parser).start();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == openBtn) {
			int retVal = fileChooser.showOpenDialog(frame);
			if (retVal == JFileChooser.APPROVE_OPTION) {
				parseXLS();
			}
		} else if (ae.getSource() == exitBtn) {
			System.exit(0);
		}
	}

	@Override
	public void execute() {
		setOpenBtnEnabled(true);
	}

	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		XLS2HTML.getInstance();
	}

}

interface Callback {
	public void execute();
}

final class XLS2HTMLParser implements Runnable {

	private static final String NEW_LINE = "\n";
	private static final String HTML_FILE_EXTENSION = ".html";
	private static final String TEMP_FILE_EXTENSION = ".tmp";
	private static final String HTML_SNNIPET_1 = "<!DOCTYPE html><html><head><title>";
	private static final String HTML_SNNIPET_2 = "</title></head><body><table>";
	private static final String HTML_SNNIPET_3 = "</table></body></html>";
	private static final String HTML_TR_S = "<tr>";
	private static final String HTML_TR_E = "</tr>";
	private static final String HTML_TD_S = "<td>";
	private static final String HTML_TD_E = "</td>";
	public Workbook workbook;
	Sheet sheet;

	private File file;
	private Callback callback;

	XLS2HTMLParser(File file, Callback callback) {
		this.file = file;
		this.callback = callback;
	}

	public void run() {
		try {
			parse(file);
			callback.execute();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parse(File file) throws Exception {
		BufferedWriter writer;
		String fileName = file.getName();
		String folderName = file.getParent();
		if (fileName.toLowerCase().endsWith(XLS2HTML.FILE_TYPES[0])) {
			workbook = new HSSFWorkbook(new FileInputStream(file));
		} else {
			workbook = new XSSFWorkbook(new FileInputStream(file));
		}

		File tempFile = File.createTempFile(fileName + '-', HTML_FILE_EXTENSION
				+ TEMP_FILE_EXTENSION, new File(folderName));
		writer = new BufferedWriter(new FileWriter(tempFile));
		writer.write(HTML_SNNIPET_1);
		writer.write(fileName);
		writer.write(HTML_SNNIPET_2);

		sheet = workbook.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		Iterator<Cell> cells = null;
		
		// Obtaining the propertiesName by the Cell index
		ArrayList<String>propertiesName = new ArrayList<String>();
		propertiesName = getCellsByColRef("A14");

		Ontology o = new Ontology("http://www.semanticweb.org/ontologies/ont.owl");
		
		while (rows.hasNext()) {
			Row row = rows.next();
			
			cells = row.cellIterator();
			writer.write(NEW_LINE);
			writer.write(HTML_TR_S);
			int i = 0;
			OWLClass parentClass = null;
			while (cells.hasNext() && row.getRowNum() >= 27) {
				HSSFCell cell = (HSSFCell)cells.next();
				writer.write(HTML_TD_S);
				//System.out.println(propertiesName.get(i));
				//System.out.println(cell.toString() +"  ----   " + cell.getColumnIndex() + " \n");
				
	            String value = "";

	            switch (cell.getCellType()) {
	                case HSSFCell.CELL_TYPE_NUMERIC:
	                    value = BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
	                    break;

	                case HSSFCell.CELL_TYPE_STRING:
	                    value = cell.getStringCellValue();
	                    break;

	                case HSSFCell.CELL_TYPE_BLANK:
	                    value = "";
	                    break;
	            }
				
				String propertyName = propertiesName.get(i);
				
				switch (propertyName) {
				case "Code":
					parentClass = o.addClass(value);
					o.addRDFSLabel(parentClass, value, "en");
					break;
					
				case "PreferredName.EN":
					o.addSkosPrefLabel(parentClass, value, "en");
					break; 
					
				case "SynonymousName.EN":
					o.addSkosAltLabel(parentClass, value, "en");
					break; 		
					
				case "Definition.EN":
					o.addRDFSComment(parentClass, value, "en");
					break;	
				
				case "Superclass":
					if(!"".equals(value)){
						OWLClass cls = o.addClass(value);
						o.addSubclass(parentClass,cls);
					}
					break;	

				default:
					break;
				}
				
				/*if(propertiesName.get(i).equals("Code")){
					System.out.println(" SSS " + cell.toString() + "  AAA ");
					parentClass = o.addClass(cell.toString());
				}
				
				if(propertiesName.get(i).equals("PreferredName.EN")){
					o.addSkosPrefLabel(parentClass, cell.toString(), "en");
				}
				
				if(propertiesName.get(i).equals("Definition.EN")){
					o.addRDFSComment(parentClass, cell.toString(), "en");
				}
				
				if(propertiesName.get(i).equals("Superclass")){
					System.out.println(" SSS " + cell.toString() + "  AAA ");
					OWLClass cls = o.addClass(cell.toString());
					o.addSubclass(parentClass,cls);
				}*/
				 
				
				writer.write(cell.toString());
				//writer.write(cell.toString() + " - " + i + " - ");
				writer.write(HTML_TD_E);
				i++;
			}
			writer.write(HTML_TR_E);

		}
		writer.write(NEW_LINE);
		writer.write(HTML_SNNIPET_3);
		writer.close();
		File newFile = new File(folderName + '\\' + fileName + '-'
				+ System.currentTimeMillis() + HTML_FILE_EXTENSION);
		tempFile.renameTo(newFile);

		o.saveOntFormat(6);
	}
	
	/**
	 * Return the row of the first occurrence of a given string
	 * @param <String> obj
	 * @return <Row> 
	 */
	public Row getRowByString(String obj){
		sheet = workbook.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		Iterator<Cell> cells = null;
		Row toReturn = null;
		while (rows.hasNext()) {
			Row row = rows.next();
			cells = row.cellIterator();
			while (cells.hasNext()) {
				Cell cell = cells.next();
				if(cell.toString().contains(obj)) {
					toReturn = row;
					break;
				}
			}
		}
		return toReturn;
	}

	/**
	 * Return an string Array of the contents of a row
	 * @param cellRef
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getCellsByColRef(String cellRef){
		ArrayList<String> cellsContent = new ArrayList<String>();
		CellReference ref = new CellReference(cellRef);
		Row row = sheet.getRow(ref.getRow());
		if (row != null) {
			for (Cell cell : row) {
				cellsContent.add(cell.toString());
			}
		}
		//cellsContent.remove(0);
		return cellsContent;
	}

}
