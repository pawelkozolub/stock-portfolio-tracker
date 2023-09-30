package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.model.PriceHistory;
import org.example.service.PriceHistoryRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class UpdatePriceHistory {

    private final String CSV_DIR = "./csvdata/";
    private final String CSV_FILE_LIST = "_csv_files.txt";
    private final ResourceLoader resourceLoader;
    private final PriceHistoryRepository priceHistoryRepository;

    public UpdatePriceHistory(ResourceLoader resourceLoader, PriceHistoryRepository priceHistoryRepository) {
        this.resourceLoader = resourceLoader;
        this.priceHistoryRepository = priceHistoryRepository;
    }

    @GetMapping("/update")
    @ResponseBody
    public String updatePriceHistory() {
        List<String> stocks = getCsvFileNames();
        for (String stock : stocks) {
            long lineCount = 0;
            Resource resource = resourceLoader.getResource("classpath:" + CSV_DIR + stock + "_w.csv");
            try (BufferedReader bf = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                log.info("Stock: {} data opened...", stock.toUpperCase());
                String line;
                while ((line = bf.readLine()) != null) {
                    String[] lineSplit = line.split(",");
                    log.info("{}: {}, {}, {}, {}, {}, {}",
                            lineCount, lineSplit[0], lineSplit[1], lineSplit[2], lineSplit[3], lineSplit[4], lineSplit[5]);
                    if (lineCount > 0) {
                        PriceHistory priceHistory = new PriceHistory(
                                null,
                                stock.toUpperCase(),
                                lineSplit[0],
                                BigDecimal.valueOf(Double.parseDouble(lineSplit[1])),
                                BigDecimal.valueOf(Double.parseDouble(lineSplit[2])),
                                BigDecimal.valueOf(Double.parseDouble(lineSplit[3])),
                                BigDecimal.valueOf(Double.parseDouble(lineSplit[4])),
                                Long.parseLong(lineSplit[5])
                        );
                        priceHistoryRepository.save(priceHistory);
                    }
                    lineCount++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("Stock: {} data updated...", stock.toUpperCase());
        }
        log.info("All stock data updated...");
        return "All stock data updated...";
    }

    private List<String> getCsvFileNames() {
        List<String> resourceFileNames = new ArrayList<>();
        Resource resource = resourceLoader.getResource("classpath:" + CSV_DIR + CSV_FILE_LIST);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                resourceFileNames.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resourceFileNames;
    }
}
