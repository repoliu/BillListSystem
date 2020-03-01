package com.chinaoly.util;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 *
 * 文档处理类
 *
 * */
public class WordTemplateUtil {

    //此方法中的代码无需改动，已调试过。如果不满足您的需求（那你随便玩）

    public static void replaceText(InputStream inputStream, OutputStream outputStream, Map<String, String> map) {
        try {
            XWPFDocument document = new XWPFDocument(inputStream);
            //1. 替换段落中的指定文字（模板中 对应的编号）
            Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
            String text;
            Set<String> set;
            XWPFParagraph paragraph;
            List<XWPFRun> run;
            String key;
            while (itPara.hasNext()) {
                paragraph = itPara.next();
                set = map.keySet();
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    run = paragraph.getRuns();
                    for (int i = 0, runSie = run.size(); i < runSie; i++) {
                        text = run.get(i).getText(run.get(i).getTextPosition());
                        if (text != null && text.equals(key)) {
                            run.get(i).setText(map.get(key), 0);
                        }
                    }
                }
            }
            //2. 替换表格中的指定文字（模板中 对应的姓名、性别等）
            Iterator<XWPFTable> itTable = document.getTablesIterator();
            XWPFTable table;
            int rowsCount;
            while (itTable.hasNext()) {
                XWPFParagraph p = document.createParagraph();
                XWPFRun headRun0 = p.createRun();
                table = itTable.next();
                rowsCount = table.getNumberOfRows();
                for (int i = 0; i < rowsCount; i++) {
                    XWPFTableRow row = table.getRow(i);
                    List<XWPFTableCell> cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {
                        for (Map.Entry<String, String> e : map.entrySet()) {
                            if (cell.getText().equals(e.getKey())) {
                                cell.removeParagraph(0);
                                // cell.setText(e.getValue());
                                //设置单元格文本样式
                                XWPFParagraph xwpfParagraph = cell.addParagraph();
                                XWPFRun run1 = xwpfParagraph.createRun();
                                run1.setFontSize(14);
                                run1.setText(e.getValue());
                                //设置内容水平居中
                                CTTc cttc = cell.getCTTc();
                                CTTcPr ctPr = cttc.addNewTcPr();
                                ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
                                /*  cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);*/
                            }
                        }
                    }
                }
            }
            //3.输出流
            document.write(outputStream);
//            System.out.println("shuchule");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
