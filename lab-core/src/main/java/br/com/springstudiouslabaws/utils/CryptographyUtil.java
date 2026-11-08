package br.com.springstudiouslabaws.utils;

import br.com.springstudiouslabaws.exceptions.CryptographyException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe utilitária para operações de criptografia, descriptografia e hash.
 * Fornece métodos seguros para criptografar dados sensíveis, gerar e verificar hashes,
 * utilizando algoritmos criptográficos robustos e práticas de segurança recomendadas.
 *
 * <p>Esta classe utiliza:
 * <ul>
 *     <li>AES/GCM/NoPadding para criptografia simétrica</li>
 *     <li>PBKDF2WithHmacSHA256 para derivação de chave</li>
 *     <li>SecureRandom para geração de IVs e salts</li>
 *     <li>Limpeza automática de dados sensíveis da memória</li>
 * </ul>
 *
 * <p><strong>Características de Segurança:</strong>
 * <ul>
 *     <li>Utiliza GCM (Galois/Counter Mode) para autenticação e criptografia</li>
 *     <li>Salt único para cada operação de hash</li>
 *     <li>IV (vetor de inicialização) único para cada operação de criptografia</li>
 *     <li>Iterações múltiplas na derivação de chave (65536)</li>
 *     <li>Chaves de 256 bits</li>
 * </ul>
 *
 * <p><strong>Exemplo de Uso para Criptografia:</strong>
 * <pre>{@code
 * String dadosSensiveis = "Dados confidenciais";
 * String salt = "SaltSeguro123!@#";
 *
 * // Criptografando dados
 * try {
 *     String dadosCriptografados = CryptographyUtil.encrypt(dadosSensiveis, salt);
 *     System.out.println("Dados criptografados: " + dadosCriptografados);
 *
 *     // Descriptografando dados
 *     String dadosOriginais = CryptographyUtil.decrypt(dadosCriptografados, salt);
 *     System.out.println("Dados descriptografados: " + dadosOriginais);
 * } catch (CryptographyException e) {
 *     // Tratamento de erro apropriado
 *     logger.error("Erro na operação criptográfica", e);
 * }
 * }</pre>
 *
 * <p><strong>Exemplo de Uso para Hash:</strong>
 * <pre>{@code
 * String senha = "MinhaSenhaSecreta";
 *
 * // Gerando hash
 * try {
 *     String hashGerado = CryptographyUtil.hash(senha);
 *
 *     // Verificando hash
 *     boolean hashValido = CryptographyUtil.verifyHash(senha, hashGerado);
 *     System.out.println("Hash válido: " + hashValido);
 * } catch (CryptographyException e) {
 *     logger.error("Erro na operação de hash", e);
 * }
 * }</pre>
 *
 * <p><strong>Considerações de Segurança:</strong>
 * <ol>
 *     <li>O salt deve ser único e seguro para cada contexto de uso</li>
 *     <li>O salt não precisa ser secreto, mas deve ser armazenado junto com os dados criptografados</li>
 *     <li>Cada operação de criptografia gera um IV único, garantindo que os mesmos dados gerem
 *         diferentes resultados criptografados</li>
 *     <li>Os hashes gerados incluem o salt automaticamente e são seguros para armazenamento de senhas</li>
 * </ol>
 *
 * <p><strong>Exceções:</strong>
 * <ul>
 *     <li>{@link CryptographyException} - Lançada para erros de criptografia, descriptografia,
 *         hash ou entradas inválidas</li>
 * </ul>
 *
 * @see javax.crypto.Cipher
 * @see javax.crypto.SecretKey
 * @see java.security.SecureRandom
 */
public final class CryptographyUtil {
    private static final Logger LOGGER = Logger.getLogger(CryptographyUtil.class.getName());

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Construtor privado para prevenir instanciação.
     * Esta é uma classe utilitária que não deve ser instanciada.
     *
     * @throws UnsupportedOperationException sempre, pois a classe não deve ser instanciada
     */
    private CryptographyUtil() {
        throw new UnsupportedOperationException("Classe utilitária não deve ser instanciada");
    }

