package com.gtech.food_api.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gtech.food_api.api.dto.report.DailySelling;
import com.gtech.food_api.domain.filter.DailySellingFilter;
import com.gtech.food_api.domain.service.report.SellingQueryService;
import com.gtech.food_api.domain.service.report.SellingReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private SellingQueryService sellingQueryService;

    @Autowired
    private SellingReportService sellingReportService;

    @GetMapping(value = "/daily-selling", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DailySelling>> queryDailySelling(DailySellingFilter filter, @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {
        List<DailySelling> dailySelling = sellingQueryService.queryDailySelling(filter, timeOffset);
        return ResponseEntity.ok().body(dailySelling);
    }

    @GetMapping(value = "/daily-selling", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> queryDailySellingPDF(DailySellingFilter filter, @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {
        // gera o relatório em PDF
        byte[] reportPdf = sellingReportService.generateDailySellingReport(filter, timeOffset);
        // adiciona o header para o relatório em PDF
        var header = new HttpHeaders();
        // attachment serve para o navegador baixar o arquivo e filename é o nome do arquivo
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=daily-selling.pdf");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .headers(header)
                .body(reportPdf);
    }
    
}
