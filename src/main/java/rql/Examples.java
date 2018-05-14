package rql;

import rql.filters.Filter;

import java.util.HashMap;
import java.util.Map;

/**
 * A class holding some examples of how to use Liqp.
 */
public class Examples
{

    private static void demoGuards()
    {

        ProtectionSettings protectionSettings = new ProtectionSettings.Builder()
                .withMaxSizeRenderedString(300)
                .withMaxIterations(15)
                .withMaxRenderTimeMillis(100L)
                .withMaxTemplateSizeBytes(100)
                .build();

        String rendered = Template.parse("{% for i in (1..10) %}{ text }{% endfor %}")
                .withProtectionSettings(protectionSettings)
                .render("{\"text\": \"abcdefghijklmnopqrstuvwxyz\"}");

        System.out.println(rendered);
    }

    private static void demoSimple()
    {

        Template template = Template.parse("hi {name}");
        String rendered = template.render("name", "tobi");
        System.out.println(rendered);

        template = Template.parse("hi {name}");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "tobi");
        rendered = template.render(map);
        System.out.println(rendered);

        template = Template.parse("hi {name}");
        rendered = template.render("{\"name\" : \"tobi\"}");
        System.out.println(rendered);
    }

    private static void demoCustomStrongFilter()
    {

        // first register your custom filter
        Filter.registerFilter(new Filter("b")
        {
            @Override
            public Object apply(Object value, Object... params)
            {
                // create a string from the  value
                String text = super.asString(value);

                // replace and return *...* with <strong>...</strong>
                return text.replaceAll("\\*(\\w(.*?\\w)?)\\*", "<strong>$1</strong>");
            }
        });

        // use your filter
        Template template = Template.parse("{ wiki | b }");
        String rendered = template.render("{\"wiki\" : \"Some *bold* text *in here*.\"}");
        System.out.println(rendered);
    }

    private static void demoCustomRepeatFilter()
    {

        // first register your custom filter
        Filter.registerFilter(new Filter("repeat")
        {
            @Override
            public Object apply(Object value, Object... params)
            {

                // check if an optional parameter is provided
                int times = params.length == 0 ? 1 : super.asNumber(params[0]).intValue();

                // get the text of the value
                String text = super.asString(value);

                StringBuilder builder = new StringBuilder();

                while (times-- > 0) {
                    builder.append(text);
                }

                return builder.toString();
            }
        });

        // use your filter
        Template template = Template.parse("{ 'a' | repeat }\n{ 'b' | repeat:5 }");
        String rendered = template.render();
        System.out.println(rendered);
    }

    private static void demoCustomSumFilter()
    {

        Filter.registerFilter(new Filter("sum")
        {
            @Override
            public Object apply(Object value, Object... params)
            {

                Object[] numbers = super.asArray(value);

                double sum = 0;

                for (Object obj : numbers) {
                    sum += super.asNumber(obj).doubleValue();
                }

                return sum;
            }
        });

        Template template = Template.parse("{ numbers | sum }");
        String rendered = template.render("{\"numbers\" : [1, 2, 3, 4, 5]}");
        System.out.println(rendered);
    }

    public static void instanceFilter()
    {

        Template template = Template.parse("{ numbers | sum }").with(new Filter("sum")
        {
            @Override
            public Object apply(Object value, Object... params)
            {

                Object[] numbers = super.asArray(value);

                double sum = 0;

                for (Object obj : numbers) {
                    sum += super.asNumber(obj).doubleValue();
                }

                return sum;
            }
        });

        String rendered = template.render("{\"numbers\" : [1, 2, 3, 4, 5]}");
        System.out.println(rendered);
    }

    public static void demoStrictVariables()
    {
        try {
            Template.parse("{mu}")
                    .withRenderSettings(new RenderSettings.Builder().withStrictVariables(true).build())
                    .render();
        }
        catch (RuntimeException ex) {
            System.out.println("Caught an exception for strict variables");
        }
    }
}
