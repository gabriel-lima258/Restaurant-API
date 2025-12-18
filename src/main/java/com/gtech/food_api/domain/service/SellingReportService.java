package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.filter.DailySellingFilter;

public interface SellingReportService {
    // retorna o relatório em PDF em bytes para ser exibido no navegador implementado em PdfJasperReportService
    byte[] generateDailySellingReport(DailySellingFilter filter, String timeOffset);
}
