package com.shopapotheke.githubpopularrepositories.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Repository {
    private  String name;
    private  String url;
    private Integer stars;
    private  String programmingLanguage;

    private String aggregatedString;

    public Repository() {
    }

    public Repository(String name, String url, int stars, String programmingLanguage) {
        this.name = name;
        this.url = url;
        this.stars = stars;
        this.programmingLanguage = programmingLanguage;
    }
//    @JsonCreator
//    public Repository(@JsonProperty("name") String name,
//                      @JsonProperty("url") String url,
//                      @JsonProperty("stargazers_count") int stars,
//                      @JsonProperty("language") String programmingLanguage) {
//        this.name = name;
//        this.url = url;
//        this.stars = stars;
//        this.programmingLanguage = programmingLanguage;
//    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Integer getStars() {
        return stars;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public String getAggregatedString() {
        return aggregatedString;
    }

    public void setAggregatedString(String aggregatedString) {
        this.aggregatedString = aggregatedString;
        reset();
    }

    public void reset() {
        this.name = null;
        this.url = null;
        this.stars = null;
        this.programmingLanguage = null;
    }
}