package com.gtech.food_api.infra.service.report;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtech.food_api.domain.service.SellingQueryService;
import com.gtech.food_api.domain.service.SellingReportService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.gtech.food_api.domain.filter.DailySellingFilter;

@Service
public class PdfJasperReportService implements SellingReportService {

    @Autowired
    private SellingQueryService sellingQueryService;

    @Override
    public byte[] generateDailySellingReport(DailySellingFilter filter, String timeOffset) {   
        try {
            // busca o arquivo jasper no classpath
            var inputStream = this.getClass().getResourceAsStream(
                "/reports/DailySelling.jasper");
            
            // valida se o arquivo foi encontrado
            if (inputStream == null) {
                throw new ReportException("Arquivo de relatório jasper não encontrado");
            }

            // cria os parametros para o relatório 
            var parameters = new HashMap<String, Object>();
            // locale é o idioma do relatório
            parameters.put("REPORT_LOCALE", new Locale("pt", "BR"));

            // busca os dados para o relatório
            var dailySelling = sellingQueryService.queryDailySelling(filter, timeOffset); 

            // dataSource é o conjunto de dados java que será usado no relatório, ja que temos uma lista de DailySelling
            var dataSource = new JRBeanCollectionDataSource(dailySelling);

            var jasperPrint = JasperFillManager.fillReport(inputStream, parameters, dataSource); 

            // JasperExportManager é responsável por exportar o relatório para PDF ou outros formatos
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new ReportException("Error generating daily selling report", e);
        }
    }
}
