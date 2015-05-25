package com.everis.poi.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.everis.util.NumeroUtil;

public class Excel implements Serializable {

    private static final long serialVersionUID = 1L;
    private transient Workbook workbook = null;

    public Excel(String fileName) throws InvalidFormatException, IOException {
        File file;
        FileInputStream input;

        file = new File(fileName);
        if (file.isFile() && file.exists()) {
            input = new FileInputStream(file);

            if (fileName.toLowerCase().lastIndexOf(".xlsx") > -1) {
                OPCPackage opcPackage = OPCPackage.open(input);
                this.setWorkbook(new XSSFWorkbook(opcPackage));
            } else {
                POIFSFileSystem poiFile = new POIFSFileSystem(input);
                this.setWorkbook(new HSSFWorkbook(poiFile, true));
            }
        } else {
            throw new FileNotFoundException("El archivo " + file.getPath() + " no existe");
        }
    }

    public Sheet getSheet(int index) {
        return this.getWorkbook().getSheetAt(index);
    }

    public Sheet getSheet(String sheetName) {
        Sheet sheet = this.getWorkbook().getSheet(sheetName);
        if (sheet == null) {
            sheet = this.getWorkbook().createSheet(sheetName);
        }

        return sheet;
    }

    public String getSheetName(int index) {
        Sheet sheet = this.getSheet(index);
        String sheetName = "";
        if (sheet != null) {
            sheetName = sheet.getSheetName();
        }

        return sheetName;
    }

    public Row getRow(int sheetIndex, int rowIndex) {
        Sheet sheet = null;
        Row row = null;

        sheet = this.getSheet(sheetIndex);
        if (sheet != null) {
            row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }
        }

        return row;
    }

    public Row getRow(String sheetName, int rowIndex) {
        Sheet sheet = null;
        Row row = null;

        sheet = this.getSheet(sheetName);
        if (sheet != null) {
            row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }
        }

        return row;
    }

    public Cell getCell(int sheetIndex, int rowIndex, int cellIndex) {
        Row row = null;
        Cell cell = null;

        row = this.getRow(sheetIndex, rowIndex);
        if (row != null) {
            cell = row.getCell(cellIndex);
            if (cell == null) {
                cell = row.createCell(cellIndex);
            }
        }

        return cell;
    }

    public Cell getCell(int sheetIndex, String cellRef) {
        CellReference cellReference = new CellReference(cellRef);
        return this.getCell(sheetIndex, cellReference.getRow(), cellReference.getCol());
    }

    public Cell getCell(String sheetName, int rowIndex, int cellIndex) {
        Row row = null;
        Cell cell = null;

        row = this.getRow(sheetName, rowIndex);
        if (row != null) {
            cell = row.getCell(cellIndex);
            if (cell == null) {
                cell = row.createCell(cellIndex);
            }
        }

        return cell;
    }

    public Cell getCell(String sheetName, String cellRef) {
        CellReference cellReference = new CellReference(cellRef);
        return this.getCell(sheetName, cellReference.getRow(), cellReference.getCol());
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public void setContent(int sheetIndex, int rowIndex, int colIndex, String content) {
        Cell cell;

        cell = this.getCell(sheetIndex, rowIndex, colIndex);
        if (cell != null) {
            cell.setCellValue(content);
        }
    }

    public void setContent(int sheetIndex, int rowIndex, int colIndex, double content) {
        Cell cell;

        cell = this.getCell(sheetIndex, rowIndex, colIndex);
        if (cell != null) {
            cell.setCellValue(content);
        }
    }

    public void setContent(int sheetIndex, String cellRef, double content) {
        Cell cell;
        String dataFormat;

        cell = this.getCell(sheetIndex, cellRef);
        if (cell != null) {
            dataFormat = cell.getCellStyle().getDataFormatString();
            if ("0.00%".equalsIgnoreCase(dataFormat) || "0%".equalsIgnoreCase(dataFormat)) {
                cell.setCellValue(content / 100);
            } else {
                cell.setCellValue(content);
            }
        }
    }

    public void setContent(long sheetIndex, String cellRef, double content) {
        int index = Integer.parseInt(String.valueOf(sheetIndex));
        this.setContent(index, cellRef, content);
    }

    public void setContent(int sheetIndex, String cellRef, String content) {
        Cell cell;

        cell = this.getCell(sheetIndex, cellRef);
        if (cell != null) {
            cell.setCellValue(content);
        }
    }

    public void setContent(long sheetIndex, String cellRef, String content) {
        int index = Integer.parseInt(String.valueOf(sheetIndex));
        this.setContent(index, cellRef, content);
    }

    public String getContentString(String sheetName, String cellRef) {
        return getContentString(sheetName, cellRef, null);
    }

    public String getContentString(String sheetName, String cellRef, String format) {
        String result = "";
        Cell cell = this.getCell(sheetName, cellRef);
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            result = cell.getStringCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            if (format != null && !format.isEmpty()) {
                result = NumeroUtil.format(BigDecimal.valueOf(cell.getNumericCellValue()), format);
            } else {
                result = String.valueOf(cell.getNumericCellValue());
            }
        }

        return result;
    }

    public Double getContentNumber(String sheetName, String cellRef) {
        Double result = null;
        Cell cell = this.getCell(sheetName, cellRef);
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            result = cell.getNumericCellValue();
        }

        return result;
    }

    public Date getContentDate(String sheetName, String cellRef) {
        Date result = null;
        Cell cell = this.getCell(sheetName, cellRef);
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            result = DateUtil.getJavaDate(cell.getNumericCellValue());
        }

        return result;
    }

    public CellValue getContentFormula(String sheetName, String cellRef) {
        CellValue result = null;
        FormulaEvaluator evaluator = this.getWorkbook().getCreationHelper().createFormulaEvaluator();
        Cell cell = this.getCell(sheetName, cellRef);

        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            result = evaluator.evaluate(cell);
        }

        return result;
    }

    public void saveAs(String fileName) throws IOException {
        saveAs(new File(fileName));
    }

    public void saveAs(File file) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(file);
        this.getWorkbook().write(fileOut);
        fileOut.close();
    }

    public InputStream toInputStream() throws IOException {
        return new ByteArrayInputStream(toByteArray());
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.getWorkbook().write(baos);
        return baos.toByteArray();
    }

    public void recalculateFormula() {
        this.getWorkbook().getCreationHelper().createFormulaEvaluator().evaluateAll();
    }

    public void recalculateFormulaOpen() {
        this.getWorkbook().setForceFormulaRecalculation(true);
    }
}
