package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.views.OperationEntry;
import org.clematis.mt.repository.OperationEntryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class OperationEntryRepositoryTest extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private OperationEntryRepository operationEntryRepository;

    @Test
    void testSorting() {
        Assertions.assertEquals(1517, operationEntryRepository
                .findAll(PageRequest.of(0, 30, Sort.Direction.DESC, "amount"))
                .getTotalElements());

        Page<OperationEntry> page = operationEntryRepository
                .findAll(PageRequest.of(0, 30, Sort.Direction.DESC, "amount"));

        Assertions.assertTrue(page.get().findFirst().isPresent());
        Assertions.assertEquals(1000000, page.get().findFirst().get().getAmount());

        page = operationEntryRepository
                .findAll(PageRequest.of(0, 30, Sort.Direction.ASC, "amount"));

        Assertions.assertTrue(page.get().findFirst().isPresent());
        Assertions.assertEquals(1, page.get().findFirst().get().getAmount());

    }


}
