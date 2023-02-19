package org.clematis.mt.web;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.clematis.mt.model.OperationEntryKey;
import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.stereotype.Component;
/**
 * Class to convert a composite id to a string and back
 * Tailored for <code>org.clematis.mt.model.OperationEntry</code> to make it hunan readable
 *
 * @author Anton Troshin
 */
@Component
public class OperationEntryIdConverter implements BackendIdConverter {

    public static final String DELIMITER = "_";

    public static final int PARTS = 2;

    private static final String DATE_FORMAT = "dd-MM-yyyy";

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
        try {
            if (parts.length == PARTS) {
                return new OperationEntryKey(new SimpleDateFormat(DATE_FORMAT).parse(parts[i]),
                        Integer.parseInt(parts[++i]));
            } else {
                throw new ParseException("Bad id format", 0);
            }
        } catch (ParseException | NumberFormatException e) {
            throw new IllegalArgumentException("Id must consist of a date and an account id separated by '_'", e);
        }
    }

    /**
     * Returns the id to be used in the URI generated to point to an entity of the given type with the given id.
     *
     * @param id         the entity's id, will never be {@literal null}.
     * @param entityType the type of the entity to expose.
     * @return the id to be used in the URI
     */
    @Override
    public String toRequestId(Serializable id, Class<?> entityType) {
        OperationEntryKey key = (OperationEntryKey) id;
        return new SimpleDateFormat(DATE_FORMAT).format(key.getTransferDate()) + DELIMITER + key.getAccount();
    }

    /**
     * Returns if a plugin should be invoked according to the given delimiter.
     *
     * @param delimiter must not be {@literal null}.
     * @return if the plugin should be invoked
     */
    @Override
    public boolean supports(Class<?> delimiter) {
        return delimiter.isAssignableFrom(OperationEntryKey.class);
    }
}
