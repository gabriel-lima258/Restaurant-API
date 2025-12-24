package com.gtech.food_api.core.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Wrapper para Page que preserva o Pageable original após conversões.
 * 
 * Por que usar esta classe?
 * - Quando você precisa converter uma Page (ex: após aplicar filtros ou traduções de ordenação)
 * - O PageImpl padrão pode perder informações do Pageable original durante conversões
 * - Esta classe garante que o Pageable passado no construtor seja sempre retornado pelo getPageable()
 * 
 * Quando usar:
 * - Após traduzir campos de ordenação (usando PageableTranslator)
 * - Quando você precisa manter o Pageable original mesmo após transformações
 * - Em situações onde o Pageable retornado pelo PageImpl não reflete o Pageable real usado na consulta
 * 
 * Exemplo de uso:
 * <pre>
 * Converter Pageable (traduzir campos de ordenação)
 * Pageable translatedPageable = PageableTranslator.translate(pageable, mapper);
 * 
 * Buscar dados com Pageable traduzido
 * Page<Order> orders = orderService.listAll(filter, translatedPageable);
 * 
 * Criar PageWrapper preservando o Pageable traduzido
 * PageWrapper<Order> wrappedPage = new PageWrapper<>(orders, translatedPageable);
 * 
 * Agora getPageable() sempre retorna o Pageable traduzido correto
 * PagedModel<OrderSummaryDTO> pagedModel = pagedResourcesAssembler.toModel(wrappedPage, assembler);
 * </pre>
 * 
 * Diferença do PageImpl:
 * - PageImpl: pode retornar um Pageable diferente do que foi passado no construtor
 * - PageWrapper: sempre retorna o Pageable exato que foi passado no construtor
 * 
 * @param <T> Tipo da entidade na página
 */
public class PageWrapper<T> extends PageImpl<T>{

    private Pageable pageable;

    /**
     * Construtor que cria um PageWrapper preservando o Pageable especificado.
     */
    public PageWrapper(Page<T> page, Pageable pageable) {
        super(page.getContent(), pageable, page.getTotalElements());
        this.pageable = pageable;
    }

    /**
     * Retorna o Pageable que foi passado no construtor.
     * 
     * Este método sobrescreve o comportamento padrão do PageImpl para garantir
     * que sempre retorne o Pageable original, mesmo após conversões ou transformações.
     * 
     * @return Pageable preservado do construtor
     */
    @Override
    public Pageable getPageable() {
        return this.pageable;
    }
}
