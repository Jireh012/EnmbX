import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelTest {
    public static void main(String[] args) throws IOException {
        File xlsFile = new File(Test.class.getResource("Source.xlsx").getPath());
        // 获得工作簿
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(xlsFile));
        // 获得工作表
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 1; i < rows; i++) {
            XSSFRow sheetRow = sheet.getRow(i);
            // 获取第0格数据
            XSSFCell source = sheetRow.getCell(0);
            XSSFCell aims = sheetRow.getCell(1);



        }
    }
}
