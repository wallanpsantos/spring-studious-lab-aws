package br.com.springstudiouslabaws;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ReadmeGuideTest {

    @Test
    void readmeDescreveOLaboratorioAtual() throws IOException {
        Path root = repoRoot();
        String readme = Files.readString(root.resolve("README.md"));
        assertFalse(readme.isBlank());

        List<String> presentes = List.of(
                "POST /v1/emprestimos",
                "GET /v1/emprestimos/{loanId}",
                "POST /v1/pagamentos",
                "nome",
                "pagamentos",
                "identificador_emprestimo",
                "Idempotency-Key",
                "400",
                "409",
                "422",
                "error.code",
                "error.message",
                "error.details",
                "docs/archify/arquitetura-emprestimos-pagamentos/contrato-io.md",
                "https://archify.si/guide",
                "docs/archify/",
                ".archify/",
                ".html",
                ".md",
                "localstack/localstack:4.14.0",
                "mongo:8.0",
                "8082",
                "Java 25",
                "4.1.2",
                "Maven 3.8",
                "Docker Compose v2",
                "docker compose up -d",
                "lab-entrypoint",
                "lab-core",
                "lab-dataprovider",
                "lab-config",
                "br.com.springstudiouslabaws.LabEntrypointApplication",
                "mvnw test",
                "mvnw.cmd test",
                "mvnw verify",
                "mvnw.cmd verify",
                "JaCoCo",
                "80%",
                "pendente");
        for (String frase : presentes) {
            assertTrue(readme.contains(frase), () -> "ausente: " + frase);
        }

        List<String> ausentes = List.of(
                "Em Desenvolvimento",
                "Em breve, será adicionada",
                "qwe123",
                "mongodb://root",
                "AWS_SECRET_ACCESS_KEY",
                "OAuth2",
                "Micrometer",
                "circuit breaker");
        for (String frase : ausentes) {
            assertFalse(readme.contains(frase), () -> "proibida: " + frase);
        }
    }

    private static Path repoRoot() {
        Path dir = Path.of("").toAbsolutePath();
        while (dir != null) {
            if (Files.isRegularFile(dir.resolve("pom.xml")) && Files.isRegularFile(dir.resolve("README.md"))) {
                return dir;
            }
            dir = dir.getParent();
        }
        fail("README.md não encontrado a partir de " + Path.of("").toAbsolutePath());
        return Path.of("");
    }
}
