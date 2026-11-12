package org.clematis.mt.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * Projection providing link info for an organization.
 */
@Projection(name = "organizationLink", types = Organization.class)
public interface OrganizationLink {

    /** Returns the organization ID. */
    @Value("#{target.id}")
    long getId();

    /** Returns the organization name. */
    String getName();
}
