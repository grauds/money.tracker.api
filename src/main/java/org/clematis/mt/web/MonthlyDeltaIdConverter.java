package org.clematis.mt.web;

import java.io.Serializable;

import org.clematis.mt.model.views.MonthlyDelta;
import org.clematis.mt.model.views.MonthlyDeltaKey;
import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.stereotype.Component;

/**
 * Class to convert a composite id to a string and back
 * Tailored for <code>org.clematis.mt.model.views.MonthlyDelta</code> to make it hunan readable
 *
 * @author Anton Troshin
 */
@Component
public class MonthlyDeltaIdConverter implements BackendIdConverter {

    public static final String DELIMITER = "_";
    public static final int PARTS = 3;

    /**
     * Returns the id of the entity to be looked up eventually.
     *
     * @param id         the source id as it was parsed from the incoming request, will never be {@literal null}.
     * @param entityType the type of the object to be resolved, will never be {@literal null}.
     * @return must not be {@literal null}.
     */
    @Override
    public Serializable fromRequestId(String id, Class<?> entityType) {
        String[] parts = id.split(DELIMITER, PARTS);

        int i = 0;
        if (parts.length == PARTS) {
            return new MonthlyDeltaKey(Integer.parseInt(parts[i]),
                    Integer.parseInt(parts[++i]),
                    parts[++i]);
        } else {
            throw new IllegalArgumentException("Id must consist of 3 chunks separated by '_'");
        }
    }

    /**
     * Returns the id to be used in the URI generated to point to an entity of the given type with the given id.
     *
     * @param id         the entity's id, will never be {@literal null}.
     * @param entityType the type of the entity to expose.
     * @return a human-readable string
     */
    @Override
    public String toRequestId(Serializable id, Class<?> entityType) {
        MonthlyDeltaKey monthlyDelta = (MonthlyDeltaKey) id;
        return monthlyDelta.getAn()
                + DELIMITER + monthlyDelta.getMois()
                + DELIMITER + monthlyDelta.getCode();
    }

    /**
     * Returns if a plugin should be invoked according to the given delimiter.
     *
     * @param delimiter must not be {@literal null}.
     * @return if the plugin should be invoked
     */
    @Override
    public boolean supports(Class<?> delimiter) {
        return delimiter.isAssignableFrom(MonthlyDelta.class);
    }
}
