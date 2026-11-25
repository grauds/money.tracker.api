package org.clematis.mt.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.clematis.mt.dto.AccountMonthlyBalanceSummaryDTO;
import org.clematis.mt.repository.MonthlyDeltaRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {

    private static final String DELIM = "-";
    private final MonthlyDeltaRepository repository;

    public AccountsController(MonthlyDeltaRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/monthly/summary")
    @ResponseBody
    public ResponseEntity<CollectionModel<AccountMonthlyBalanceSummaryDTO>> getSummaryInCurrency(
        @RequestParam("code") String code,
        @RequestParam("moisStart") int moisStart,
        @RequestParam("anStart") int anStart,
        @RequestParam("moisEnd") int moisEnd,
        @RequestParam("anEnd") int anEnd,
        HttpServletRequest request) {

        String requestUrl = request.getRequestURL().toString();

        List<AccountMonthlyBalanceSummaryDTO> dtos = repository
            .getSummaryInCurrency(code, moisStart, anStart, moisEnd, anEnd)
            .stream()
            .map(AccountMonthlyBalanceSummaryDTO::new)
            .collect(Collectors.toList());

        String fullSelfUrl = requestUrl + "?" + request.getQueryString();
        return ResponseEntity.ok(CollectionModel.of(dtos, Link.of(fullSelfUrl).withSelfRel()));
    }
}
