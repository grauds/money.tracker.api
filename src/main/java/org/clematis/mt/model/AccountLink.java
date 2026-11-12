package org.clematis.mt.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * Projection providing link info for an account.
 */
@Projection(name = "accountLink", types = Account.class)
public interface AccountLink {

    /**
     * Returns the account id.
     * @return account id
     */
    @Value("#{target.id}")
    long getId();

    /**
     * Returns the account name.
     * @return account name
     */
    String getName();
}
