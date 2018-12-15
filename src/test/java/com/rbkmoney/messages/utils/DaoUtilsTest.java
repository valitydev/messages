package com.rbkmoney.messages.utils;

import com.rbkmoney.messages.exception.DaoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.List;

@RunWith(BlockJUnit4ClassRunner.class)
public class DaoUtilsTest {

    @Test
    public void collectToIdsCollection() {
        Assert.assertEquals("('1', '2', '3')", DaoUtils.collectToIdsCollection(List.of("1", "2", "3")));
    }

    @Test(expected = DaoException.class)
    public void throwWhenEmptyUpdates() {
        DaoUtils.checkUpdateRowsSuccess(new int[][]{});
    }


    @Test(expected = DaoException.class)
    public void throwWhenUnsuccessfulUpdate() {
        DaoUtils.checkUpdateRowsSuccess(new int[][]{{0, 0, 0}, {0, 0, 1}});
    }

    @Test
    public void notThrownForCorrectUpdates() {
        DaoUtils.checkUpdateRowsSuccess(new int[][]{{1, 1, 1}, {1, 1, 1}});
    }

}
