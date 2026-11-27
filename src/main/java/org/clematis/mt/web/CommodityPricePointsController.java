package org.clematis.mt.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.clematis.mt.dto.PurchasePricePointDTO;
import org.clematis.mt.model.Commodity;
import org.clematis.mt.repository.CommodityRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST controller for commodity purchase price points.
 * Provides endpoints for retrieving purchase price history for commodities.
 */
@RestController
public class CommodityPricePointsController {

    private final CommodityRepository commodityRepository;

    public CommodityPricePointsController(CommodityRepository commodityRepository) {
        this.commodityRepository = commodityRepository;
    }

    /**
     * Retrieves all purchase price points for a given commodity.
     * Returns purchase records with tradeplace name, transfer date, price, and currency code.
     *
     * @param id the commodity ID
     * @return list of purchase price points
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    @GetMapping("/api/commodities/{id}/purchasePricePoints")
    public ResponseEntity<List<PurchasePricePointDTO>> getPurchasePricePoints(@PathVariable("id") Integer id) {

        Commodity commodity = commodityRepository.findById(id)
            .orElse(null);

        if (commodity == null) {
            return ResponseEntity.notFound().build();
        }

        List<Object[]> rows = commodityRepository.findPurchasePricePointsByCommodityId(id);

        List<PurchasePricePointDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            PurchasePricePointDTO dto = new PurchasePricePointDTO();
            dto.setTradeplaceName((String) row[0]);
            dto.setTradeplaceId((Integer) row[1]);
            dto.setTransferDate((Date) row[2]);
            dto.setPrice((Double) row[3]);
            dto.setCurrencyCode((String) row[4]);
            result.add(dto);
        }

        return ResponseEntity.ok(result);
    }
}
