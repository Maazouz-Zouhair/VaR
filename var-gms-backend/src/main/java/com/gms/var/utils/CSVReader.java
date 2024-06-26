package com.gms.var.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.gms.var.model.Trade;

public class CSVReader {

    private static final Logger logger = LoggerFactory.getLogger(CSVReader.class);

    public static List<Trade> readTradesFromCSV(MultipartFile file) throws IOException {
        List<Trade> trades = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            for (CSVRecord csvRecord : csvParser) {
                trades.add(parseRecord(csvRecord));
            }
            trades.forEach(trade -> logger.debug("Parsed Trade: ID={}, Values={}", trade.getId(), trade.getHistoricalValues()));
        } catch (NumberFormatException e) {
            throw new IOException("Invalid number format in CSV", e);
        }
        return trades;
    }

    private static Trade parseRecord(CSVRecord csvRecord) {
        List<Double> historicalValues = new ArrayList<>();
        for (int i = 1; i < csvRecord.size(); i++) {
            historicalValues.add(Double.parseDouble(csvRecord.get(i)));
        }
        return new Trade(csvRecord.get(0), historicalValues);
    }
}

