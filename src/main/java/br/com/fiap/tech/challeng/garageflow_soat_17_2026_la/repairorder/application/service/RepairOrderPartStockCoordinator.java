package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.application.service;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.part.application.usecase.PartStockControlUseCase;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.exception.PartStockOperationException;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.repairorder.domain.model.PartSnapshot;
import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.exception.NotEnoughResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class RepairOrderPartStockCoordinator {

    private final PartStockControlUseCase partStockControlUseCase;

    public <T> T debitAndExecute(List<PartSnapshot> parts, Supplier<T> operation) {
        List<PartSnapshot> debitedParts = new ArrayList<>();
        debitParts(parts, debitedParts);

        try {
            return operation.get();
        } catch (RuntimeException exception) {
            rollbackDebitedParts(debitedParts, exception);
            throw exception;
        }
    }

    public <T> T debitAndExecute(PartSnapshot part, Supplier<T> operation) {
        return debitAndExecute(List.of(part), operation);
    }

    public <T> T restoreAndExecute(PartSnapshot part, Supplier<T> operation) {
        try {
            partStockControlUseCase.addPartStock(part.getId(), part.getQuantity());
        } catch (RuntimeException exception) {
            throw stockOperationException("add", exception);
        }

        try {
            return operation.get();
        } catch (RuntimeException exception) {
            try {
                partStockControlUseCase.debitPartStock(part.getId(), part.getQuantity());
            } catch (RuntimeException rollbackException) {
                PartStockOperationException stockException = new PartStockOperationException(
                        "remove",
                        "Failed to rollback stock after remove part failure: "
                                + rollbackException.getMessage());
                stockException.addSuppressed(exception);
                stockException.addSuppressed(rollbackException);
                throw stockException;
            }
            throw exception;
        }
    }

    private void debitParts(List<PartSnapshot> parts, List<PartSnapshot> debitedParts) {
        for (PartSnapshot part : parts) {
            try {
                partStockControlUseCase.debitPartStock(part.getId(), part.getQuantity());
                debitedParts.add(part);
            } catch (NotEnoughResourceException exception) {
                rollbackDebitedParts(debitedParts, exception);
                throw exception;
            } catch (RuntimeException exception) {
                rollbackDebitedParts(debitedParts, exception);
                throw stockOperationException("debit", exception);
            }
        }
    }

    private void rollbackDebitedParts(List<PartSnapshot> debitedParts, RuntimeException cause) {
        RuntimeException rollbackFailure = null;

        for (int index = debitedParts.size() - 1; index >= 0; index--) {
            PartSnapshot part = debitedParts.get(index);
            try {
                partStockControlUseCase.addPartStock(part.getId(), part.getQuantity());
            } catch (RuntimeException exception) {
                if (rollbackFailure == null) {
                    rollbackFailure = exception;
                } else {
                    rollbackFailure.addSuppressed(exception);
                }
            }
        }

        if (rollbackFailure != null) {
            PartStockOperationException exception = new PartStockOperationException(
                    "add",
                    "Failed to rollback stock after repair order operation failure: "
                            + rollbackFailure.getMessage());
            exception.addSuppressed(cause);
            exception.addSuppressed(rollbackFailure);
            throw exception;
        }
    }

    private PartStockOperationException stockOperationException(String operation, RuntimeException cause) {
        PartStockOperationException exception =
                new PartStockOperationException(operation, cause.getMessage());
        exception.addSuppressed(cause);
        return exception;
    }
}
