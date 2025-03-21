package org.example.bank.api;


import org.example.bank.domain.Category;
import org.example.bank.domain.Operation;
import org.example.bank.facade.FinancialFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "http://localhost:5173") // Разрешаем только React
public class AnalyticsController {

    private final FinancialFacade financialFacade;

    public AnalyticsController(FinancialFacade financialFacade) {
        this.financialFacade = financialFacade;
    }

    @GetMapping("/income-expense-difference")
    public ResponseEntity<BigDecimal> getIncomeExpenseDifference(@RequestParam("start") String start,
                                                                 @RequestParam("end") String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        BigDecimal difference = financialFacade.getIncomeExpenseDifference(startDate, endDate);
        return ResponseEntity.ok(difference);
    }

    @GetMapping("/group-operations")
    public ResponseEntity<Map<Category, List<Operation>>> getOperationsGroupedByCategory() {
        Map<Category, List<Operation>> grouped = financialFacade.getOperationsGroupedByCategory();
        return ResponseEntity.ok(grouped);
    }
}
