package br.com.springstudiouslabaws.labcore.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Classe utilitária para operações de serialização e deserialização JSON.
 * Fornece métodos para converter objetos Java para JSON e vice-versa, além de
 * ler arquivos JSON do diretório de recursos.
 *
 * <p>Esta classe utiliza Jackson ObjectMapper configurado para:
 * <ul>
 *     <li>Suportar módulos de data/hora do Java 8+</li>
 *     <li>Ignorar propriedades desconhecidas durante a deserialização</li>
 *     <li>Aceitar valores únicos como arrays</li>
 *     <li>Formatar a saída JSON com indentação</li>
 * </ul>
 *
 * <p><strong>Estrutura de Diretórios Necessária:</strong>
 * <pre>
 * src/
 *   main/
 *     resources/
 *       mocks/
 *         exemplo.json
 *   test/
 *     resources/
 *       mocks/
 *         teste-exemplo.json
 * </pre>
 *
 * <p><strong>Exemplo de Uso:</strong>
 * <pre>{@code
 * // Lendo arquivo JSON para objeto
 * MinhaClasse objeto = JsonUtil.readJsonFileToObject("mocks/exemplo.json", MinhaClasse.class);
 *
 * // Lendo arquivo JSON para Lista
 * List<MinhaClasse> lista = JsonUtil.readJsonFileToObject("mocks/exemplo.json",
 *     new TypeReference<List<MinhaClasse>>() {});
 *
 * // Convertendo objeto para JSON
 * String json = JsonUtil.toJson(objeto);
 *
 * // Convertendo JSON para objeto
 * MinhaClasse novoObjeto = JsonUtil.toObject(json, MinhaClasse.class);
 * }</pre>
 *
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see com.fasterxml.jackson.core.type.TypeReference
 */
@SuppressWarnings("ALL")
public final class JsonUtil {

    /**
     * ObjectMapper configurado com as configurações padrão da aplicação.
     */
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .enable(SerializationFeature.INDENT_OUTPUT);

    /**
     * Construtor privado para prevenir instanciação.
     */
    private JsonUtil() {
        throw new UnsupportedOperationException("Classe utilitária não deve ser instanciada");
    }

    /**
     * Lê um arquivo JSON do classpath e converte para o tipo especificado.
     *
     * @param resourcePath Caminho do arquivo no classpath (ex: "mocks/exemplo.json")
     * @param targetType   Classe do tipo de retorno desejado
     * @param <T>          Tipo genérico do objeto de retorno
     * @return Objeto convertido do JSON
     * @throws RuntimeException se o arquivo não for encontrado ou houver erro na conversão
     */
    public static <T> T readJsonFileToObject(String resourcePath, Class<T> targetType) {
        return readFromInputStream(getResourceAsStream(resourcePath), targetType);
    }

    /**
     * Lê um arquivo JSON do classpath e converte para o tipo de referência especificado.
     * Útil para coleções e tipos genéricos complexos.
     *
     * @param resourcePath  Caminho do arquivo no classpath
     * @param typeReference Referência do tipo para conversão
     * @param <T>           Tipo genérico do objeto de retorno
     * @return Objeto convertido do JSON
     * @throws RuntimeException se o arquivo não for encontrado ou houver erro na conversão
     */
    public static <T> T readJsonFileToObject(String resourcePath, TypeReference<T> typeReference) {
        return readFromInputStream(getResourceAsStream(resourcePath), typeReference);
    }

    /**
     * Converte uma string JSON para um objeto do tipo especificado.
     *
     * @param json       String JSON a ser convertida
     * @param targetType Classe do tipo de retorno desejado
     * @param <T>        Tipo genérico do objeto de retorno
     * @return Objeto convertido do JSON
     * @throws RuntimeException se houver erro na conversão
     */
    public static <T> T toObject(String json, Class<T> targetType) {
        try {
            return objectMapper.readValue(json, targetType);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter JSON para objeto: %s", e);
        }
    }

    /**
     * Converte uma string JSON para um objeto do tipo de referência especificado.
     *
     * @param json          String JSON a ser convertida
     * @param typeReference Referência do tipo para conversão
     * @param <T>           Tipo genérico do objeto de retorno
     * @return Objeto convertido do JSON
     * @throws RuntimeException se houver erro na conversão
     */
    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter JSON para objeto: %s", e);
        }
    }

    /**
     * Converte um objeto para sua representação em string JSON.
     *
     * @param object Objeto a ser convertido
     * @return String JSON representando o objeto
     * @throws RuntimeException se houver erro na conversão
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter objeto para JSON", e);
        }
    }

    /**
     * Obtém um InputStream para o arquivo no classpath.
     *
     * @param resourcePath Caminho do arquivo no classpath
     * @return InputStream do arquivo
     * @throws RuntimeException se o arquivo não for encontrado
     */
    private static InputStream getResourceAsStream(String resourcePath) {
        return Optional.ofNullable(JsonUtil.class.getClassLoader().getResourceAsStream(resourcePath))
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado: %s" + resourcePath));
    }

    /**
     * Lê um JSON de um InputStream e converte para o tipo especificado.
     *
     * @param inputStream InputStream contendo o JSON
     * @param targetType  Classe do tipo de retorno desejado
     * @param <T>         Tipo genérico do objeto de retorno
     * @return Objeto convertido do JSON
     * @throws RuntimeException se houver erro na leitura ou conversão
     */
    private static <T> T readFromInputStream(InputStream inputStream, Class<T> targetType) {
        try (inputStream) {
            return objectMapper.readValue(inputStream, targetType);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar JSON: " + e.getMessage(), e);
        }
    }

    /**
     * Lê um JSON de um InputStream e converte para o tipo de referência especificado.
     *
     * @param inputStream   InputStream contendo o JSON
     * @param typeReference Referência do tipo para conversão
     * @param <T>           Tipo genérico do objeto de retorno
     * @return Objeto convertido do JSON
     * @throws RuntimeException se houver erro na leitura ou conversão
     */
    private static <T> T readFromInputStream(InputStream inputStream, TypeReference<T> typeReference) {
        try (inputStream) {
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar JSON: " + e.getMessage(), e);
        }
    }

}
