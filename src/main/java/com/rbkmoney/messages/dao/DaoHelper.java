package com.rbkmoney.messages.dao;

import com.rbkmoney.messages.exception.DaoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DaoHelper {

    @Value("${messages.db.batch-size:1000}")
    public Integer batchSize;

    public void checkUpdateRowsSuccess(int[][] updateCounts) throws DaoException {
        boolean checked = false;
        for (int[] updateCountArray : updateCounts) {
            for (int updateCount : updateCountArray) {
                checked = true;
                if (updateCount != 1) {
                    throw new DaoException("Unexpected update count: " + updateCount);
                }
            }
        }
        if (!checked) {
            throw new DaoException("No rows were updated!");
        }
    }

    public String collectToIdsCollection(List<String> ids) {
        return ids.stream().collect(Collectors.joining("', '", "('", "')"));
    }

}
