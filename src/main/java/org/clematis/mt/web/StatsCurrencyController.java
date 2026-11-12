package org.clematis.mt.web;

import java.util.List;

import org.clematis.mt.dto.MoneyShareInCurrency;
import org.clematis.mt.repository.ExpenseRepository;
import org.clematis.mt.repository.IncomeRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RepositoryRestController
public class StatsCurrencyController {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    public StatsCurrencyController(
        IncomeRepository incomeRepository,
        ExpenseRepository expenseRepository
    ) {
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("/income/search/total")
    public ResponseEntity<Double> getIncomeTotal(@RequestParam("code") String code) {
        return ResponseEntity.ok(incomeRepository.sumTotalInCurrency(code));
    }

    @GetMapping("/income/search/totalByCurrency")
    public ResponseEntity<List<MoneyShareInCurrency>> getIncomeByCurrency(
        @RequestParam("code") String code
    ) {
        return ResponseEntity.ok(incomeRepository.getIncomeByCurrency(code));
    }

    @GetMapping("/expenses/search/total")
    public ResponseEntity<Double> getExpenseTotal(@RequestParam("code") String code) {
        return ResponseEntity.ok(expenseRepository.sumTotalInCurrency(code));
    }

    @GetMapping("/expenses/search/totalByCurrency")
    public ResponseEntity<List<MoneyShareInCurrency>> getExpenseByCurrency(
        @RequestParam("code") String code
    ) {
        return ResponseEntity.ok(expenseRepository.getExpensesByCurrency(code));
    }
}
