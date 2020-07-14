package model;

public class CountryLanguage {
    String language;
    boolean isOfficial;
    double percentage;

    public CountryLanguage(String language, boolean isOfficial, double percentage) {
        this.language = language;
        this.isOfficial = isOfficial;
        this.percentage = percentage;
    }
}
