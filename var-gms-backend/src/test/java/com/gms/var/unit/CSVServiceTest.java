package com.gms.var.unit;

import com.gms.var.model.Trade;
import com.gms.var.service.CSVService;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CSVServiceTest {

    private final CSVService csvService = new CSVService();

    @Test
    public void testParseCSV() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "id,value1,value2\n1,1.0,2.0\n2,3.0,4.0\n".getBytes());
        List<Trade> trades = csvService.parseCSV(file);

        assertEquals(2, trades.size());
        assertEquals("1", trades.get(0).getId());
        assertEquals(Arrays.asList(1.0, 2.0), trades.get(0).getHistoricalValues());
        assertEquals("2", trades.get(1).getId());
        assertEquals(Arrays.asList(3.0, 4.0), trades.get(1).getHistoricalValues());
    }

    @Test
    public void testParseCSVWithInvalidData() {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "id,value1,value2\n1,1.0,invalid\n".getBytes());

        assertThrows(IOException.class, () -> {
            csvService.parseCSV(file);
        });
    }

    @Test
    public void testParseEmptyCSV() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "empty.csv", "text/csv", "id,value1,value2\n".getBytes());
        List<Trade> trades = csvService.parseCSV(file);

        assertEquals(0, trades.size());
    }
}

