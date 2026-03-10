package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class DeliveryStatusTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DeliveryStatus(null));
    }

    @Test
    public void constructor_invalidDeliveryStatus_throwsIllegalArgumentException() {
        String invalidDeliveryStatus = "maybe";
        assertThrows(IllegalArgumentException.class, () -> new DeliveryStatus(invalidDeliveryStatus));
    }

    @Test
    public void isValidDeliveryStatus() {
        // null delivery status
        assertThrows(NullPointerException.class, () -> DeliveryStatus.isValidDeliveryStatus(null));

        // invalid delivery status
        assertFalse(DeliveryStatus.isValidDeliveryStatus("")); // empty string
        assertFalse(DeliveryStatus.isValidDeliveryStatus(" ")); // spaces only
        assertFalse(DeliveryStatus.isValidDeliveryStatus("^")); // only non-alphanumeric characters
        assertFalse(DeliveryStatus.isValidDeliveryStatus("pending*")); // contains non-alphanumeric characters
        assertFalse(DeliveryStatus.isValidDeliveryStatus("Pending")); // wrong case
        assertFalse(DeliveryStatus.isValidDeliveryStatus("DELIVERED")); // wrong case
        assertFalse(DeliveryStatus.isValidDeliveryStatus("out_for_delivery")); // underscore instead of hyphen

        // valid delivery status
        assertTrue(DeliveryStatus.isValidDeliveryStatus("pending"));
        assertTrue(DeliveryStatus.isValidDeliveryStatus("preparing"));
        assertTrue(DeliveryStatus.isValidDeliveryStatus("out-for-delivery"));
        assertTrue(DeliveryStatus.isValidDeliveryStatus("delivered"));
    }

    @Test
    public void equals() {
        DeliveryStatus deliveryStatus = new DeliveryStatus("delivered");

        // same values -> returns true
        assertTrue(deliveryStatus.equals(new DeliveryStatus("delivered")));

        // same object -> returns true
        assertTrue(deliveryStatus.equals(deliveryStatus));

        // null -> returns false
        assertFalse(deliveryStatus.equals(null));

        // different types -> returns false
        assertFalse(deliveryStatus.equals(5.0f));

        // different values -> returns false
        assertFalse(deliveryStatus.equals(new DeliveryStatus("pending")));
    }
}
