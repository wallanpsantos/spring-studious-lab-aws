package br.com.springstudiouslabaws.repositories;

import br.com.springstudiouslabaws.config.mongodb.MongoLoanRespository;
import br.com.springstudiouslabaws.entities.LoanEntity;
import br.com.springstudiouslabaws.enums.LoanStatusEnum;
import br.com.springstudiouslabaws.exceptions.BusinessException;
import br.com.springstudiouslabaws.exceptions.LoanNotFoundException;
import br.com.springstudiouslabaws.exceptions.LoanUpdateConflictException;
import br.com.springstudiouslabaws.mappers.LoanCoreToDataMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanRepositoryImplDebitTest {

    @Mock
    private MongoLoanRespository mongoRepository;

    @Mock
    private LoanCoreToDataMapper mapper;

    private LoanRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new LoanRepositoryImpl(mapper, mongoRepository);
    }

    @Test
    void baixaParcialGravaSaldoEStatusPendenteNaMesmaEscrita() {
        LocalDateTime antes = LocalDateTime.of(2020, 1, 1, 0, 0);
        when(mongoRepository.findLoanEntityByLoanId("loan-1")).thenReturn(loan(antes, new BigDecimal("100.00"), 4L));
        when(mongoRepository.save(any(LoanEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        repository.applyDebit("loan-1", new BigDecimal("40.00"));

        LoanEntity gravado = capturaUnicaGravacao();
        assertEquals("mongo-id", gravado.id());
        assertEquals(4L, gravado.version());
        assertEquals(new BigDecimal("60.00"), gravado.outstandingBalance());
        assertEquals(LoanStatusEnum.PENDING.getValue(), gravado.status());
        assertTrue(gravado.lastUpdate().isAfter(antes));
    }

    @Test
    void baixaTotalGravaSaldoZeroEStatusQuitadoNaMesmaEscrita() {
        when(mongoRepository.findLoanEntityByLoanId("loan-1"))
                .thenReturn(loan(LocalDateTime.of(2020, 1, 1, 0, 0), new BigDecimal("80.00"), 1L));
        when(mongoRepository.save(any(LoanEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        repository.applyDebit("loan-1", new BigDecimal("80.00"));

        LoanEntity gravado = capturaUnicaGravacao();
        assertEquals(0, gravado.outstandingBalance().compareTo(BigDecimal.ZERO));
        assertEquals(LoanStatusEnum.PAID.getValue(), gravado.status());
        assertEquals("mongo-id", gravado.id());
        assertEquals(1L, gravado.version());
    }

    @Test
    void emprestimoAusenteNaoGrava() {
        when(mongoRepository.findLoanEntityByLoanId("loan-1")).thenReturn(null);

        assertThrows(LoanNotFoundException.class, () -> repository.applyDebit("loan-1", new BigDecimal("10.00")));

        verify(mongoRepository, never()).save(any());
    }

    @Test
    void baixaInvalidaNaoGrava() {
        when(mongoRepository.findLoanEntityByLoanId("loan-1"))
                .thenReturn(loan(LocalDateTime.of(2020, 1, 1, 0, 0), new BigDecimal("10.00"), 0L));

        assertThrows(BusinessException.class, () -> repository.applyDebit("loan-1", new BigDecimal("11.00")));

        verify(mongoRepository, never()).save(any());
    }

    @Test
    void versaoDivergenteViraConflitoDeDominio() {
        when(mongoRepository.findLoanEntityByLoanId("loan-1"))
                .thenReturn(loan(LocalDateTime.of(2020, 1, 1, 0, 0), new BigDecimal("100.00"), 2L));
        when(mongoRepository.save(any(LoanEntity.class)))
                .thenThrow(new OptimisticLockingFailureException("versão divergente"));

        LoanUpdateConflictException erro = assertThrows(LoanUpdateConflictException.class,
                () -> repository.applyDebit("loan-1", new BigDecimal("10.00")));

        assertEquals("Empréstimo foi atualizado por outra operação", erro.getMessage());
    }

    private LoanEntity capturaUnicaGravacao() {
        ArgumentCaptor<LoanEntity> gravacao = ArgumentCaptor.forClass(LoanEntity.class);
        verify(mongoRepository).save(gravacao.capture());
        return gravacao.getValue();
    }

    private static LoanEntity loan(LocalDateTime lastUpdate, BigDecimal outstanding, Long version) {
        return new LoanEntity(
                "mongo-id",
                "loan-1",
                "descrição",
                new BigDecimal("100.00"),
                outstanding,
                LoanStatusEnum.OVERDUE.getValue(),
                null,
                null,
                null,
                lastUpdate,
                version);
    }
}
