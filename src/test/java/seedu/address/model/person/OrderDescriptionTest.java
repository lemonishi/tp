package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class OrderDescriptionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new OrderDescription(null));
    }

    @Test
    public void constructor_invalidOrderDescription_throwsIllegalArgumentException() {
        String invalidOrderDescription = "#cake";
        assertThrows(IllegalArgumentException.class, () -> new OrderDescription(invalidOrderDescription));
    }

    @Test
    public void isValidOrderDescription() {
        // null order description
        assertThrows(NullPointerException.class, () -> OrderDescription.isValidOrderDescription(null));

        // invalid order descriptions
        assertFalse(OrderDescription.isValidOrderDescription("")); // empty string
        assertFalse(OrderDescription.isValidOrderDescription(" ")); // spaces only
        assertFalse(OrderDescription.isValidOrderDescription("#")); // only non-alphanumeric characters
        assertFalse(OrderDescription.isValidOrderDescription("cake!")); // contains non-alphanumeric characters

        // valid order descriptions
        assertTrue(OrderDescription.isValidOrderDescription("2 cakes")); // alphanumeric with spaces
        assertTrue(OrderDescription.isValidOrderDescription("12345")); // numbers only
        assertTrue(OrderDescription.isValidOrderDescription("Large Latte")); // letters with capitals
    }

    @Test
    public void equals() {
        OrderDescription orderDescription = new OrderDescription("2 cakes");

        // same values -> returns true
        assertTrue(orderDescription.equals(new OrderDescription("2 cakes")));

        // same object -> returns true
        assertTrue(orderDescription.equals(orderDescription));

        // null -> returns false
        assertFalse(orderDescription.equals(null));

        // different types -> returns false
        assertFalse(orderDescription.equals(5.0f));

        // different values -> returns false
        assertFalse(orderDescription.equals(new OrderDescription("3 cakes")));
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        OrderDescription first = new OrderDescription("2 cakes");
        OrderDescription second = new OrderDescription("2 cakes");
        assertEquals(first.hashCode(), second.hashCode());
    }
}
