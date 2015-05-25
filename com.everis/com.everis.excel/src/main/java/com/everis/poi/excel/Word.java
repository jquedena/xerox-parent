package com.everis.poi.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import com.everis.util.FechaUtil;

public class Word {

    private static final Logger LOG = Logger.getLogger(Word.class);

    private static void setCell(XWPFTable table, String key, String value) {
        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell col : row.getTableCells()) {
                LOG.info("cell: " + col.getText());
                if (col.getText().equalsIgnoreCase(key)) {
                    LOG.info("runs: " + col.getParagraphs().get(0).getRuns().size());
                    for (XWPFRun c : col.getParagraphs().get(0).getRuns()) {
                        if (c.toString().equalsIgnoreCase(key)) {
                            c.setText(value, 0);
                            break;
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        // Create a new document from scratch
        XWPFDocument doc = new XWPFDocument();

        // Create a new table with 6 rows and 3 columns
        int nRows = 6;
        int nCols = 3;
        XWPFTable table = doc.createTable(nRows, nCols);

        // Set the table style. If the style is not defined, the table style
        // will become "Normal".
        CTTblPr tblPr = table.getCTTbl().getTblPr();

        tblPr.addNewTblW().setW(new BigInteger("9267"));
        tblPr.getTblW().setType(STTblWidth.AUTO);

        CTString styleStr = tblPr.addNewTblStyle();
        styleStr.setVal("StyledTable");

        // Get a list of the rows in the table
        List<XWPFTableRow> rows = table.getRows();
        int rowCt = 0;
        int colCt = 0;
        for (XWPFTableRow row : rows) {
            // get table row properties (trPr)
            CTTrPr trPr = row.getCtRow().addNewTrPr();
            // set row height; units = twentieth of a point, 360 = 0.25"
            CTHeight ht = trPr.addNewTrHeight();
            ht.setVal(BigInteger.valueOf(360));

            // get the cells in this row
            List<XWPFTableCell> cells = row.getTableCells();
            // add content to each cell
            for (XWPFTableCell cell : cells) {
                // get a table cell properties element (tcPr)
                CTTcPr tcpr = cell.getCTTc().addNewTcPr();
                // set vertical alignment to "center"
                CTVerticalJc va = tcpr.addNewVAlign();
                va.setVal(STVerticalJc.CENTER);

                // create cell color element
                CTShd ctshd = tcpr.addNewShd();
                ctshd.setColor("auto");
                ctshd.setVal(STShd.CLEAR);
                if (rowCt == 0) {
                    // header row
                    ctshd.setFill("A7BFDE");
                } else if (rowCt % 2 == 0) {
                    // even row
                    ctshd.setFill("D3DFEE");
                } else {
                    // odd row
                    ctshd.setFill("EDF2F8");
                }

                // get 1st paragraph in cell's paragraph list
                XWPFParagraph para = cell.getParagraphs().get(0);
                // create a run to contain the content
                XWPFRun rh = para.createRun();
                // style cell as desired
                if (colCt == nCols - 1) {
                    // last column is 10pt Courier
                    rh.setFontSize(10);
                    rh.setFontFamily("Courier");
                }
                if (rowCt == 0) {
                    // header row
                    rh.setText("header row, col " + colCt);
                    rh.setBold(true);
                    para.setAlignment(ParagraphAlignment.CENTER);
                } else if (rowCt % 2 == 0) {
                    // even row
                    rh.setText("row " + rowCt + ", col " + colCt);
                    para.setAlignment(ParagraphAlignment.LEFT);
                } else {
                    // odd row
                    rh.setText("row " + rowCt + ", col " + colCt);
                    para.setAlignment(ParagraphAlignment.LEFT);
                }
                colCt++;
            } // for cell
            colCt = 0;
            rowCt++;
        } // for row

        // write the file
        FileOutputStream out;
        try {
            out = new FileOutputStream("D:/styledTable.docx");
            doc.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            LOG.error("", e);
        } catch (IOException e) {
            LOG.error("", e);
        }

        try {
            FileInputStream fis = new FileInputStream("D:/T214-Template.docx");
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
            XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(xdoc);
            // read header
            XWPFHeader header = policy.getDefaultHeader();
            System.out.println(header.getText());
            // read footer
            XWPFFooter footer = policy.getDefaultFooter();

            setCell(xdoc.getTables().get(0), "${fecha}", FechaUtil.getAhoraString());
            setCell(xdoc.getTables().get(1), "${fecha}", FechaUtil.getAhoraString());

            System.out.println(xdoc.getParagraphs().size());
            for (XWPFParagraph c : xdoc.getParagraphs()) {
                LOG.debug(c.getText());
            }

            LOG.debug(footer.getTables().get(0).getRow(0).getCell(0).getText());
            LOG.debug("-***" + footer.getTables().get(0).getRow(0).getCell(0).getParagraphs().get(0).getRuns().size());

            for (XWPFRun c : footer.getTables().get(0).getRow(0).getCell(0).getParagraphs().get(0).getRuns()) {
                if (c.toString().equalsIgnoreCase("${nombreDocumento}")) {
                    c.setText("Hola Mundo ...........", 0);
                }

                LOG.debug("++++++++" + c.toString());
            }

            for (IBodyElement c : footer.getTables().get(0).getRow(0).getCell(0).getBodyElements()) {
                LOG.debug("---" + c.toString());
            }

            LOG.debug(footer.getTables().size());
            for (XWPFTable c : footer.getTables()) {
                LOG.debug(c.getText());
            }

            xdoc.write(new FileOutputStream(new File("D:/demo.docx")));

            LOG.debug(footer.getText());
        } catch (Exception ex) {
            LOG.error("", ex);
        }
    }

}
