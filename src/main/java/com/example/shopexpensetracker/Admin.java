package com.example.shopexpensetracker;

import com.example.shopexpensetracker.Models.Bill;
import com.example.shopexpensetracker.Models.Employee;
import com.example.shopexpensetracker.Models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.format.CellDateFormatter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Admin {

    public static void AddProduct(String ProductName,double ProductBuyPrice,double ProductSellPrice,int ProductQuantity) throws IOException {

        File file = new File("src/main/resources/data/Product.xlsx");
        System.out.println(file.getAbsolutePath());

        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        System.out.println(workbook);
        XSSFSheet sheet = workbook.getSheetAt(0);
        System.out.println(sheet);

        int lastRow = sheet.getLastRowNum();
        System.out.println(lastRow);
        System.out.println(ProductName);
        System.out.println(ProductBuyPrice);
        System.out.println(ProductSellPrice);
        System.out.println(ProductQuantity);

        XSSFRow productIsPresent =  Common.isPresent(sheet,ProductName,1);

        if(productIsPresent==null){
            System.out.println("Product Is not present");
            int productID = (int) sheet.getRow(lastRow).getCell(0).getNumericCellValue() + 1;
            XSSFRow row = sheet.createRow(lastRow + 1) ;
            row.createCell(0).setCellValue(productID);
            row.createCell(1).setCellValue(ProductName);
            row.createCell(2).setCellValue(ProductSellPrice);
            row.createCell(3).setCellValue(ProductQuantity);
        }
        else{
            System.out.println("Product Is present");
            int updateProductStock =  ProductQuantity + (int) productIsPresent.getCell(3).getNumericCellValue();
            productIsPresent.getCell(2).setCellValue(ProductSellPrice);
            productIsPresent.getCell(3).setCellValue(updateProductStock);
        }

        fis.close();

        //Creating output stream and writing the updated workbook
        FileOutputStream os = new FileOutputStream(file);
        workbook.write(os);

        //Close the workbook and output stream
        workbook.close();
        os.close();

        System.out.println("Product excel file has been updated successfully.");

        String reportTitle = "Bought (" + ProductName + ")" + " X " + ProductQuantity;
        double reportAmount = ProductQuantity * ProductBuyPrice * -1;
        Common.addReport(reportTitle,reportAmount);

    }

    public static List<Product> getThirdPartyProduct() throws IOException {
        List <Product> products = new ArrayList<>();

        File file = new File("src/main/resources/data/ThirdPartyProduct.xlsx");
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        System.out.println(sheet);
        System.out.println(workbook);

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

    public static ObservableList<Bill> getBills() throws IOException {
        ObservableList <Bill> billList = FXCollections.observableArrayList();
        File file = new File("src/main/resources/data/Report.xlsx");
        System.out.println(file.getAbsolutePath());
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        System.out.println(workbook);
        XSSFSheet sheet = workbook.getSheet("bill-names");
        System.out.println(sheet);
        int rowCount = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rowCount; i++) {
            XSSFRow row = sheet.getRow(i);
            String billName = row.getCell(0).getStringCellValue();
            billList.add(new Bill(billName));
        }
        return billList;
    }

    public static void addBill(String billName) throws IOException {
        File file = new File("src/main/resources/data/Report.xlsx");
        System.out.println(file.getAbsolutePath());

        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        System.out.println(workbook);
        XSSFSheet sheet = workbook.getSheet("bill-names");
        System.out.println(sheet);

        int lastRow = sheet.getPhysicalNumberOfRows();
        System.out.println(lastRow);
        System.out.println(billName);

        XSSFRow row = sheet.createRow(lastRow);
        row.createCell(0).setCellValue(billName);

        fis.close();

        //Creating output stream and writing the updated workbook
        FileOutputStream os = new FileOutputStream(file);
        workbook.write(os);

        //Close the workbook and output stream
        workbook.close();
        os.close();

        System.out.println("Bill excel file has been updated successfully.");
    }
    public static void deleteProduct(String ProductName) throws IOException {
        File file = new File("src/main/resources/data/Product.xlsx");
        System.out.println(file.getAbsolutePath());

        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        System.out.println(workbook);
        XSSFSheet sheet = workbook.getSheetAt(0);
        System.out.println(sheet);
        XSSFRow productIsPresent =  Common.isPresent(sheet,ProductName,1);
        int totalNoOfRows = sheet.getLastRowNum();
        System.out.println(totalNoOfRows);
        if(productIsPresent != null){
            int rowIndex = productIsPresent.getRowNum();
            if (rowIndex >= 0 && rowIndex < totalNoOfRows) {
                sheet.shiftRows(rowIndex + 1, totalNoOfRows, -1);
            }
            if (rowIndex == totalNoOfRows) {
                sheet.removeRow(productIsPresent);
            }
        }

        fis.close();

        //Creating output stream and writing the updated workbook
        FileOutputStream os = new FileOutputStream(file);
        workbook.write(os);

        //Close the workbook and output stream
        workbook.close();
        os.close();
    }

    public static void editProduct(Product product, String productName, int productStock, double productPrice) throws IOException {
        File file = new File("src/main/resources/data/Product.xlsx");
        System.out.println(file.getAbsolutePath());

        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        System.out.println(workbook);
        XSSFSheet sheet = workbook.getSheetAt(0);
        System.out.println(sheet);
        XSSFRow productIsPresent =  Common.isPresent(sheet,product.getProductName(),1);

        if(productIsPresent != null){
            productIsPresent.getCell(1).setCellValue(productName);
            productIsPresent.getCell(2).setCellValue(productPrice);
            productIsPresent.getCell(3).setCellValue(productStock);
        }

        fis.close();

        //Creating output stream and writing the updated workbook
        FileOutputStream os = new FileOutputStream(file);
        workbook.write(os);

        //Close the workbook and output stream
        workbook.close();
        os.close();
    }

    public static ObservableList<Employee> getAllEmployee() throws IOException, ParseException {
        File file = new File("src/main/resources/data/Employee.xlsx");
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        System.out.println(sheet);
        System.out.println(workbook);

        ObservableList<Employee> employeeList = FXCollections.observableArrayList();
        int rowCount = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rowCount; i++) {
            XSSFRow row = sheet.getRow(i);
            int employeeID = (int) row.getCell(0).getNumericCellValue();
            String lastPaidDate = new CellDateFormatter("MM/dd/yyyy").format(row.getCell(1).getDateCellValue());
            String employeeEmail = row.getCell(2).getStringCellValue();
            String employeeName = row.getCell(3).getStringCellValue();
            String employeePassword = row.getCell(4).getStringCellValue();
            Double employeeBalance = row.getCell(5).getNumericCellValue();
            employeeList.add(new Employee(employeeID,lastPaidDate,employeeEmail,employeeName,employeePassword,employeeBalance));
        }

        fis.close();
        return employeeList;
    }

    public static void paySalary(Employee selectedEmployee, double salaryAmount) throws IOException {
        File file = new File("src/main/resources/data/Employee.xlsx");
        System.out.println(file.getAbsolutePath());

        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        System.out.println(workbook);
        XSSFSheet sheet = workbook.getSheetAt(0);
        System.out.println(sheet);
        XSSFRow employeeIsPresent =  Common.isPresent(sheet,selectedEmployee.getEmployeeEmail(),2);

        if(employeeIsPresent != null){
            Date now = new Date();
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat((short)14);
            employeeIsPresent.getCell(1).setCellStyle(cellStyle);
            employeeIsPresent.getCell(1).setCellValue(now);

            double previousBalance = employeeIsPresent.getCell(5).getNumericCellValue();
            double newSalary = salaryAmount + previousBalance;
            employeeIsPresent.getCell(5).setCellValue(newSalary);
        }

        fis.close();

        //Creating output stream and writing the updated workbook
        FileOutputStream os = new FileOutputStream(file);
        workbook.write(os);

        //Close the workbook and output stream
        workbook.close();
        os.close();
    }

    public static void AddProductCoupon(Product product, String couponCode, double couponDiscount) throws IOException {
        File file = new File("src/main/resources/data/Product.xlsx");
        System.out.println(file.getAbsolutePath());

        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        System.out.println(workbook);
        XSSFSheet sheet = workbook.getSheetAt(0);
        System.out.println(sheet);
        XSSFRow productIsPresent =  Common.isPresent(sheet,product.getProductName(),1);

        if(productIsPresent != null){
            productIsPresent.createCell(4).setCellValue(couponCode);
            XSSFCell cell = productIsPresent.createCell(5);
            cell.setCellValue(couponDiscount/100); // set value as number
            CellStyle style = workbook.createCellStyle();
            style.setDataFormat(workbook.createDataFormat().getFormat("0.000%"));
            cell.setCellStyle(style);
        }

        fis.close();

        //Creating output stream and writing the updated workbook
        FileOutputStream os = new FileOutputStream(file);
        workbook.write(os);

        //Close the workbook and output stream
        workbook.close();
        os.close();
    }
}
