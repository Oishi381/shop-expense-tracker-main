package com.example.shopexpensetracker;

import com.example.shopexpensetracker.Models.Employee;
import com.example.shopexpensetracker.Models.Product;
import com.example.shopexpensetracker.Models.ProductCoupon;
import com.example.shopexpensetracker.Models.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.format.CellDateFormatter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Common {
    public static SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    public static ObservableList<Product> getAllProduct() throws IOException {
        File file = new File("src/main/resources/data/Product.xlsx");
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        System.out.println(sheet);
        System.out.println(workbook);

        ObservableList<Product> products = FXCollections.observableArrayList();
        int rowCount = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rowCount; i++) {
            XSSFRow row = sheet.getRow(i);
            int ProductID = (int) row.getCell(0).getNumericCellValue();
            String ProductName = row.getCell(1).getStringCellValue();
            double ProductPrice = row.getCell(2).getNumericCellValue();
            int ProductStock = (int) row.getCell(3).getNumericCellValue();
            products.add(new Product(ProductID,ProductName,ProductPrice,ProductStock));
        }

        fis.close();
        return products;
    }

    public static ObservableList<ProductCoupon> getAllProductCoupon() throws IOException {
        File file = new File("src/main/resources/data/Product.xlsx");
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        System.out.println(sheet);
        System.out.println(workbook);

        ObservableList<ProductCoupon> products = FXCollections.observableArrayList();
        int rowCount = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rowCount; i++) {
            XSSFRow row = sheet.getRow(i);
            int ProductID = (int) row.getCell(0).getNumericCellValue();
            String ProductName = row.getCell(1).getStringCellValue();
            double ProductPrice = row.getCell(2).getNumericCellValue();
            int ProductStock = (int) row.getCell(3).getNumericCellValue();
            try{
                String couponCode = row.getCell(4).getStringCellValue();
                double discountPercentage = row.getCell(5).getNumericCellValue();
                if(!Objects.equals(couponCode, "") && discountPercentage != 0){
                    products.add(new ProductCoupon(couponCode,discountPercentage,new Product(ProductID,ProductName,ProductPrice,ProductStock)));
                }
            }catch (NullPointerException ignored){}
        }

        fis.close();
        return products;
    }

    public static ObservableList<Report> getReport() throws IOException, ParseException {
        String date = formatter.format(new Date(0L));
        return getReport(date);
    }
    public static ObservableList<Report> getReport(String dateLimit) throws IOException, ParseException {
        System.out.println("dateLimit");
        System.out.println(dateLimit);
        File file = new File("src/main/resources/data/Report.xlsx");
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheet("report-sheet");
        System.out.println(sheet);
        System.out.println(workbook);

        ObservableList<Report> reports = FXCollections.observableArrayList();
        int rowCount = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rowCount; i++) {
            XSSFRow row = sheet.getRow(i);
            String date = new CellDateFormatter("MM/dd/yyyy").format(row.getCell(0).getDateCellValue());
            String reportTitle = row.getCell(1).getStringCellValue();
            double reportPrice = row.getCell(2).getNumericCellValue();
            Date recordDate = formatter.parse(date);
            Date limitDate = formatter.parse(dateLimit);
            if(!recordDate.before(limitDate)){
                reports.add(new Report(date,reportTitle,reportPrice));
            }
        }

        fis.close();
        return reports;
    }
    public static void addReport( String reportTitle, double reportAmount) throws IOException {
        reportAmount= Math.round(reportAmount * 10000) / 10000.0;
        Date now = new Date();
        File file = new File("src/main/resources/data/Report.xlsx");
        System.out.println(file.getAbsolutePath());

        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        System.out.println(workbook);
        XSSFSheet sheet = workbook.getSheet("report-sheet");
        System.out.println(sheet);

        int lastRow = sheet.getLastRowNum();
        System.out.println(lastRow);
        System.out.println(now);
        System.out.println(reportTitle);
        System.out.println(reportAmount);

        XSSFRow row = sheet.createRow(lastRow + 1) ;
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat((short)14);
        row.createCell(0).setCellStyle(cellStyle);
        row.getCell(0).setCellValue(now);
        row.createCell(1).setCellValue(reportTitle);
        row.createCell(2).setCellValue(reportAmount);

        fis.close();

        //Creating output stream and writing the updated workbook
        FileOutputStream os = new FileOutputStream(file);
        workbook.write(os);

        //Close the workbook and output stream
        workbook.close();
        os.close();

    }

    public static XSSFRow isPresent(XSSFSheet sheet, String ProductName, Integer colNoToSearch){
        int rowCount = sheet.getPhysicalNumberOfRows();
        for (int i = 1; i < rowCount; i++) {
            XSSFRow row = sheet.getRow(i);
            String nameToFind = row.getCell(colNoToSearch).getStringCellValue();

            if (Objects.equals(ProductName.toLowerCase().trim(), nameToFind.toLowerCase().trim())){
                System.out.println("Returning from");
                System.out.println(i);
                return row;
            }
        }
        return null;
    }
    public static void sellProduct(String productType, String productName, int remainStock) throws IOException {
        String filePath = "";
        if(Objects.equals(productType, "Product")){
            filePath = "src/main/resources/data/Product.xlsx";
        }else if(Objects.equals(productType, "ThirdPartyProduct")){
            filePath = "src/main/resources/data/ThirdPartyProduct.xlsx";
        }

        File file = new File(filePath);
        System.out.println(file.getAbsolutePath());

        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        System.out.println(workbook);
        XSSFSheet sheet = workbook.getSheetAt(0);
        System.out.println(sheet);

        XSSFRow productIsPresent =  Common.isPresent(sheet,productName,1);
        assert productIsPresent != null;
        productIsPresent.getCell(3).setCellValue(remainStock);

        fis.close();

        FileOutputStream os = new FileOutputStream(file);
        workbook.write(os);

        //Close the workbook and output stream
        workbook.close();
        os.close();
        System.out.println("Product excel file has been updated successfully.");
    }

    public static Employee isUserValid(String userType, String email, String password) throws IOException, ParseException {
        String filePath = "src/main/resources/data/" + userType + ".xlsx";
        File file = new File(filePath);
        System.out.println(file.getAbsolutePath());

        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        System.out.println(workbook);
        XSSFSheet sheet = workbook.getSheetAt(0);
        System.out.println(sheet);
        XSSFRow employeeIsPresent =  Common.isPresent(sheet,email,2);
        if(employeeIsPresent!=null){
            String employeePassword = employeeIsPresent.getCell(4).getStringCellValue();
            if(Objects.equals(employeePassword, password)){
                System.out.println("User Found");
                int employeeID = (int) employeeIsPresent.getCell(0).getNumericCellValue();
                String lastPaidDate = new CellDateFormatter("MM/dd/yyyy").format(employeeIsPresent.getCell(1).getDateCellValue());
                String employeeEmail = employeeIsPresent.getCell(2).getStringCellValue();
                String employeeName = employeeIsPresent.getCell(3).getStringCellValue();
                Double employeeBalance = employeeIsPresent.getCell(5).getNumericCellValue();

                return new Employee(employeeID,lastPaidDate,employeeEmail,employeeName,employeePassword,employeeBalance);
            }
        }
        return null;
    }

    public static void deleteProductCoupon(String ProductName) throws IOException {
        File file = new File("src/main/resources/data/Product.xlsx");
        System.out.println(file.getAbsolutePath());

        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        System.out.println(workbook);
        XSSFSheet sheet = workbook.getSheetAt(0);
        System.out.println(sheet);
        XSSFRow productIsPresent =  Common.isPresent(sheet,ProductName,1);
        if(productIsPresent != null){
            productIsPresent.getCell(4).setCellValue((String) null);
            productIsPresent.getCell(5).setCellValue((RichTextString) null);
        }

        fis.close();

        //Creating output stream and writing the updated workbook
        FileOutputStream os = new FileOutputStream(file);
        workbook.write(os);

        //Close the workbook and output stream
        workbook.close();
        os.close();
    }

    public static double reduceSalary(Employee employee, double finalPrice) throws IOException {
        File file = new File("src/main/resources/data/Employee.xlsx");
        System.out.println(file.getAbsolutePath());
        double newBalance = employee.getBalance();
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        System.out.println(workbook);
        XSSFSheet sheet = workbook.getSheetAt(0);
        System.out.println(sheet);
        XSSFRow employeeIsPresent =  Common.isPresent(sheet,employee.getEmployeeEmail(),2);

        if(employeeIsPresent != null){
            double previousBalance = employeeIsPresent.getCell(5).getNumericCellValue();
            newBalance = previousBalance - finalPrice;
            employeeIsPresent.getCell(5).setCellValue(newBalance);
        }

        fis.close();

        //Creating output stream and writing the updated workbook
        FileOutputStream os = new FileOutputStream(file);
        workbook.write(os);

        //Close the workbook and output stream
        workbook.close();
        os.close();
        return newBalance;
    }
}
