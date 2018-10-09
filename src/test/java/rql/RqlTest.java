package rql;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import rql.Rql;
import rql.Settings;
import rql.exceptions.RqlException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RqlTest
{
    @Test
    public void testStripNewlines()
            throws Exception
    {
        Settings settings = new Settings();
        settings.setStripNewlines(true);
        assertEquals(Rql.parse("\n\n{name}\n\n", settings).render(of("name", "tobi")), "'tobi'");
    }

    @Test
    public void testResultIsHasInvalidType()
            throws Exception
    {
        throwException("{tobi.age}", of("tobi", of("age", new Double(3.141))),
                "The result of 'tobi.age' has an invalid type 'java.lang.Double'");
    }

    @Test
    public void testArrayElementIsNull()
            throws Exception
    {
        ArrayList array = new ArrayList();
        array.add(null);
        throwException("{tobi[0]}", of("tobi", array), "The result of 'tobi[0]' is null");
    }

    @Test
    public void testObjectMemberNull()
            throws Exception
    {
        HashMap map = new HashMap();
        map.put("age", null);

        throwException("{tobi.age}", of("tobi", map),
                "The result of 'tobi.age' is null");
    }

    @Test
    public void testIfElseStatementFailed()
            throws Exception
    {
        throwException("[{tobi}]({mobi})", ImmutableMap.<String, Object>of(),
                "The if-else statement '[{tobi}]({mobi})' failed");
    }

    @Test
    public void testConflictingTypes()
            throws Exception
    {
        throwException("{ar} {ar.size} {ar[0]}", ImmutableMap.<String, Object>of(),
                "Variable 'ar' has conflicting types. [ 'PRIMITIVE', 'OBJECT', 'ARRAY' ]");
    }

    @Test
    public void testVariableAndOptionalNotExist()
            throws Exception
    {
        render("hi miss {j} and [ms {m}](mr {t})", of("j", "jobi", "t", "tobi"), "hi miss 'jobi' and mr 'tobi'");
    }

    @Test
    public void testVariableAndOptionalExist()
            throws Exception
    {
        render("hi miss {j} and [ms {m}](mr {t})", of("j", "jobi", "m", "mobi"), "hi miss 'jobi' and ms 'mobi'");
    }

    @Test
    public void testMultipleVariables()
            throws Exception
    {
        render("hi {j}, {m} and {t}", of("j", "jobi", "m", "mobi", "t", "tobi"), "hi 'jobi', 'mobi' and 'tobi'");
    }

    @Test
    public void testMissingClosingCodeTagInElseTag()
            throws Exception
    {
        throwExceptionRegex("[]({", ImmutableMap.<String, Object>of(),
                "No '\\}' found starting from line (.+), index (.+)");
    }

    @Test
    public void testMissingClosingCodeTagInIfTag()
            throws Exception
    {
        throwExceptionRegex("[{", ImmutableMap.<String, Object>of(),
                "No '\\}' found starting from line (.+), index (.+)");
    }

    @Test
    public void testMissingClosingElseTag()
            throws Exception
    {
        throwExceptionRegex("[](", ImmutableMap.<String, Object>of(),
                "No '\\)' found starting from line (.+), index (.+)");
    }

    @Test
    public void testNonStartingElseTag()
            throws Exception
    {
        throwExceptionRegex("[]_", ImmutableMap.<String, Object>of(),
                "No '\\(' found on line (.+), index (.+)");
    }

    @Test
    public void testMissingStartingElseTag()
            throws Exception
    {
        throwExceptionRegex("[]", ImmutableMap.<String, Object>of(),
                "No '\\(' found on line (.+), index (.+)");
    }

    @Test
    public void testMissingClosingIfTag()
            throws Exception
    {
        throwExceptionRegex("[", ImmutableMap.<String, Object>of(),
                "No '\\]' found starting from line (.+), index (.+)");
    }

    @Test
    public void testMissingClosingCodeTag()
            throws Exception
    {
        throwExceptionRegex("{", ImmutableMap.<String, Object>of(),
                "No '\\}' found starting from line (.+), index (.+)");
    }

    @Test
    public void testSimpleArrayNestedNonNumericalIndex()
            throws Exception
    {
        throwExceptionRegex("hi {names[nested.index]}", of("names", ImmutableList.of("tobi", "mobi"), "nested", of("index", "hello"))
                , "'nested.index' cannot be used as the index of the array 'names'");
    }

    @Test
    public void testSimpleArrayNestedNumericalIndex()
            throws Exception
    {
        render("hi {names[nested.index]}", of("names", ImmutableList.of("tobi", "mobi"),
                "nested", of("index", 1)), "hi 'mobi'");
    }

    @Test
    public void testSimpleArrayIndex()
            throws Exception
    {
        render("hi {names[index]}",
                of("names", ImmutableList.of("tobi", "mobi"), "index", 1), "hi 'mobi'");
    }

    @Test
    public void testNestedObjectNumber()
            throws Exception
    {
        render("hi {tobi.address.postcode}",
                of("tobi", of("address", of("postcode", 69123))), "hi 69123");
    }

    @Test
    public void testNestedObjectString()
            throws Exception
    {
        render("hi {tobi.address.city}", of("tobi", of("address", of("city", "lyon"))), "hi 'lyon'");
    }

    @Test
    public void testSimpleArrayNumber()
            throws Exception
    {
        render("hi {factorials[3]}", of("factorials", ImmutableList.of(1, 2, 6, 24, 120)), "hi 24");
    }

    @Test
    public void testSimpleArrayString()
            throws Exception
    {
        render("hi {names[0]}", of("names", ImmutableList.of("tobi", "mobi")), "hi 'tobi'");
    }

    @Test
    public void testIfElseEndEscape()
            throws Exception
    {
        render("[\\]\\\\](), [{missing}](\\)\\\\)",
                of("name", "mobi"), "]\\, )\\");
    }

    @Test
    public void testElseStartEscape()
            throws Exception
    {
        render("\\\\[{missing}]({name}),  \\\\\\\\[{missing}]({name})",
                of("name", "mobi"), "\\'mobi',  \\\\'mobi'");
    }

    @Test
    public void testIfStartEscape()
            throws Exception
    {
        render("\\\\[{name}](), \\\\\\[{name}](), \\\\\\\\[{name}](), \\\\\\\\\\[{name}]()",
                of("name", "mobi"), "\\'mobi', \\['mobi'](), \\\\'mobi', \\\\['mobi']()");
    }

    @Test
    public void testCodeStartEscape()
            throws Exception
    {
        render("\\\\{name}, \\\\\\{name}, \\\\\\\\{name}, \\\\\\\\\\{name}",
                of("name", "mobi"), "\\'mobi', \\{name}, \\\\'mobi', \\\\{name}");
    }

    @Test
    public void testMiddleEscape()
            throws Exception
    {
        render("20 / 2 = [40 / 4]() = [{missing}](30 / 3)", ImmutableMap.<String, Object>of(), "20 / 2 = 40 / 4 = 30 / 3");
    }

    @Test
    public void testSimpleStringEscape()
            throws Exception
    {
        render("hi \\{name}", of("name", "mobi"), "hi {name}");
    }

    @Test
    public void testSimpleStringNotExist()
            throws Exception
    {
        throwException("hi {name}", ImmutableMap.<String, Object>of(), "Variable 'name' doesn't exist");
    }

    @Test
    public void testSimpleStringObjectMismatch()
            throws Exception
    {
        throwException("hi {name.test}", of("name", "mobi"), "Variable 'name' is not an object");
    }

    @Test
    public void testSimpleObjectPropertyDoesNotExist()
            throws Exception
    {
        throwException("hi {name.test}", of("name", of("test1", 1)), "Variable 'name' doesn't have a property 'test'. It has the following properties: 'test1'");
    }

    @Test
    public void testSimpleStringInvalid()
            throws Exception
    {
        render("hi {n*ame}", of("name", "mobi"), "hi {n*ame}");
    }

    @Test
    public void testSimpleStringInvalidSpace()
            throws Exception
    {
        render("hi {name }", of("name", "mobi"), "hi {name }");
    }

    @Test
    public void testSimpleStringValid()
            throws Exception
    {
        render("hi {n_ame}", of("n_ame", "mobi"), "hi 'mobi'");
    }

    @Test
    public void testSimpleOptionalEscape()
            throws Exception
    {
        render("hi \\[{name}]()", of("name", "mobi"), "hi ['mobi']()");
    }

    @Test
    public void testSimpleOptionalEscapeEscapeAlt()
            throws Exception
    {
        render("hi \\[\\{name}]()", of("name", "mobi"), "hi [{name}]()");
    }

    @Test
    public void testSimpleString()
            throws Exception
    {
        render("hi {name}", of("name", "mobi"), "hi 'mobi'");
    }

    @Test
    public void testSimpleDate()
            throws Exception
    {
        // render("hi {name}", of("name", LocalDate.parse("2018-1-1")), "hi date '2018-1-1'");
        render("hi {name}", of("name", LocalDate.parse("2018-01-01")), "hi CAST('2018-01-01' AS DATE)");
    }

    @Test
    public void testSimpleNumber()
            throws Exception
    {
        render("hi {name}", of("name", 4), "hi 4");
    }

    @Test
    public void testSimpleObjectString()
            throws Exception
    {
        render("hi {name.tobi}", of("name", of("tobi", "mobi")), "hi 'mobi'");
    }

    @Test
    public void testSimpleObjectNumber()
            throws Exception
    {
        render("hi {name.tobi}", of("name", of("tobi", 1)), "hi 1");
    }

    @Test
    public void testSimpleObjectDate()
            throws Exception
    {
        // render("hi {name.tobi}", of("name", of("tobi", LocalDate.parse("2018-1-1"))), "hi date '2018-1-1'");
        render("hi {name.tobi}", of("name", of("tobi", LocalDate.parse("2018-01-01"))), "hi CAST('2018-01-01' AS DATE)");
    }

    @Test
    public void testOptionalPartNotExists()
            throws Exception
    {
        render("hi [{name}](test)", ImmutableMap.<String, Object>of(), "hi test");
    }

    @Test
    public void testOptionalPartExistObject()
            throws Exception
    {
        render("hi [{name.tobi}](test)", of("name", of("tobi", 1)), "hi 1");
    }

    @Test
    public void testOptionalPartExistString()
            throws Exception
    {
        render("hi [{name}](test)", of("name", 1), "hi 1");
    }

    public static void render(String template, Map<String, ?> variables, String expected)
            throws RqlException
    {
        assertEquals(expected, buildTemplate(template, variables));
    }

    public static void throwException(String template, Map<String, ?> variables, String exceptionMessage)
    {
        try {
            buildTemplate(template, variables);
        }
        catch (/*IllegalArgumentException*/ RqlException e) {
            assertEquals(exceptionMessage, e.getMessage());
        }
    }

    public static void throwExceptionRegex(String template, Map<String, ?> variables, String prefix)
    {
        try {
            buildTemplate(template, variables);
        }
        catch (/*IllegalArgumentException*/ RqlException e) {
            assertTrue(e.getMessage().matches(prefix));
        }
    }

    private static String buildTemplate(String template, Map<String, ?> variables)
            throws RqlException
    {
        return Rql.parse(template).render(variables);
    }
}
