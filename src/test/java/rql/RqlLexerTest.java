package rql;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

public class RqlLexerTest
{

    @Test
    public void name()
            throws Exception
    {
        Template template = Template.parse("hi {name.tobi}");
        String rendered = template.render("name", ImmutableMap.of("tobi", "mobi"));
        System.out.println(rendered);
    }
}
