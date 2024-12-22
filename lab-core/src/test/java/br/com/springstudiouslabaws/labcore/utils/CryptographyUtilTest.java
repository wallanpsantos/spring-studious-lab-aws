package br.com.springstudiouslabaws.labcore.utils;

import br.com.springstudiouslabaws.labcore.exceptions.CryptographyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Testes do CryptographyUtil")
class CryptographyUtilTest {

    private static final String DADOS_VALIDOS = "Dados para teste de criptografia";
    private static final String SALT_VALIDO = "SaltTeste123!@#";

    @Nested
    @DisplayName("Testes de Criptografia e Descriptografia")
    class TestesCriptografiaDescriptografia {

        @Test
        @DisplayName("Deve criptografar e descriptografar dados com sucesso")
        void deveCriptografarEDescriptografarDadosComSucesso() {
            // Dado
            String dadosOriginais = DADOS_VALIDOS;

            // Quando
            String dadosCriptografados = CryptographyUtil.encrypt(dadosOriginais, SALT_VALIDO);
            String dadosDescriptografados = CryptographyUtil.decrypt(dadosCriptografados, SALT_VALIDO);

            // Então
            assertNotEquals(dadosOriginais, dadosCriptografados, "Dados criptografados devem ser diferentes dos originais");
            assertEquals(dadosOriginais, dadosDescriptografados, "Dados descriptografados devem ser iguais aos originais");
        }

        @Test
        @DisplayName("Deve gerar criptografias diferentes para os mesmos dados")
        void deveGerarCriptografiasDiferentesParaMesmosDados() {
            // Quando
            String primeiraCriptografia = CryptographyUtil.encrypt(DADOS_VALIDOS, SALT_VALIDO);
            String segundaCriptografia = CryptographyUtil.encrypt(DADOS_VALIDOS, SALT_VALIDO);

            // Então
            assertNotEquals(primeiraCriptografia, segundaCriptografia,
                    "Diferentes chamadas de criptografia devem gerar resultados diferentes devido ao IV aleatório");
        }

        @Test
        @DisplayName("Deve lançar exceção ao descriptografar com salt incorreto")
        void deveLancarExcecaoAoDescriptografarComSaltIncorreto() {
            // Dado
            String dadosCriptografados = CryptographyUtil.encrypt(DADOS_VALIDOS, SALT_VALIDO);
            String saltIncorreto = "SaltIncorreto456!@#";

            // Quando/Então
            assertThrows(CryptographyException.class,
                    () -> CryptographyUtil.decrypt(dadosCriptografados, saltIncorreto),
                    "Deve lançar exceção ao descriptografar com salt incorreto");
        }

        @Test
        @DisplayName("Deve lançar exceção para dados nulos na criptografia")
        void deveLancarExcecaoParaDadosNulosNaCriptografia() {
            assertThrows(CryptographyException.class,
                    () -> CryptographyUtil.encrypt(null, SALT_VALIDO),
                    "Deve lançar exceção para dados nulos");
        }

        @Test
        @DisplayName("Deve lançar exceção para salt nulo na criptografia")
        void deveLancarExcecaoParaSaltNuloNaCriptografia() {
            assertThrows(CryptographyException.class,
                    () -> CryptographyUtil.encrypt(DADOS_VALIDOS, null),
                    "Deve lançar exceção para salt nulo");
        }

        @Test
        @DisplayName("Deve lançar exceção para dados vazios na criptografia")
        void deveLancarExcecaoParaDadosVaziosNaCriptografia() {
            assertThrows(CryptographyException.class,
                    () -> CryptographyUtil.encrypt("", SALT_VALIDO),
                    "Deve lançar exceção para dados vazios");
        }

        @Test
        @DisplayName("Deve lançar exceção para salt vazio na criptografia")
        void deveLancarExcecaoParaSaltVazioNaCriptografia() {
            assertThrows(CryptographyException.class,
                    () -> CryptographyUtil.encrypt(DADOS_VALIDOS, ""),
                    "Deve lançar exceção para salt vazio");
        }
    }

