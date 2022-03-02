package fundamentals;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Penguin {
    private String name;
    private Integer height;
    private Family family;
    // ...

    @BsonCreator
    public Penguin(@BsonProperty("name") String name, @BsonProperty("height") Integer height, @BsonProperty("family") Family family) {
        this.name = name;
        this.height = height;
        this.family = family;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getHeight() {
        return height;
    }
    public void setHeight(Integer height) {
        this.height = height;
    }
    public Family getFamily() {
        return family;
    }
    public void setFamily(Family family) {
        this.family = family;
    }
    
    @Override
    public String toString() {
        return "Penguin [name=" + name + ", height=" + height + ", family=" + family + "]";
    }
}
