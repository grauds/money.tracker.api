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
 * @author Anton Troshin
 */
@RestController
public class ExpenseController {

    private final ExpenseRepository expenseRepository;

    public ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("/api/agentCommodityGroupExpenses")
    public ResponseEntity<Page<AgentCommodityGroup>> getAgentCommodityGroupExpenses(
            @RequestParam(value = "code") String code,
            @RequestParam(value = "mois") int mois,
            @RequestParam(value = "an") int an) {

        List<AgentCommodityGroup> groups = this.expenseRepository.getAgentCommodityGroups(code, mois, an);
        Pageable pageRequest = Pageable.ofSize(groups.size());
        Page<AgentCommodityGroup> p = new PageImpl<>(groups, pageRequest, groups.size());

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Page-Number", String.valueOf(p.getNumber()));
        headers.add("X-Page-Size", String.valueOf(p.getSize()));

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(p);
    }
}
