package org.clematis.mt.model;

import org.springframework.data.rest.core.config.Projection;
/**
 * @author Anton Troshin
 */
@Projection(name = "moneyTypeEntry", types = MoneyType.class)
public interface MoneyTypeEntry {

    String getCode();

    String getSign();
}
