package namedEntities;

import java.util.List;

public class PersonEntity extends NamedEntity {
    private int age;
    private boolean isAlive;
    private String nationality;

    public PersonEntity(String label, String category, List<String> topics, List<String> keywords) {
        super(label, category, topics, keywords);
        this.age = 0;
        this.isAlive = true;
        this.nationality = "";
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean getIsAlive() {
        return this.isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public String getNationality() {
        return this.nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

}
