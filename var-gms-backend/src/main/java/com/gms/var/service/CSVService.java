package com.gms.var.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gms.var.model.Trade;
import com.gms.var.utils.CSVReader;

import java.io.IOException;
import java.util.List;

@Service
public class CSVService {

    public List<Trade> parseCSV(MultipartFile file) throws IOException {
        return CSVReader.readTradesFromCSV(file);
    }
}
