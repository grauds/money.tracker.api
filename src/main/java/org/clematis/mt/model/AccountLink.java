package org.clematis.mt.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * @author Anton Troshin
 */
@Projection(name = "accountLink", types = Account.class)
public interface AccountLink {

    @Value("#{target.id}")
    long getId();

    String getName();
}
