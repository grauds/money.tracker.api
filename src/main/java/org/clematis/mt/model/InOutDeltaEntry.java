package org.clematis.mt.model;

import org.springframework.data.rest.core.config.Projection;

/**
 * @author Anton Troshin
 */
@Projection(name = "inOutDeltaEntry", types = InOutDelta.class)
public interface InOutDeltaEntry {

    CommodityLink getCommodity();

    Double getEtotal();

    Double getItotal();

    Double getDelta();

    MoneyTypeEntry getMoneyType();
}