    /**
     * Criptografa uma string utilizando AES/GCM/NoPadding.
     * O método gera um IV (Vetor de Inicialização) único para cada operação de criptografia,
     * garantindo que mesmo dados idênticos produzam resultados diferentes.
     *
     * <p>O resultado inclui o IV concatenado com o texto cifrado, codificado em Base64.
     * Estrutura do resultado: Base64(IV + TextoCifrado)
     *
     * <p><strong>Exemplo de uso:</strong>
     * <pre>{@code
     * String dados = "Dados sensíveis";
     * String salt = "MeuSaltSecreto123";
     *
     * try {
     *     String dadosCriptografados = CryptographyUtil.encrypt(dados, salt);
     *     // O resultado será diferente a cada chamada devido ao IV único
     * } catch (CryptographyException e) {
     *     // Tratamento do erro
     * }
     * }</pre>
     *
     * @param data string a ser criptografada
     * @param salt salt usado para derivação da chave
     * @return string codificada em Base64 contendo IV + dados criptografados
     * @throws CryptographyException se ocorrer erro na criptografia ou se os parâmetros forem inválidos
     */
    public static String encrypt(String data, String salt) throws CryptographyException {
        validateInputs(data, salt);
        try {
            byte[] iv = new byte[GCM_IV_LENGTH];
            SECURE_RANDOM.nextBytes(iv);

            SecretKey key = generateKey(salt);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);

            byte[] cipherText = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            byte[] combined = new byte[iv.length + cipherText.length];

            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro na criptografia: {0}", e.getMessage());
            throw new CryptographyException("Erro na criptografia dos dados", e);
        }
    }


    /**
     * Descriptografa uma string previamente criptografada com {@link #encrypt}.
     * O método extrai o IV do início dos dados criptografados e utiliza o salt fornecido
     * para derivar a chave de descriptografia.
     *
     * <p>Se o salt fornecido for diferente do usado na criptografia, ou se os dados
     * estiverem corrompidos, uma exceção será lançada.
     *
     * <p><strong>Exemplo de uso:</strong>
     * <pre>{@code
     * String dadosCriptografados = "..."; // Dados previamente criptografados
     * String salt = "MeuSaltSecreto123";  // Mesmo salt usado na criptografia
     *
     * try {
     *     String dadosOriginais = CryptographyUtil.decrypt(dadosCriptografados, salt);
     * } catch (CryptographyException e) {
     *     if (e.getMessage().contains("Falha na autenticação")) {
     *         // Salt incorreto ou dados corrompidos
     *     } else {
     *         // Outro erro de descriptografia
     *     }
     * }
     * }</pre>
     *
     * @param encryptedData dados criptografados em formato Base64(IV + TextoCifrado)
     * @param salt          salt usado para derivação da chave
     * @return string original descriptografada
     * @throws CryptographyException se ocorrer erro na descriptografia, autenticação falhar ou parâmetros forem inválidos
     */
    public static String decrypt(String encryptedData, String salt) throws CryptographyException {
        validateInputs(encryptedData, salt);
        try {
            byte[] decoded = Base64.getDecoder().decode(encryptedData);

            byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);
            byte[] cipherText = Arrays.copyOfRange(decoded, GCM_IV_LENGTH, decoded.length);

            SecretKey key = generateKey(salt);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);

            byte[] decryptedText = cipher.doFinal(cipherText);
            return new String(decryptedText, StandardCharsets.UTF_8);
        } catch (Exception e) {
            String mensagem = e.getMessage();
            if (mensagem != null && mensagem.contains("Tag mismatch")) {
                // Log em nível INFO para falhas esperadas de autenticação
                LOGGER.log(Level.INFO, "Falha na autenticação durante descriptografia: {0}", mensagem);
                throw new CryptographyException("Falha na autenticação dos dados criptografados");
            } else {
                // Mantém log SEVERE para outros erros
                LOGGER.log(Level.SEVERE, "Erro na descriptografia: {0}", mensagem);
                throw new CryptographyException("Erro na descriptografia dos dados", e);
            }
        }
    }

    /**
     * Gera um hash seguro para uma string usando PBKDF2WithHmacSHA256.
     * O método gera automaticamente um salt aleatório e o inclui no resultado final.
     *
     * <p>O resultado contém tanto o salt quanto o hash, permitindo verificação posterior
     * sem necessidade de armazenar o salt separadamente.
     * Estrutura do resultado: Base64(Salt + Hash)
     *
     * <p><strong>Exemplo de uso:</strong>
     * <pre>{@code
     * String senha = "SenhaDoUsuario123";
     *
     * try {
     *     String hashParaArmazenar = CryptographyUtil.hash(senha);
     *     // O hash pode ser armazenado diretamente no banco de dados
     * } catch (CryptographyException e) {
     *     // Tratamento do erro
     * }
     * }</pre>
     *
     * @param data string para qual será gerado o hash
     * @return string em Base64 contendo salt e hash combinados
     * @throws CryptographyException se ocorrer erro na geração do hash ou se o parâmetro for inválido
     */
    public static String hash(String data) throws CryptographyException {
        validateInputs(data);
        try {
            byte[] salt = generateRandomSalt();
            byte[] hash = generateHash(data, salt);

            byte[] combined = new byte[salt.length + hash.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hash, 0, combined, salt.length, hash.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro na geração do hash: {0}", e.getMessage());
            throw new CryptographyException("Erro na geração do hash", e);
        }
    }

    /**
     * Verifica se uma string corresponde a um hash previamente gerado por {@link #hash}.
     * O método extrai o salt do hash armazenado e o utiliza para gerar um novo hash
     * dos dados fornecidos, comparando-o com o hash armazenado.
     *
     * <p><strong>Exemplo de uso:</strong>
     * <pre>{@code
     * String senhaFornecida = "SenhaDoUsuario123";
     * String hashArmazenado = "..."; // Hash previamente gerado e armazenado
     *
     * try {
     *     boolean senhaCorreta = CryptographyUtil.verifyHash(senhaFornecida, hashArmazenado);
     *     if (senhaCorreta) {
     *         // Senha válida
     *     } else {
     *         // Senha inválida
     *     }
     * } catch (CryptographyException e) {
     *     // Erro na verificação (formato inválido, etc)
     * }
     * }</pre>
     *
     * @param data       string a ser verificada
     * @param storedHash hash previamente gerado pelo método {@link #hash}
     * @return true se os dados corresponderem ao hash, false caso contrário
     * @throws CryptographyException se ocorrer erro na verificação ou se os parâmetros forem inválidos
     */
    public static boolean verifyHash(String data, String storedHash) throws CryptographyException {
        validateInputs(data, storedHash);
        try {
            byte[] combined = Base64.getDecoder().decode(storedHash);

            byte[] salt = Arrays.copyOfRange(combined, 0, 16);
            byte[] hash = Arrays.copyOfRange(combined, 16, combined.length);

            byte[] testHash = generateHash(data, salt);
            return MessageDigest.isEqual(hash, testHash);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro na verificação do hash: {0}", e.getMessage());
            throw new CryptographyException("Erro na verificação do hash", e);
        }
    }

    /**
     * Gera uma chave secreta a partir do salt fornecido usando PBKDF2WithHmacSHA256.
     *
     * @param salt salt para derivação da chave
     * @return chave secreta gerada
     * @throws Exception se ocorrer erro na geração da chave
     */
    private static SecretKey generateKey(String salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(
                salt.toCharArray(),
                salt.getBytes(StandardCharsets.UTF_8),
                ITERATION_COUNT,
                KEY_LENGTH
        );

        try {
            SecretKey tmp = factory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(), "AES");
        } finally {
            Arrays.fill(spec.getPassword(), '\u0000'); // Limpa dados sensíveis
        }
    }

    /**
     * Gera um salt aleatório de 16 bytes usando SecureRandom.
     *
     * @return array de bytes contendo o salt gerado
     */
    private static byte[] generateRandomSalt() {
        byte[] salt = new byte[16];
        SECURE_RANDOM.nextBytes(salt);
        return salt;
    }

    /**
     * Gera um hash PBKDF2 com os parâmetros fornecidos.
     *
     * @param data dados para gerar o hash
     * @param salt salt a ser usado na geração do hash
     * @return array de bytes contendo o hash gerado
     * @throws Exception se ocorrer erro na geração do hash
     */
    private static byte[] generateHash(String data, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(
                data.toCharArray(),
                salt,
                ITERATION_COUNT,
                KEY_LENGTH
        );
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } finally {
            Arrays.fill(spec.getPassword(), '\u0000'); // Limpa dados sensíveis
        }
    }

    /**
     * Valida as entradas fornecidas, garantindo que não sejam nulas ou vazias.
     *
     * @param inputs strings a serem validadas
     * @throws CryptographyException se alguma das entradas for nula ou vazia
     */
    private static void validateInputs(String... inputs) {
        for (String input : inputs) {
            if (input == null || input.isEmpty()) {
                throw new CryptographyException("Parâmetro de entrada não pode ser nulo ou vazio");
            }
        }
    }
}