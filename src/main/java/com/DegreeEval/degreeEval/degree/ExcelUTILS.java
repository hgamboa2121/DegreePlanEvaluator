package com.DegreeEval.degreeEval.degree;

import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUTILS {

    static XSSFWorkbook workbook;
    static XSSFSheet sheet;

    public ExcelUTILS(String excelPath) {
        try {
            workbook = new XSSFWorkbook(excelPath);
            sheet = workbook.getSheet("Sheet1");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public Object getCellData(int row, int col) throws IOException{
        DataFormatter formatter = new DataFormatter();
        Object value = formatter.formatCellValue(sheet.getRow(row).getCell(col));
        return value;

    }
    public static int getRowCount() {
        int rowCount = sheet.getLastRowNum() + 1;
        return rowCount;
    }

}

