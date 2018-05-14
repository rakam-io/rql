package rql.filters;

class Newline_To_Br extends Filter {

    /*
     * newline_to_br(input)
     *
     * Add <br /> tags in front of all newlines in input string
     */
    @Override
    public Object apply(Object value, Object... params) {

        return super.asString(value).replaceAll("[\n]", "<br />\n");
    }
}
