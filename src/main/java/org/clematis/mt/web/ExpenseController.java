package org.clematis.mt.web;

import java.util.List;

import org.clematis.mt.dto.AgentCommodityGroup;
import org.clematis.mt.repository.ExpenseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST controller for expense-related API endpoints.
 * Provides methods for retrieving expense aggregates grouped by agent and commodity group.
 */
@RestController
public class ExpenseController {

    public static final String X_PAGE_NUMBER = "X-Page-Number";
    public static final String X_PAGE_SIZE = "X-Page-Size";

    private final ExpenseRepository expenseRepository;

    public ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    /**
     * Retrieves expense aggregates grouped by agent and commodity group within a specified date range.
     * Returns paginated results with page metadata headers.
     *
     * @param code       target currency code for conversion
     * @param moisStart  starting month (1-12)
     * @param anStart    starting year
     * @param moisEnd    ending month (1-12)
     * @param anEnd      ending year
     * @return paginated response with expense aggregates and page metadata headers
     */
    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @GetMapping("/api/agentCommodityGroupExpenses")
    public ResponseEntity<Page<AgentCommodityGroup>> getAgentCommodityGroupExpenses(
            @RequestParam(value = "code") String code,
            @RequestParam(value = "moisStart") int moisStart,
            @RequestParam(value = "anStart") int anStart,
            @RequestParam(value = "moisEnd") int moisEnd,
            @RequestParam(value = "anEnd") int anEnd) {

        List<AgentCommodityGroup> groups = this.expenseRepository.getAgentCommodityGroups(
            code, moisStart, anStart, moisEnd, anEnd
        );
        if (groups.isEmpty()) {
            Pageable pageRequest = Pageable.unpaged();
            Page<AgentCommodityGroup> p = Page.empty(pageRequest);

            HttpHeaders headers = new HttpHeaders();
            headers.add(X_PAGE_NUMBER, "0");
            headers.add(X_PAGE_SIZE, "0");

            return ResponseEntity
                .ok()
                .headers(headers)
                .body(p);
        } else {
            Pageable pageRequest = Pageable.ofSize(groups.size());
            Page<AgentCommodityGroup> p = new PageImpl<>(groups, pageRequest, groups.size());

            HttpHeaders headers = new HttpHeaders();
            headers.add(X_PAGE_NUMBER, String.valueOf(p.getNumber()));
            headers.add(X_PAGE_SIZE, String.valueOf(p.getSize()));

            return ResponseEntity
                .ok()
                .headers(headers)
                .body(p);
        }
    }
}
