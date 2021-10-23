package seedu.modulink.logic.parser;

import static seedu.modulink.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.modulink.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.modulink.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.modulink.logic.commands.CommandTestUtil.ID_DESC_AMY;
import static seedu.modulink.logic.commands.CommandTestUtil.ID_DESC_BOB;
import static seedu.modulink.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.modulink.logic.commands.CommandTestUtil.INVALID_ID_DESC;
import static seedu.modulink.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.modulink.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.modulink.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.modulink.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.modulink.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.modulink.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.modulink.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.modulink.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.modulink.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.modulink.logic.commands.CommandTestUtil.TAG_DESC_CS2100;
import static seedu.modulink.logic.commands.CommandTestUtil.TAG_DESC_CS2103T;
import static seedu.modulink.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.modulink.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.modulink.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.modulink.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.modulink.logic.commands.CommandTestUtil.VALID_TAG_CS2100;
import static seedu.modulink.logic.commands.CommandTestUtil.VALID_TAG_CS2103T;
import static seedu.modulink.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.modulink.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.modulink.testutil.TypicalPersons.AMY;
import static seedu.modulink.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.modulink.logic.commands.CreateCommand;
import seedu.modulink.model.person.Email;
import seedu.modulink.model.person.Name;
import seedu.modulink.model.person.Person;
import seedu.modulink.model.person.Phone;
import seedu.modulink.model.person.StudentId;
import seedu.modulink.model.tag.Mod;
import seedu.modulink.testutil.PersonBuilder;

public class CreateCommandParserTest {
    private CreateCommandParser parser = new CreateCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).withTags(VALID_TAG_CS2100).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + ID_DESC_BOB
                + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_CS2100, new CreateCommand(expectedPerson));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB + ID_DESC_BOB + PHONE_DESC_BOB
                + EMAIL_DESC_BOB + TAG_DESC_CS2100, new CreateCommand(expectedPerson));

        // multiple student IDs - last student ID accepted
        assertParseSuccess(parser, NAME_DESC_BOB + ID_DESC_AMY
                + ID_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_CS2100, new CreateCommand(expectedPerson));

        // multiple phones - last phone accepted
        assertParseSuccess(parser, NAME_DESC_BOB + ID_DESC_BOB
                + PHONE_DESC_AMY + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_CS2100, new CreateCommand(expectedPerson));

        // multiple emails - last email accepted
        assertParseSuccess(parser, NAME_DESC_BOB + ID_DESC_BOB + PHONE_DESC_BOB
                + EMAIL_DESC_AMY + EMAIL_DESC_BOB
                + TAG_DESC_CS2100, new CreateCommand(expectedPerson));

        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(BOB).withTags(VALID_TAG_CS2100, VALID_TAG_CS2103T)
                .build();
        assertParseSuccess(parser, NAME_DESC_BOB + ID_DESC_BOB
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + TAG_DESC_CS2103T
                + TAG_DESC_CS2100, new CreateCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY + ID_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY,
                new CreateCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CreateCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + ID_DESC_BOB + PHONE_DESC_BOB
                + EMAIL_DESC_BOB, expectedMessage);

        // missing student ID prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_ID_BOB + PHONE_DESC_BOB
                + EMAIL_DESC_BOB, expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB
                + VALID_PHONE_BOB + EMAIL_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB
                + PHONE_DESC_BOB + VALID_EMAIL_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_ID_BOB
                + VALID_PHONE_BOB + VALID_EMAIL_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + ID_DESC_BOB
                + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_CS2103T + TAG_DESC_CS2100, Name.MESSAGE_CONSTRAINTS);

        // invalid student ID
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_ID_DESC
                + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TAG_DESC_CS2103T + TAG_DESC_CS2100, StudentId.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB
                + INVALID_PHONE_DESC + EMAIL_DESC_BOB
                + TAG_DESC_CS2103T + TAG_DESC_CS2100, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB
                + PHONE_DESC_BOB + INVALID_EMAIL_DESC
                + TAG_DESC_CS2103T + TAG_DESC_CS2100, Email.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + ID_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + INVALID_TAG_DESC + VALID_TAG_CS2100, Mod.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + ID_DESC_BOB
                + PHONE_DESC_BOB + EMAIL_DESC_BOB,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + ID_DESC_BOB
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + TAG_DESC_CS2103T + TAG_DESC_CS2100,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CreateCommand.MESSAGE_USAGE));
    }
}