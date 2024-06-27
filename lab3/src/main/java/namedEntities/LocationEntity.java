package namedEntities;

import java.util.List;

public class LocationEntity extends NamedEntity {
    
    private String coordinates;
    private int heightFromSeaLevel;
    private String country;

    public LocationEntity(String label, String category, List<String> topics, List<String> keywords) {
        super(label, category, topics, keywords);
        this.coordinates = "";
        this.heightFromSeaLevel = 0;
        this.country = "";
    }

    public String getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public int getHeightFromSeaLevel() {
        return this.heightFromSeaLevel;
    }

    public void setHeightFromSeaLevel(int heightFromSeaLevel) {
        this.heightFromSeaLevel = heightFromSeaLevel;
    }
    
    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}