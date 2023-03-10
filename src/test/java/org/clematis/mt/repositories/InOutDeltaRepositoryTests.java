package org.clematis.mt.repositories;

import java.util.List;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.InOutDelta;
import org.clematis.mt.repository.InOutDeltaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class InOutDeltaRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private InOutDeltaRepository deltaRepository;

    @Test
    void countDeltas() {
        Assertions.assertEquals(2, deltaRepository.count());
    }


    @Test
    void testQuery() {
        Page<InOutDelta> entries = deltaRepository.findAll(PageRequest.of(0, 10));
        Assertions.assertEquals(1, entries.getTotalPages());
        Assertions.assertEquals(2, entries.getTotalElements());
    }


    @Test
    void testDeltas() {
        List<InOutDelta> entries = deltaRepository.getDeltas("RUB");
        Assertions.assertEquals(0, entries.size());
    }
}
