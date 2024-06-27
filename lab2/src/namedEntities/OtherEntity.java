package namedEntities;

import java.util.List;

public class OtherEntity extends NamedEntity {
    private String description;

    public OtherEntity(String label, String category, List<String> topics, List<String> keywords) {
        super(label, category, topics, keywords);
        this.description = "";
    }

    // Getters y Setters
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
