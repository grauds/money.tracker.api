package org.clematis.mt.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.clematis.mt.model.OperationEntry;
import org.clematis.mt.model.OperationEntryKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for OperationEntryIdConverter covering branch conditions.
 */
@SuppressWarnings("checkstyle:MultipleStringLiterals")
public class OperationEntryIdConverterTest {

    private static final String DELIMITER = "_";

    private final OperationEntryIdConverter converter = new OperationEntryIdConverter();

    /**
     * Verifies fromRequestId accepts the valid format: "dd-MM-yyyy_accountId".
     * Covers if branch: parts.length == PARTS.
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    @Test
    public void testFromRequestIdValidFormat() {
        String validId = "01-01-2024" + DELIMITER + "5";
        OperationEntryKey key = (OperationEntryKey) converter.fromRequestId(
            validId,
            OperationEntry.class
        );

        Assertions.assertNotNull(key);
        Assertions.assertEquals(5, key.getAccount());
    }

    /**
     * Verifies fromRequestId throws IllegalArgumentException for bad format.
     * Covers the else branch: parts.length != PARTS.
     */
    @Test
    public void testFromRequestIdBadFormatNoDelimiter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            converter.fromRequestId("01-01-2024", OperationEntry.class);
        });
    }

    /**
     * Verifies fromRequestId throws IllegalArgumentException for too many parts.
     * Covers the else branch: parts.length != PARTS (more than 2).
     */
    @Test
    public void testFromRequestIdBadFormatTooManyDelimiters() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            converter.fromRequestId("01-01-2024_5_extra",
                OperationEntry.class);
        });
    }

    /**
     * Verifies fromRequestId throws IllegalArgumentException for invalid date.
     * Covers the catch branch: ParseException.
     */
    @Test
    public void testFromRequestIdInvalidDate() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> converter.fromRequestId("not-a-date_5",
            OperationEntry.class)
        );
    }

    /**
     * Verifies fromRequestId throws IllegalArgumentException for a non-numeric account.
     * Covers the catch branch: NumberFormatException.
     */
    @Test
    public void testFromRequestIdNonNumericAccountId() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> converter.fromRequestId("01-01-2024" + DELIMITER + "abc",
            OperationEntry.class)
        );
    }

    /**
     * Verifies toRequestId produces the correct format from OperationEntryKey.
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    @Test
    public void testToRequestId() {
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse("15-06-2024");
            OperationEntryKey key = new OperationEntryKey(date, 10);
            String requestId = converter.toRequestId(
                key,
                OperationEntry.class
            );

            Assertions.assertEquals("15-06-2024_10", requestId);
        } catch (ParseException e) {
            Assertions.fail("Should not throw ParseException for valid date");
        }
    }

    /**
     * Verifies supports returns true for OperationEntryKey.
     */
    @Test
    public void testSupportsForOperationEntryKey() {
        Assertions.assertTrue(converter.supports(OperationEntryKey.class));
    }

    /**
     * Verifies supports returns false for unrelated class.
     */
    @Test
    public void testSupportsForUnrelatedClass() {
        Assertions.assertFalse(converter.supports(String.class));
    }
}
