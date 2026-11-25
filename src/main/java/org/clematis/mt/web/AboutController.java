package org.clematis.mt.web;

import org.clematis.mt.dto.GeneralStatsDTO;
import org.clematis.mt.repository.AccountRepository;
import org.clematis.mt.repository.ExpenseItemRepository;
import org.clematis.mt.repository.ExpenseRepository;
import org.clematis.mt.repository.IncomeRepository;
import org.clematis.mt.repository.OrganizationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller providing application-wide statistics.
 */
@RestController
public class AboutController {

    private final ExpenseRepository expenseRepository;

    private final ExpenseItemRepository expenseItemRepository;

    private final IncomeRepository incomeRepository;

    private final OrganizationRepository organizationRepository;

    private final AccountRepository accountRepository;

    public AboutController(ExpenseRepository expenseRepository,
                           ExpenseItemRepository expenseItemRepository,
                           IncomeRepository incomeRepository,
                           OrganizationRepository organizationRepository,
                           AccountRepository accountRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.expenseItemRepository = expenseItemRepository;
        this.incomeRepository = incomeRepository;
        this.organizationRepository = organizationRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Returns application-wide statistics.
     * @return response entity with statistics
     */
    @GetMapping("/about")
    public ResponseEntity<GeneralStatsDTO> getInfoAbout() {
        GeneralStatsDTO generalStatsDTO = new GeneralStatsDTO();

        generalStatsDTO.setExpenses(expenseRepository.count());
        generalStatsDTO.setIncome(incomeRepository.count());
        generalStatsDTO.setOrganizations(organizationRepository.count());
        generalStatsDTO.setAccounts(accountRepository.count());
        generalStatsDTO.setDates(expenseRepository.getDatesRange());
        generalStatsDTO.setExpensesNoCommodity(expenseItemRepository.countItemsWithNoCommodity());
        generalStatsDTO.setExpensesNoTradeplace(expenseItemRepository.countItemsWithNoTradeplace());
        generalStatsDTO.setExpensesTradeplace(expenseItemRepository.countItemsWithTradeplace());

        return ResponseEntity
            .ok()
            .body(generalStatsDTO);
    }
}
