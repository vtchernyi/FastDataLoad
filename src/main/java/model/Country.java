package model;

public class Country {
    private String code, name, continent, region, localName, governmentForm, headOfState, сode2;
    private Double surfaceArea, lifeExpectancy, gNP, gNPOld;
    private Integer indepYear, population, capitalId;

    public Country(String code, String name, String continent, String region, String localName, String governmentForm,
                   String headOfState, String сode2, Double surfaceArea, Double lifeExpectancy, Double gNP,
                   Double gNPOld, Integer indepYear, Integer population, Integer capitalId) {
        this.code = code;
        this.name = name;
        this.continent = continent;
        this.region = region;
        this.localName = localName;
        this.governmentForm = governmentForm;
        this.headOfState = headOfState;
        this.сode2 = сode2;
        this.surfaceArea = surfaceArea;
        this.lifeExpectancy = lifeExpectancy;
        this.gNP = gNP;
        this.gNPOld = gNPOld;
        this.indepYear = indepYear;
        this.population = population;
        this.capitalId = capitalId;
    }
}
