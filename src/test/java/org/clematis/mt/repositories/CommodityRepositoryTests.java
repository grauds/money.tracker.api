package org.clematis.mt.repositories;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.clematis.mt.model.Commodity;
import org.clematis.mt.repository.CommodityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


/**
 * Repository tests for Commodity data access.
 */
public class CommodityRepositoryTests extends ClematisMoneyTrackerApplicationTests {

    @Autowired
    private CommodityRepository commodityRepository;

    /**
     * Verifies the total count of commodities in the database.
     */
    @Test
    void countCommodities() {
        Assertions.assertEquals(1369, commodityRepository.count());
    }

    /**
     * Tests finding commodities using example-based matching with case-insensitive contains.
     */
    @Test
    @Disabled
    public void findCommodities() {
        Commodity commodity = new Commodity();
        commodity.setName(".");
        ExampleMatcher matcher = ExampleMatcher.matching()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreCase();
        Example<Commodity> example = Example.of(commodity, matcher);
        Page<Commodity> page = commodityRepository.findAll(
            example, PageRequest.of(0, 10)
        );
        Assertions.assertEquals(40, page.getTotalElements());
    }

    /**
     * Verifies the count of recursive commodities for a given commodity group ID.
     */
    @Test
    void countRecursiveCommodities() {
        Assertions.assertEquals(5,
            commodityRepository.findCommoditiesRecursiveByGroupId(303,
                Pageable.ofSize(200)).getTotalElements());
    }
}
