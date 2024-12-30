package com.multitenant.file.service;

import com.multitenant.aws.clients.TenantS3Client;
import com.multitenant.events.FileMessage;
import com.multitenant.config.database.SequenceHandler;
import com.multitenant.csv.CSVParser;
import com.multitenant.csv.FileDataCSV;
import com.multitenant.file.model.FileData;
import com.multitenant.file.repository.TenantFileRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class FileDataServiceImp implements FileDataService {
    private static final Logger logger = LoggerFactory.getLogger(FileDataServiceImp.class);
    private static final String FILE_DATA_SEQ = "file_data_id_seq";
    private final TenantS3Client tenantS3Client;
    private final CSVParser csvParser;
    private final SequenceHandler sequenceHandler;
    private final TenantFileRepository tenantFileRepository;

    @Transactional
    @Override
    public void createTenantFile(FileMessage fileMessage) {
        try(InputStream stream = tenantS3Client.download(fileMessage)) {
            List<FileData> fileDataList = csvParser.readFile(stream)
                    .stream()
                    .map(this::toTenantFile)
                    .peek(fileData -> fileData.setId(sequenceHandler.generateId(FILE_DATA_SEQ)))
                    .toList();
            tenantFileRepository.saveAll(fileDataList);
        } catch (Exception exception) {
            logger.error("Error occurred while downloading/processing file", exception);
        }
    }

    @Override
    public Optional<FileData> listFileByDeviceId(Integer deviceId) {
        return tenantFileRepository.findByDeviceId(deviceId);
    }

    @Override
    public void deleteTenantFile(FileMessage fileMessage) {
        try {
            tenantS3Client.delete(fileMessage);
        } catch (Exception e) {
            logger.error("Error occurred while deleting the file", e);
        }
    }

    private FileData toTenantFile(FileDataCSV fileDataCSV) {
        FileData fileData = new FileData();
        fileData.setDeviceId(fileDataCSV.getDeviceId());
        fileData.setDeviceType(fileDataCSV.getDeviceType());
        fileData.setModel(fileDataCSV.getModel());
        fileData.setManufacturer(fileDataCSV.getManufacturer());
        fileData.setApprovalDate(fileDataCSV.getApprovalDate());
        return fileData;
    }
}
