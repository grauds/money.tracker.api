package org.clematis.mt.model.views;

import org.clematis.mt.model.CommodityLink;
import org.clematis.mt.model.MoneyTypeEntry;
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
