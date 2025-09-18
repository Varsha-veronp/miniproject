package flipkart ;
import org.apache.poi.ss.usermodel.*;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

import java.io.IOException;

public class ExcelSet {

	private Workbook workbook;
	 
    private Sheet sheet;
 
    private int rowNum = 0;
 
    public ExcelSet() {
 
        this.workbook = new XSSFWorkbook(); 
 
        this.sheet = workbook.createSheet("Test Results");
 
        writeHeader();
 
    }
 
    private void writeHeader() {
 
        Row headerRow = sheet.createRow(rowNum++);
 
        String[] headers = {"Test Case", "Status", "Description"};
 
        for (int i = 0; i < headers.length; i++) {
 
            Cell cell = headerRow.createCell(i);
 
            cell.setCellValue(headers[i]);
 
            CellStyle headerStyle = workbook.createCellStyle();
 
            Font headerFont = workbook.createFont();
 
            headerFont.setBold(true);
 
            headerStyle.setFont(headerFont);
 
            cell.setCellStyle(headerStyle);
 
        }
 
    }
 
    public void writeData(String testCase, String status, String description) {
 
        Row dataRow = sheet.createRow(rowNum++);
 
        dataRow.createCell(0).setCellValue(testCase);
 
        dataRow.createCell(1).setCellValue(status);
 
        dataRow.createCell(2).setCellValue(description);
 
    }
 
    public void saveFile(String filePath) {
 
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
 
            workbook.write(outputStream);
 
            System.out.println("Excel report saved successfully to: " + filePath);
 
        } catch (IOException e) {
 
            System.err.println("Failed to save Excel file: " + e.getMessage());
 
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
