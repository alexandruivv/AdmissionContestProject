package domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Section implements hasId<Integer> {
    private static int currentNr;

    private Integer id;
    private String name;
    private int freePlaces;

    public Section(){}
    //constructor Section
    public Section(Integer id, String name, int freePlaces){
        this.id = id;
        this.name = name;
        this.freePlaces = freePlaces;
    }

    public Section(String name, int freePlaces){
        this.id = ++currentNr;
        this.name = name;
        this.freePlaces = freePlaces;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public int getFreePlaces() {
        return freePlaces;
    }

    public void setFreePlaces(int freePlaces) {
        this.freePlaces = freePlaces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        return (name.equals(section.name)) || (section.getId() == id);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return  name + ' ' + Integer.toString(freePlaces);
    }

    @XmlElement
    @Override
    public Integer getId(){
        return id;
    }

    public static void setCurrentNr(int nr){
        currentNr = nr;
    }
}
