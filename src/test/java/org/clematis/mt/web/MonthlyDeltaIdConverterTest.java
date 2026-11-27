package org.clematis.mt.web;

import org.clematis.mt.model.MonthlyDelta;
import org.clematis.mt.model.MonthlyDeltaKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for MonthlyDeltaIdConverter covering branch conditions.
 */
@SuppressWarnings({"checkstyle:MultipleStringLiterals", "checkstyle:MagicNumber"})
public class MonthlyDeltaIdConverterTest {

    private static final String DELIMITER = "_";

    private final MonthlyDeltaIdConverter converter = new MonthlyDeltaIdConverter();

    /**
     * Verifies fromRequestId accepts valid format: "year_month_code".
     * Covers the if branch: parts.length == PARTS.
     */
    @Test
    public void testFromRequestIdValidFormat() {
        String validId = "2024" + DELIMITER + "1" + DELIMITER + "RUB";
        MonthlyDeltaKey key = (MonthlyDeltaKey) converter.fromRequestId(
            validId,
            MonthlyDelta.class
        );

        Assertions.assertNotNull(key);
        // Note: converter passes parts[0] as mois and parts[1] as an
        Assertions.assertEquals(2024, key.getMois());
        Assertions.assertEquals(1, key.getAn());
        Assertions.assertEquals("RUB", key.getCode());
    }

    /**
     * Verifies fromRequestId throws IllegalArgumentException for bad format.
     * Covers the else branch: parts.length != PARTS.
     */
    @Test
    public void testFromRequestIdBadFormatOnlyTwoParts() {
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> converter.fromRequestId(
                "2024" + DELIMITER + "1",
                MonthlyDelta.class
            )
        );
    }

    /**
     * Verifies fromRequestId throws IllegalArgumentException for single part.
     * Covers the else branch: parts.length != PARTS.
     */
    @Test
    public void testFromRequestIdBadFormatSinglePart() {
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> converter.fromRequestId(
                "2024",
                MonthlyDelta.class
            )
        );
    }

    /**
     * Verifies fromRequestId throws IllegalArgumentException for non-numeric year.
     * Covers the catch branch: NumberFormatException.
     */
    @Test
    public void testFromRequestIdNonNumericYear() {
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> converter.fromRequestId(
                "abc" + DELIMITER + "1" + DELIMITER + "RUB",
                MonthlyDelta.class
            )
        );
    }

    /**
     * Verifies fromRequestId throws IllegalArgumentException for non-numeric month.
     * Covers the catch branch: NumberFormatException.
     */
    @Test
    public void testFromRequestIdNonNumericMonth() {
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> converter.fromRequestId(
                "2024" + DELIMITER + "abc" + DELIMITER + "RUB",
                MonthlyDelta.class
            )
        );
    }

    /**
     * Verifies toRequestId produces correct format from MonthlyDeltaKey.
     */
    @Test
    public void testToRequestId() {
        MonthlyDeltaKey key = new MonthlyDeltaKey(6, 2024, "USD");
        String requestId = converter.toRequestId(key, MonthlyDelta.class);

        Assertions.assertEquals("2024_6_USD", requestId);
    }

    /**
     * Verifies supports returns true for MonthlyDelta.
     */
    @Test
    public void testSupportsForMonthlyDelta() {
        Assertions.assertTrue(converter.supports(MonthlyDelta.class));
    }

    /**
     * Verifies supports returns false for unrelated class.
     */
    @Test
    public void testSupportsForUnrelatedClass() {
        Assertions.assertFalse(converter.supports(String.class));
    }
}
