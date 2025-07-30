package org.example;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {

    public Properties Main() throws IOException {
        InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties");
        Properties prop = new Properties();
        prop.load(input);
        return prop;
    }



    public static void main(String[] args) throws IOException, InterruptedException {
        Main obj = new Main();
        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;
        fh = new FileHandler("logfile.path");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        logger.setUseParentHandlers(false);

        /* Start - Commenting the below code which is created for fetching the Angular package verison details
        String nodeExecutable = "node";
        String scriptPath = "C://Users//yravichandran//Downloads//ReadExcel//ReadExcel.js";

        ProcessBuilder processBuilder = new ProcessBuilder(nodeExecutable, scriptPath);
        Process process = processBuilder.start();

        // Read standard output
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));


        // Read error output
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String errorLine;
        while ((errorLine = errorReader.readLine()) != null) {
            System.err.println("ERROR: " + errorLine);
        }

        // Wait for the process to complete
        int exitCode = process.waitFor();
        System.out.println("Node.js script exited with code: " + exitCode);

        String line;
        String packageResponse [];
        Map<String, String> angularPackageMap = new HashMap<>();
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            packageResponse = line.split( ":");
            obj.WriteExcelUI(packageResponse , logger);
            packageResponse = null;
        }

        */ //Start - Commenting the below code which is created for fetching the Angular package verison details

        String DriverPAth = obj.Main().getProperty("ChromeDriver.path");

        System.setProperty("webdriver.chrome.driver", DriverPAth);
       // System.setProperty("webdriver.chrome.driver", "C:\\Users\\yravichandran\\Downloads\\chromedriver_NEW\\chromedriver-win64\\chromedriver.exe");

        // Instantiate a ChromeDriver class.



        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--remote-allow-origins=*");



// Access the properties
      //  String fileinput = object.Main().getProperty("inputfile.path");


        FileInputStream fs = new FileInputStream(obj.Main().getProperty("inputfile.path"));
//Creating a workbook
        XSSFWorkbook workbook = new XSSFWorkbook(fs);
        XSSFSheet sheet = workbook.getSheetAt(0);
        //logger.info(sheet.getLastRowNum() + "Last line");
        Row Excelrow = sheet.getRow(1);
        //Cell cell = Excelrow.getCell(0);
        Cell Excelcell = Excelrow.getCell(0);
       // logger.info(sheet.getRow(1).getCell(0));
        int rowLength = sheet.getLastRowNum();
        for(int a=1; a<= sheet.getLastRowNum();a++){
            WebDriver driver=new ChromeDriver(chromeOptions);
            String s= String.valueOf(sheet.getRow(a).getCell(0));
            String g= String.valueOf(sheet.getRow(a).getCell(1));
            logger.info(s + "-->" + g + " ");
            
           // driver.get("https://mvnrepository.com/artifact/com.google.guava/guava");
            //driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

            try{
                if(s != null && s != " " &  s.equalsIgnoreCase("Saxon-EE"))
                {
                        g = "com.saxonica";
                    logger.info( "Group Id amended for Saxon-EE --> " + s + "-->" + g + " ");
                }

                driver.get("https://mvnrepository.com/artifact/"+ g +"/" + s );
                WebElement table = driver.findElement(By.className("tab_container")); // Replace with actual class name
                // Locate all rows in the table
                List<WebElement> rows = table.findElements(By.tagName("tr"));
            /*WebDriverWait wait = new WebDriverWait(driver , Duration.ofSeconds(1));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("fc-button-label")));
*/
                Thread.sleep(1000);
                if(driver.findElements(By.className("fc-button-label")).size() > 0){
                    logger.info(" Element found ");
                    driver.findElement(By.className("fc-button-label")).click();
                }

                String version = "";
                String Previousversion = "";
                String vul= "";
                String VulnerableFreeVerison = "";
                // Loop through each row
                boolean flag = true;
                int LatestVersionCount=0;
                String LatestVersionStr = "";
                String LatestVersionDate = "";
                //      logger.info("Row count  -->" + rows.size());
                for (WebElement row : rows) {
                    // Locate all cells in the row
                    List<WebElement> cells = row.findElements(By.tagName("td"));

                    //   logger.info("Row cell count  -->" + cells.size());
                    // Loop through each cell
                    int RowCellCount = cells.size();
                    int count = 1;
                    if (flag){

                        Previousversion = version;
                        for (WebElement cell : cells) {
                            //                logger.info("Row cell value-->" + cell.getText());
                            if(RowCellCount == 5){
                                if(LatestVersionCount ==1 && count==1){
                                    LatestVersionStr = cell.getText();
                                    //  logger.info("TEst  -->" + LatestVersionStr);
                                }
                            } else {
                                if(LatestVersionCount ==1 && count==2){
                                    LatestVersionStr = cell.getText();
                                    //  logger.info("TEst  -->" + LatestVersionStr);
                                }
                            }



                            if(LatestVersionCount ==1 && count== cells.size()){
                                LatestVersionDate = cell.getText();
                                if(LatestVersionCount == 1){
                                    break;
                                }
                            }
                            // Fetch and print the cell value


                            //        logger.info(version);
                            String cellValue=cell.getText();
                            if(flag && (cellValue.contains("vulnerabilities") || cellValue.contains("vulnerability")  )){
                                vul=cell.getText();
                                flag = false;
                                if(LatestVersionCount > 1){
                                    break;
                                }

                            }

                            if(RowCellCount == 5){
                                if (count==1 && flag){
                                    version = cell.getText();
                                }
                            } else {
                                if (count==2 && flag){
                                    version = cell.getText();
                                }
                            }



                            count = count + 1;



                            String cellText = cell.getText();
                            //      logger.info(cellText + "test");
                        }
                    }


                    LatestVersionCount ++;
                    // Optionally, print a new line after each row for better readability
                    //logger.info( "Last out");
                }

                logger.info("version free vulnerable -->" + ((Previousversion == null || Previousversion == "") ? "NA" : Previousversion ) );
                logger.info("Latest version -->" + LatestVersionStr);
                logger.info("Latest version date -->" + LatestVersionDate);


                obj.WriteExcel(LatestVersionStr, Previousversion ,LatestVersionDate , a , logger);

                driver.quit();
            }catch (Exception e) {
                logger.warning(e.getMessage());
                logger.warning("https://mvnrepository.com/artifact/"+ g +"/" + s + "---> Internal lib skipping it");
                driver.quit();

            }




        }



        String artifact_id = "gson";
        String group_id = "com.google.code.gson";
       // driver.get("https://mvnrepository.com/artifact/"+ group_id +"/" + artifact_id);



        /*
        driver.getTitle();

        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        WebElement textBox = driver.findElement(By.name("my-text"));
        WebElement submitButton = driver.findElement(By.cssSelector("button"));

        textBox.sendKeys("Selenium");
        submitButton.click();

        WebElement message = driver.findElement(By.id("message"));
        message.getText();
        */

    }

    public  void WriteExcel(String LatestVersion , String VulfreeVersion, String LatestVersionDate,  int row , Logger logger) throws IOException {
       // Main().getProperty("outputfile.path");
        String path = Main().getProperty("outputfile.path");
       try {
           FileInputStream fs = new FileInputStream(path);
           Workbook wb = new XSSFWorkbook(fs);
           Sheet sheet1 = wb.getSheetAt(0);
           int lastRow = sheet1.getLastRowNum();
           // Row row = sheet1.getRow(row);
           Cell cell = sheet1.getRow(row).createCell(4);

           cell.setCellValue(LatestVersion);
           if(VulfreeVersion == "" || VulfreeVersion == null || VulfreeVersion.length() == 0){
                sheet1.getRow(row).createCell(3).setCellValue("NA");
           } else {
               sheet1.getRow(row).createCell(3).setCellValue(VulfreeVersion);
           }
           sheet1.getRow(row).createCell(5).setCellValue(LatestVersionDate);

           FileOutputStream fos = new FileOutputStream(path);
           wb.write(fos);
           fos.close();
       }
       catch(Exception e){
           //e.getMessage();
           logger.info(e.getMessage());
           logger.info("Close the output file for continue report creation.");
       }
    }

    public  void WriteExcelUI( String [] angularResponse ,  Logger logger) throws IOException {
        // Main().getProperty("outputfile.path");
        String path = Main().getProperty("outputfileUI.path");
        try {
            FileInputStream fs = new FileInputStream(path);
            Workbook wb = new XSSFWorkbook(fs);
            Sheet sheet1 = wb.getSheetAt(0);
            /*Below block will read the excel sheet and compare the input with angularResponse Map and
             takes the values from response object; set the value in corresponding cell in excel sheet */
            for(int a=1; a<= sheet1.getLastRowNum();a++) { //a=1 will skipping header
                String s = String.valueOf(sheet1.getRow(a).getCell(0));
                    if(s.contains(angularResponse[0])){
                        sheet1.getRow(a).createCell(1).setCellValue(angularResponse[1]);
                    }
            }


           /* if(VulfreeVersion == "" || VulfreeVersion == null || VulfreeVersion.length() == 0){
                sheet1.getRow(row).createCell(3).setCellValue("NA");
            } else {
                sheet1.getRow(row).createCell(3).setCellValue(VulfreeVersion);
            }
            sheet1.getRow(row).createCell(5).setCellValue(LatestVersionDate);*/

            FileOutputStream fos = new FileOutputStream(path);
            wb.write(fos);
            fos.close();
        }
        catch(Exception e){
            e.getMessage();
            logger.info(e.getMessage());
            logger.info("Close the output file for continue report creation.");
        }
    }
}