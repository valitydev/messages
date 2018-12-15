package com.rbkmoney.messages.utils;

import com.rbkmoney.messages.exception.DaoException;

import java.util.List;
import java.util.stream.Collectors;

public class DaoUtils {

    public static final int BATCH_SIZE = 1000;

    public static void checkUpdateRowsSuccess(int[][] updateCounts) throws DaoException {
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


    public static String collectToIdsCollection(List<String> ids) {
        return ids.stream().collect(Collectors.joining("', '", "('", "')"));
    }

}
