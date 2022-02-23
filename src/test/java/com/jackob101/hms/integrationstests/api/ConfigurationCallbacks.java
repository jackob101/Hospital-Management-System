package com.jackob101.hms.integrationstests.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.function.Consumer;

@Setter
@Getter
public class ConfigurationCallbacks<T, F> {

    private Consumer<F> createSuccessfullyBefore;
    private Consumer<ResponseEntity<T>> createSuccessfullyAfter;

    private Consumer<F> createFailedBefore;
    private Consumer<ResponseEntity<T>> createFailedAfter;

    private Consumer<F> updateSuccessfullyBefore;
    private Consumer<ResponseEntity<T>> updateSuccessfullyAfter;

    private Consumer<F> updateFailedBefore;
    private Consumer<ResponseEntity<T>> updateFailedAfter;

    private Consumer<Long> findSuccessfullyBefore;
    private Consumer<ResponseEntity<T>> findSuccessfullyAfter;

    private Consumer<Long> findFailedBefore;
    private Consumer<ResponseEntity<String>> findFailedAfter;

    private Consumer<Long> findAllSuccessfullyBefore;
    private Consumer<ResponseEntity<Object[]>> findAllSuccessfullyAfter;

    private Consumer<Long> deleteSuccessfullyBefore;
    private Consumer<ResponseEntity<String>> deleteSuccessfullyAfter;

    private Consumer<Long> deleteFailedBefore;
    private Consumer<ResponseEntity<String>> deleteFailedAfter;

}
