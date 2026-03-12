package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Box;

/**
 * Jackson-friendly version of {@link Box}
 */
public class JsonAdaptedBox {

    private final String boxName;

    /**
     * Constructs a {@code JsonAdaptedBox} with the given {@code boxName}.
     */
    @JsonCreator
    public JsonAdaptedBox(String boxName) {
        this.boxName = boxName;
    }

    /**
     * Converts a given {@code Box} into this class for Jackson use.
     */
    public JsonAdaptedBox(Box source) {
        boxName = source.boxName;
    }

    @JsonValue
    public String getBoxName() {
        return boxName;
    }

    /**
     * Converts this Jackson-friendly adapted box object into the model's {@code Box} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted box.
     */
    public Box toModelType() throws IllegalValueException {
        if (!Box.isValidBoxName(boxName)) {
            throw new IllegalValueException(Box.MESSAGE_CONSTRAINTS);
        }
        return new Box(boxName);
    }
}
