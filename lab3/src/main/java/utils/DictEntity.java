package utils;

import java.util.ArrayList;
import java.util.List;
import namedEntities.*;

public class DictEntity {
    public List<NamedEntity> dictionary;

    public DictEntity(){
        dictionary = new ArrayList<>();
    }

    public void add(String label, String category, List<String> topics, List<String> keywords){
        NamedEntity myEntity;

        if (category == "PERSON") {
            myEntity = new PersonEntity(label, category, topics, keywords);
        }
        else if (category == "LOCATION"){
            myEntity = new LocationEntity(label, category, topics, keywords);
        }
        else if (category == "ORGANIZATION"){
            myEntity = new OrganizationEntity(label, category, topics, keywords);
        }
        else{
            myEntity = new OtherEntity(label, category, topics, keywords);
        }

        this.dictionary.add(myEntity);
    }
    
    public boolean DictContainsLabel (String label){
        for(NamedEntity entity : dictionary) {
            if(entity.getLabel().equals(label)){
                return true;
            }
        }
        return false;
    }


    //PRE: DictContainsLabel(label)
    
    //toma una label, y devuelve la categoria de esa entidad si es que existe
    public String getCategoryfromlabel (String label){
        for(NamedEntity entity : dictionary) {
            if(entity.getLabel().equals(label)){
                return entity.getCategory();
            }
        }
        return null;
    }

    //toma una label y devuelve el topico de la entidad si es que existe
    public List<String> getTopicFromLabel (String label){
        for(NamedEntity entity : dictionary) {
            if(entity.getLabel().equals(label)){
                return entity.getTopics();
            }
        }
        return null;
    }

    //toma una label y devuelve la lista de keywords de la entidad si es que existe
    public List<String> getKeywordsFromLabel (String label){
        for(NamedEntity entity : dictionary) {
            if(entity.getLabel().equals(label)){
                return entity.getKeywords();
            }
        }
        return null;
    }

    //toma una keyword y devuelve la label asociada
    public String getLabelFromKeyword (String keyword){
        for(NamedEntity entity : dictionary) {
            for(String key : entity.getKeywords()){
                if(key.equals(keyword)){
                    return entity.getLabel();
                }
            }
        }
        return null;
    }

    // Para debugear
    public void print() {
        System.out.println("Cantidad de entradas(labels): " + dictionary.size());
        for(NamedEntity entity : dictionary) {
            if(entity.getAppearanceCount() > 0) {
                System.out.println("Label: " + entity.getLabel() + ", Appearance Count: " + entity.getAppearanceCount());
            }
        }
    }

    public void increaseAppearanceCount(String label){
        for(NamedEntity entity : dictionary) {
            if(entity.getLabel().equals(label)){
                entity.increaseAppearanceCount();
                return;   
            }
        }
    }
}
