package com.gtech.food_api.domain.service.report;

import java.util.List;

import com.gtech.food_api.api.V1.dto.report.DailySelling;
import com.gtech.food_api.domain.filter.DailySellingFilter;

/**
 * Serviço para consultar vendas diárias
 * Ao inves de usar isso dentro do repositorio de orders, criamos um serviço para isso, pois é uma consulta mais complexa e nao se encaixa em nenhum repositorio específico.
 * Sua implementacao é feita na classe SellingQueryServiceImpl. Dentro do infra
 */
public interface SellingQueryService {
    
    List<DailySelling> queryDailySelling(DailySellingFilter filter, String timeOffset);
}
