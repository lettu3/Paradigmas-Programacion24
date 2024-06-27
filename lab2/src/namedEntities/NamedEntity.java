package namedEntities;

import java.util.ArrayList;
import java.util.List;

public class NamedEntity {

    private String label;
    private String category;
    private List<String> topics;
    private List<String> keywords;
    private int appearanceCount;

    public NamedEntity(String label, String category, List<String> topics, List<String> keywords) {
        this.label = label;
        this.category = category;
        this.topics = topics;
        this.keywords = keywords;
        this.appearanceCount = 0;
    }

    // Setters
    public void setLabel(String Label) {
        this.label = Label;
    }

    public void setCategory(String Category) {
        this.category = Category;
    }

    public void setTopics(List<String> Topics) {
        this.topics = Topics;
    }

    public void setKeywords(List<String> Keywords) {
        this.keywords = Keywords;
    }

    // Getters
    public String getLabel() {
        return this.label;
    }

    public String getCategory() {
        return this.category;
    }

    public List<String> getTopics() {
        return this.topics;
    }

    public List<String> getKeywords() {
        return this.keywords;
    }

    public int getAppearanceCount() {
        return this.appearanceCount;
    }

    public void increaseAppearanceCount() {
        this.appearanceCount += 1;
    }

    public void print() {
        System.out.println("Label: " + label);
        System.out.println("Category: " + category);
        System.out.println("Topics: ");
        for (String t : topics) {
            System.out.println(t);
        }
        System.out.println("Keywords");
        for (String k : keywords) {
            System.out.println(k);
        }
    }

    List<NamedEntity> classifiedEntities = new ArrayList<>();

}