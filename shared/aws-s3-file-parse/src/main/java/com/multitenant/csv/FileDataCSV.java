package com.multitenant.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FileDataCSV {

    @CsvBindByName(column = "Tenant_id")
    private String tenantId;
    @CsvBindByName(column = "Device_id")
    private Integer deviceId;
    @CsvBindByName(column = "Model")
    private String model;
    @CsvBindByName(column = "Manufacturer")
    private String manufacturer;
    @CsvBindByName(column = "Device_Type")
    private String deviceType;
    @CsvBindByName(column = "Approval_Date")
    @CsvDate(value = "dd-MM-yyyy")
    private LocalDate approvalDate;

    public static String [] getHeaders() {
        return new String[] { "Tenant_id" ,"Device_id","Model" ,"Manufacturer" ,"Device_Type" ,"Approval_Date" };
    }
}
