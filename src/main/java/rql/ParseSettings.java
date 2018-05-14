package rql;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ParseSettings {
    public final boolean stripSpacesAroundTags;
    public final boolean stripSingleLine;
    public final ObjectMapper mapper;

    public static class Builder {

        boolean stripSpacesAroundTags;
        boolean stripSingleLine;
        ObjectMapper mapper;

        public Builder() {
            this.stripSpacesAroundTags = false;
            this.mapper = new ObjectMapper();
        }

        public Builder withStripSpaceAroundTags(boolean stripSpacesAroundTags) {
            return this.withStripSpaceAroundTags(stripSpacesAroundTags, false);
        }

        public Builder withStripSpaceAroundTags(boolean stripSpacesAroundTags, boolean stripSingleLine) {

            if (stripSingleLine && !stripSpacesAroundTags) {
                throw new IllegalStateException("stripSpacesAroundTags must be true if stripSingleLine is true");
            }

            this.stripSpacesAroundTags = stripSpacesAroundTags;
            this.stripSingleLine = stripSingleLine;
            return this;
        }

        public Builder withMapper(ObjectMapper mapper) {
            this.mapper = mapper;
            return this;
        }

        public ParseSettings build() {
            return new ParseSettings(this.stripSpacesAroundTags, this.stripSingleLine, this.mapper);
        }
    }

    private ParseSettings(boolean stripSpacesAroundTags, boolean stripSingleLine, ObjectMapper mapper) {
        this.stripSpacesAroundTags = stripSpacesAroundTags;
        this.stripSingleLine = stripSingleLine;
        this.mapper = mapper;
    }
}
