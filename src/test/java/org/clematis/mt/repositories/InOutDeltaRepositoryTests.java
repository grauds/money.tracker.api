package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.InOutDelta;
import org.clematis.mt.repository.InOutDeltaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


/**
 * Repository tests for InOutDelta data access.
 */
public class InOutDeltaRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private InOutDeltaRepository deltaRepository;

    /**
     * Verifies the total count of in/out delta records.
     */
    @Test
    void countDeltas() {
        Assertions.assertEquals(2, deltaRepository.count());
    }


    /**
     * Verifies retrieving all in/out deltas.
     */
    @Test
    void testQuery() {
        Page<InOutDelta> entries = deltaRepository.findAll(PageRequest.of(0, 10));
        Assertions.assertEquals(1, entries.getTotalPages());
        Assertions.assertEquals(2, entries.getTotalElements());
    }


    /**
     * Verifies retrieving in/out deltas converted to the specified currency.
     */
    @Test
    void testDeltas() {
        Page<InOutDelta> entries = deltaRepository.getDeltas("RUB", Pageable.ofSize(100));
        Assertions.assertEquals(0, entries.getTotalElements());
    }
}
