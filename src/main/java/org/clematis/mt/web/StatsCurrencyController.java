package org.clematis.mt.web;

import java.util.List;

import org.clematis.mt.dto.EntityAmountProjection;
import org.clematis.mt.dto.MoneyShareInCurrency;
import org.clematis.mt.repository.ExpenseRepository;
import org.clematis.mt.repository.IncomeRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * REST controller for currency statistics endpoints.
 * Provides methods for retrieving income and expense totals and breakdowns by currency.
 */
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

    /**
     * Retrieves the total income amount converted to the specified currency.
     *
     * @param code target currency code for conversion
     * @return response containing the total income in the specified currency
     */
    @GetMapping("/income/search/total")
    public ResponseEntity<Double> getIncomeTotal(@RequestParam("code") String code) {
        return ResponseEntity.ok(incomeRepository.sumTotalInCurrency(code));
    }

    /**
     * Retrieves income breakdown by each currency, converted to the target currency.
     *
     * @param code target currency code for conversion
     * @return response containing income shares per currency
     */
    @GetMapping("/income/search/totalByCurrency")
    public ResponseEntity<List<MoneyShareInCurrency>> getIncomeByCurrency(
        @RequestParam("code") String code
    ) {
        return ResponseEntity.ok(incomeRepository.getIncomeByCurrency(code));
    }

    /**
     * Retrieves income breakdown by organizations.
     * @param code target currency code
     * @return income shares per organization
     */
    @GetMapping("/income/search/totalByOrganizations")
    public ResponseEntity<List<EntityAmountProjection>> getIncomeByOrganizations(
        @RequestParam("code") String code
    ) {
        return ResponseEntity.ok(incomeRepository.getIncomeByOrganizations(code));
    }

    /**
     * Retrieves the total expense amount converted to the specified currency.
     *
     * @param code target currency code for conversion
     * @return response containing the total expenses in the specified currency
     */
    @GetMapping("/expenses/search/total")
    public ResponseEntity<Double> getExpenseTotal(@RequestParam("code") String code) {
        return ResponseEntity.ok(expenseRepository.sumTotalInCurrency(code));
    }

    /**
     * Retrieves expense breakdown by each currency, converted to the target currency.
     *
     * @param code target currency code for conversion
     * @return response containing expense shares per currency
     */
    @GetMapping("/expenses/search/totalByCurrency")
    public ResponseEntity<List<MoneyShareInCurrency>> getExpenseByCurrency(
        @RequestParam("code") String code
    ) {
        return ResponseEntity.ok(expenseRepository.getExpensesByCurrency(code));
    }

    /**
     * Retrieves expense breakdown by commodity categories
     * @param code target currency code
     * @return list of expense shares per commodity categories
     */
    @GetMapping("/expenses/search/totalByCategories")
    public ResponseEntity<List<EntityAmountProjection>> getExpenseByCategories(
        @RequestParam("code") String code
    ) {
        return ResponseEntity.ok(expenseRepository.getExpenseByCategories(code));
    }
}
