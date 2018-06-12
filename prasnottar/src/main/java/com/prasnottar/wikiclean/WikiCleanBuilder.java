package com.prasnottar.wikiclean;

public class WikiCleanBuilder {
    private boolean withTitle = false;
    private boolean withFooter = false;
    private WikiClean.WikiLanguage lang = WikiClean.WikiLanguage.EN;
    public WikiCleanBuilder() {}

    public WikiCleanBuilder withTitle(boolean flag) {
        this.withTitle = flag;
        return this;
    }

    public WikiCleanBuilder withFooter(boolean flag) {
        this.withFooter = flag;
        return this;
    }

    public WikiCleanBuilder withLanguage(WikiClean.WikiLanguage lang) {
        this.lang = lang;
        return this;
    }

    public WikiClean build() {
        WikiClean clean = new WikiClean();
        clean.setWithTitle(withTitle);
        clean.setWithFooter(withFooter);
        clean.setLanguage(lang);

        return clean;
    }
}
