package com.gtech.food_api.domain.service.exceptions;

// exception para lidar com requisições inválidas das regras de negocios
public class BusinessException extends RuntimeException {
    /**
     * Construtor simples que cria uma exceção apenas com mensagem de erro.
     * 
     * @param message Mensagem descritiva do erro de negócio
     */
    public BusinessException(String message) {
        super(message);
    }
    
    /**
     * Construtor que permite encadear exceções (exception chaining).
     * 
     * O parâmetro Throwable cause serve para preservar a exceção original que causou
     * este erro de negócio. Isso é importante porque:
     * 
     * 1. **Rastreabilidade**: Mantém a cadeia completa de exceções, permitindo
     *    identificar a causa raiz do problema através do stack trace completo
     * 
     * 2. **Contexto**: Preserva informações detalhadas da exceção original que
     *    podem ser úteis para debug e logs
     * 
     * 3. **Padrão de Design**: Segue o padrão de "exception wrapping", onde uma
     *    exceção de nível mais alto (BusinessException) encapsula uma exceção
     *    de nível mais baixo (ex: KitchenNotFoundException)
     * 
     * Exemplo de uso no código:
     * ```java
     * try {
     *     restaurantService.save(restaurant);
     * } catch (KitchenNotFoundException e) {
     *     - Converte KitchenNotFoundException em BusinessException,
     *     - mas preserva a exceção original como causa
     *     throw new BusinessException(e.getMessage(), e);
     * }
     * ```
     * 
     * Quando esta exceção for lançada, o stack trace mostrará tanto a
     * BusinessException quanto a KitchenNotFoundException original, facilitando
     * a identificação do problema.
     * 
     * @param message Mensagem descritiva do erro de negócio
     * @param cause Exceção original que causou este erro (pode ser null)
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
