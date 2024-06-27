package namedEntities;

import java.util.List;

public class OrganizationEntity extends NamedEntity {
    private int numMembers;
    private boolean nonProfit;

    public OrganizationEntity(String label, String category, List<String> topics, List<String> keywords) {
        super(label, category, topics, keywords);
        this.numMembers = 0;
        this.nonProfit = true;
    }
    
    // Getters y Setters
    public int getNumMembers() {
        return this.numMembers;
    }
    public void setNumMembers(int numMembers) {
        this.numMembers = numMembers;
    }

    public boolean getNonProfit() {
        return this.nonProfit;
    }
    public void setNonProfit(boolean nonProfit) {
        this.nonProfit = nonProfit;
    }

}
