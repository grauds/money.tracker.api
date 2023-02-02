package org.clematis.mt.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * @author Anton Troshin
 */
@Projection(name = "organizationLink", types = Organization.class)
public interface OrganizationLink {

    @Value("#{target.id}")
    long getId();

    String getName();
}
