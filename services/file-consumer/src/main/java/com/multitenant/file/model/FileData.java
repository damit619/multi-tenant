package com.multitenant.file.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "file_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileData {
    @Id
    private Long id;

    @Column(name = "device_id")
    private Integer deviceId;

    @Column(name = "model")
    private String model;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "approval_date")
    private LocalDate approvalDate;
}