    @Nested
    @DisplayName("Testes de Hash e Verificação")
    class TestesHashVerificacao {

        @Test
        @DisplayName("Deve criar e verificar hash com sucesso")
        void deveCriarEVerificarHashComSucesso() {
            // Dado
            String dadosOriginais = DADOS_VALIDOS;

            // Quando
            String hash = CryptographyUtil.hash(dadosOriginais);

            // Então
            assertTrue(CryptographyUtil.verifyHash(dadosOriginais, hash),
                    "Verificação do hash deve ser bem-sucedida para os dados originais");
            assertFalse(CryptographyUtil.verifyHash("dados incorretos", hash),
                    "Verificação do hash deve falhar para dados diferentes");
        }

        @Test
        @DisplayName("Deve gerar hashes diferentes para os mesmos dados")
        void deveGerarHashesDiferentesParaMesmosDados() {
            // Quando
            String primeiroHash = CryptographyUtil.hash(DADOS_VALIDOS);
            String segundoHash = CryptographyUtil.hash(DADOS_VALIDOS);

            // Então
            assertNotEquals(primeiroHash, segundoHash,
                    "Diferentes chamadas de hash devem gerar resultados diferentes devido ao salt aleatório");
        }

        @Test
        @DisplayName("Deve lançar exceção para dados nulos no hash")
        void deveLancarExcecaoParaDadosNulosNoHash() {
            assertThrows(CryptographyException.class,
                    () -> CryptographyUtil.hash(null),
                    "Deve lançar exceção para dados nulos no hash");
        }

        @Test
        @DisplayName("Deve lançar exceção para dados vazios no hash")
        void deveLancarExcecaoParaDadosVaziosNoHash() {
            assertThrows(CryptographyException.class,
                    () -> CryptographyUtil.hash(""),
                    "Deve lançar exceção para dados vazios no hash");
        }

        @Test
        @DisplayName("Deve lançar exceção para dados nulos na verificação do hash")
        void deveLancarExcecaoParaDadosNulosNaVerificacaoHash() {
            assertThrows(CryptographyException.class,
                    () -> CryptographyUtil.verifyHash(null, "hashValido"),
                    "Deve lançar exceção para dados nulos na verificação");
        }

        @Test
        @DisplayName("Deve lançar exceção para hash nulo na verificação")
        void deveLancarExcecaoParaHashNuloNaVerificacao() {
            assertThrows(CryptographyException.class,
                    () -> CryptographyUtil.verifyHash(DADOS_VALIDOS, null),
                    "Deve lançar exceção para hash nulo na verificação");
        }
    }

    @Test
    @DisplayName("Deve ter construtor privado")
    void deveTerConstrutorPrivado() {
        try {
            // Usando reflexão para verificar se o construtor é privado
            Constructor<?>[] construtores = CryptographyUtil.class.getDeclaredConstructors();
            assertEquals(1, construtores.length, "Deve ter exatamente um construtor");
            assertFalse(construtores[0].canAccess(null), "Construtor deve ser privado");

            // Verifica se o construtor lança exceção mesmo quando acessado via reflexão
            Constructor<?> construtor = construtores[0];
            construtor.setAccessible(true);
            try {
                construtor.newInstance();
                fail("Deveria ter lançado UnsupportedOperationException");
            } catch (ReflectiveOperationException e) {
                assertInstanceOf(UnsupportedOperationException.class, e.getCause(), "Causa da exceção deve ser UnsupportedOperationException");
                assertEquals("Classe utilitária não deve ser instanciada",
                        e.getCause().getMessage(), "Mensagem da exceção deve corresponder");
            }
        } catch (SecurityException e) {
            fail("Não deveria lançar SecurityException");
        }
    }
}