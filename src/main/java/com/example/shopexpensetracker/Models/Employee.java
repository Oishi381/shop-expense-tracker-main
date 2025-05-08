package com.example.shopexpensetracker.Models;

import com.example.shopexpensetracker.Common;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Employee {
    private final Integer employeeID;
    private final String lastPaidDate;
    private final String employeeEmail;
    private final String employeeName;
    private final String employeePassword;
    private Double balance;
    private final String employeePaid;


    public Employee(Integer employeeID, String lastPaidDate, String employeeEmail, String employeeName, String employeePassword, Double balance) throws ParseException {
        this.employeeID = employeeID;
        this.lastPaidDate = lastPaidDate;
        this.employeeEmail = employeeEmail;
        this.employeeName = employeeName;
        this.employeePassword = employeePassword;
        this.balance = balance;
        this.employeePaid = isEmployeePaid(lastPaidDate);
    }

    public String getLastPaidDate() {
        return lastPaidDate;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public String getEmployeePassword() {
        return employeePassword;
    }

    public Double getBalance() {
        return balance;
    }

    public String getEmployeePaid(){
        return employeePaid;
    }

    public String isEmployeePaid(String lastPaidDate) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String firstDayOfMonth = formatter.format(LocalDate.now().withDayOfMonth(1));
        Date firstDayOfMonthDate = Common.formatter.parse(firstDayOfMonth);
        Date lastPaidDayDate = Common.formatter.parse(lastPaidDate);

        System.out.println("First Day of Month" + firstDayOfMonth);
        System.out.println("Last Paid Day" + lastPaidDate);

        if(!lastPaidDayDate.before(firstDayOfMonthDate)){
            return "Paid";
        }
        else{
            return "Not Paid";
        }
    }

    public void setBalance(double balance){
        this.balance = balance;
    }


}
