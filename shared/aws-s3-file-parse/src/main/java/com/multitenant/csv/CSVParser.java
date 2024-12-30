package com.multitenant.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public class CSVParser {

    public List<FileDataCSV> readFile (String fileName) {
        Resource resource = new ClassPathResource(fileName);
        HeaderColumnNameMappingStrategy<FileDataCSV> msIn = new HeaderColumnNameMappingStrategy<>();
        msIn.setType(FileDataCSV.class);
        try {
            return readFile(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FileDataCSV> readFile (InputStream inputStream) {
        HeaderColumnNameMappingStrategy<FileDataCSV> msIn = new HeaderColumnNameMappingStrategy<>();
        msIn.setType(FileDataCSV.class);
        // read the data from the input CSV file into our SplitBean list:
        Reader reader = new BufferedReader(new InputStreamReader(inputStream));
        return new CsvToBeanBuilder<FileDataCSV>(reader)
                .withMappingStrategy(msIn)
                .build()
                .parse();
    }

    public String writeFile(List<FileDataCSV> fileData) {
        try (Writer writer  = new StringWriter()) {
            HeaderColumnNameMappingStrategy<FileDataCSV> msIn = new HeaderColumnNameMappingStrategy<>();
            msIn.setType(FileDataCSV.class);
            StatefulBeanToCsv<FileDataCSV> sbc = new StatefulBeanToCsvBuilder<FileDataCSV>(writer)
                    .withMappingStrategy(msIn)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();
            sbc.write(fileData);
            return writer.toString();
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            throw new RuntimeException(e);
        }
    }
}
