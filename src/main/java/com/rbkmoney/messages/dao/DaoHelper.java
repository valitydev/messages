package com.rbkmoney.messages.dao;

import com.rbkmoney.messages.exception.DaoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class DaoHelper {

    @Value("${messages.db.batch-size:1000}")
    public Integer batchSize;

    public void checkUpdateRowsSuccess(int[][] updateCounts) throws DaoException {
        if (updateCounts.length == 0 || containsBadNumber(updateCounts)) {
            throw new DaoException("Unexpected updates count! " + Arrays.deepToString(updateCounts));
        }
    }

    public String collectToIdsCollection(List<String> ids) {
        return ids.stream().collect(Collectors.joining("', '", "('", "')"));
    }

    private boolean containsBadNumber(int[][] updateCounts) {
        return Stream.of(updateCounts)
                .flatMapToInt(IntStream::of)
                .anyMatch(value -> value != 1);
    }

}
