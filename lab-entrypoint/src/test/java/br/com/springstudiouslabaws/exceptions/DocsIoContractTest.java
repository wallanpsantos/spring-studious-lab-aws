package br.com.springstudiouslabaws.exceptions;

import br.com.springstudiouslabaws.dtos.request.loan.LoanRequest;
import br.com.springstudiouslabaws.dtos.request.payment.PaymentItemRequest;
import br.com.springstudiouslabaws.dtos.request.payment.PaymentRequest;
import br.com.springstudiouslabaws.dtos.response.loan.LoanResponse;
import br.com.springstudiouslabaws.dtos.response.payment.PaymentItemResponse;
import br.com.springstudiouslabaws.dtos.response.payment.PaymentResponse;
import br.com.springstudiouslabaws.enums.LoanStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.RecordComponent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class DocsIoContractTest {

    private static final Pattern ERROR_CODE = Pattern.compile("ApiErrorResponse\\.of\\(\"([A-Z0-9_]+)\"");
    private static final Path CONTRACT = Path.of(
            "docs/archify/arquitetura-emprestimos-pagamentos/contrato-io.md");
    private static final Path DIAGRAM = Path.of(
            "docs/archify/arquitetura-emprestimos-pagamentos/arquitetura-emprestimos-pagamentos.html");

    @Test
    void documentacaoCobreAtributosObrigatoriedadeExcecoesEStatusPadrao() throws IOException {
        Path root = repoRoot();
        String contrato = Files.readString(root.resolve(CONTRACT));
        String html = Files.readString(root.resolve(DIAGRAM));
        assertFalse(html.isBlank());

        for (Class<?> record : records()) {
            String section = section(contrato, record.getSimpleName());
            for (RecordComponent component : record.getRecordComponents()) {
                String jsonName = ContractFieldNames.toJsonPath(record, component.getName());
                boolean required = required(record, component);
                assertTrue(row(section, jsonName, required),
                        () -> record.getSimpleName() + "." + jsonName + " obrigatório=" + required);
            }
        }

        String handler = Files.readString(root.resolve(
                "lab-entrypoint/src/main/java/br/com/springstudiouslabaws/exceptions/ApiExceptionHandler.java"));
        Matcher codes = ERROR_CODE.matcher(handler);
        int found = 0;
        while (codes.find()) {
            found++;
            String code = codes.group(1);
            assertTrue(contrato.contains(code) && html.contains(code), () -> "código ausente: " + code);
        }
        assertTrue(found > 0);

        String pendente = LoanStatusEnum.PENDING.getValue();
        assertTrue(contrato.contains(pendente) && html.contains(pendente));
        assertTrue(html.contains("POST /v1/emprestimos"));
        assertTrue(html.contains("GET /v1/emprestimos/{loanId}"));
        assertTrue(html.contains("POST /v1/pagamentos"));
        assertTrue(html.contains("em branco executa"));
        assertTrue(html.contains("inválida, repete, em andamento, outro corpo"));
        assertTrue(html.contains("ausente 404; nulo vira pendente"));
        assertTrue(html.contains("regra de negócio 422"));
        assertTrue(html.contains("corpo inválido 422"));
    }

    private static List<Class<?>> records() {
        return List.of(
                LoanRequest.class,
                LoanResponse.class,
                PaymentRequest.class,
                PaymentItemRequest.class,
                PaymentResponse.class,
                PaymentItemResponse.class,
                ApiErrorResponse.class,
                ApiErrorResponse.ErrorBody.class,
                ApiErrorResponse.FieldDetail.class);
    }

    private static Path repoRoot() {
        Path dir = Path.of("").toAbsolutePath();
        while (dir != null) {
            if (Files.isRegularFile(dir.resolve(CONTRACT)) && Files.isRegularFile(dir.resolve("pom.xml"))) {
                return dir;
            }
            dir = dir.getParent();
        }
        fail(CONTRACT + " não encontrado a partir de " + Path.of("").toAbsolutePath());
        return Path.of("");
    }

    private static String section(String markdown, String name) {
        Matcher heading = Pattern.compile("(?m)^## " + Pattern.quote(name) + "\\s*$").matcher(markdown);
        if (!heading.find()) {
            fail("seção ausente: " + name);
        }
        int start = heading.end();
        Matcher next = Pattern.compile("(?m)^## ").matcher(markdown);
        int end = next.find(start) ? next.start() : markdown.length();
        return markdown.substring(start, end);
    }

    private static boolean row(String section, String jsonName, boolean required) {
        String flag = required ? "sim" : "não";
        Pattern row = Pattern.compile("(?m)^\\|\\s*" + Pattern.quote(jsonName) + "\\s*\\|\\s*" + flag + "\\s*\\|");
        return row.matcher(section).find();
    }

    private static boolean required(Class<?> type, RecordComponent component) {
        if (hasRequired(component.getAccessor().getDeclaredAnnotations())) {
            return true;
        }
        try {
            if (hasRequired(type.getDeclaredField(component.getName()).getDeclaredAnnotations())) {
                return true;
            }
        } catch (NoSuchFieldException ignored) {
            // Record sem campo com o nome do componente.
        }
        RecordComponent[] components = type.getRecordComponents();
        Class<?>[] parameters = new Class<?>[components.length];
        int index = -1;
        for (int i = 0; i < components.length; i++) {
            parameters[i] = components[i].getType();
            if (components[i].getName().equals(component.getName())) {
                index = i;
            }
        }
        try {
            Annotation[] annotations = type.getDeclaredConstructor(parameters).getParameterAnnotations()[index];
            return hasRequired(annotations);
        } catch (NoSuchMethodException | ArrayIndexOutOfBoundsException exception) {
            return false;
        }
    }

    private static boolean hasRequired(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            Class<?> annotationType = annotation.annotationType();
            if (annotationType == NotNull.class || annotationType == NotBlank.class || annotationType == NotEmpty.class) {
                return true;
            }
        }
        return false;
    }
}
