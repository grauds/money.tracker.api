package org.clematis.mt.web;

import org.clematis.mt.dto.InfoAbout;
import org.clematis.mt.repository.AccountRepository;
import org.clematis.mt.repository.ExpenseItemRepository;
import org.clematis.mt.repository.ExpenseRepository;
import org.clematis.mt.repository.IncomeRepository;
import org.clematis.mt.repository.OrganizationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Anton Troshin
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

    @GetMapping("/about")
    public ResponseEntity<InfoAbout> getInfoAbout() {
        InfoAbout infoAbout = new InfoAbout();

        infoAbout.setExpenses(expenseRepository.count());
        infoAbout.setIncome(incomeRepository.count());
        infoAbout.setOrganizations(organizationRepository.count());
        infoAbout.setAccounts(accountRepository.count());
        infoAbout.setDates(expenseRepository.getDatesRange());
        infoAbout.setExpensesNoCommodity(expenseItemRepository.countItemsWithNoCommodity());
        infoAbout.setExpensesNoTradeplace(expenseItemRepository.countItemsWithNoTradeplace());
        infoAbout.setExpensesTradeplace(expenseItemRepository.countItemsWithTradeplace());

        return ResponseEntity
            .ok()
            .body(infoAbout);
    }
}
