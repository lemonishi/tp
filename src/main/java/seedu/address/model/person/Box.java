package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Box in Client2Door
 */
public class Box {

    public static final String MESSAGE_CONSTRAINTS = "Box names should be alphanumeric";
    public static final String VALIDATION_REGEX = "\\p{Alnum}+";

    public final String boxName;

    /**
     * Constructs a {@code Box}.
     *
     * @param boxName A valid box name.
     */
    public Box(String boxName) {
        requireNonNull(boxName);
        checkArgument(isValidBoxName(boxName), MESSAGE_CONSTRAINTS);
        this.boxName = boxName;
    }

    public static boolean isValidBoxName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Box)) {
            return false;
        }

        Box otherBox = (Box) other;
        return boxName.equals(otherBox.boxName);
    }

    @Override
    public int hashCode() {
        return boxName.hashCode();
    }

    /**
     * Formats state as text for viewing
     * @return Formatted package name
     */
    @Override
    public String toString() {
        return '[' + boxName + ']';
    }
}
