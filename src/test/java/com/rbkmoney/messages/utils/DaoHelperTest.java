package com.rbkmoney.messages.utils;

import com.rbkmoney.messages.dao.DaoHelper;
import com.rbkmoney.messages.exception.DaoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.List;

@RunWith(BlockJUnit4ClassRunner.class)
public class DaoHelperTest {

    private final DaoHelper daoHelper = new DaoHelper();

    @Test
    public void collectToIdsCollection() {
        Assert.assertEquals("('1', '2', '3')", daoHelper.collectToIdsCollection(List.of("1", "2", "3")));
        Assert.assertEquals("('')", daoHelper.collectToIdsCollection(List.of()));
    }

    @Test(expected = DaoException.class)
    public void throwWhenEmptyUpdates() {
        daoHelper.checkUpdateRowsSuccess(new int[][]{});
    }


    @Test(expected = DaoException.class)
    public void throwWhenUnsuccessfulUpdate() {
        daoHelper.checkUpdateRowsSuccess(new int[][]{{0, 0, 0}, {0, 0, 1}});
    }

    @Test
    public void notThrownForCorrectUpdates() {
        daoHelper.checkUpdateRowsSuccess(new int[][]{{1, 1, 1}, {1, 1, 1}});
    }

}
