package eu.cosminstefan;

import javax.persistence.*;

@Entity
@Table(name="shoe")
public class Shoe {

    @Id
    @Column(name="style_code")
    private String styleCode;

    @Column(name="brand")
    private String brand;

    @Column(name="model")
    private String model;

    public String toString(){
        return "Style Code: " + styleCode + "; model: " + brand + " " + model;
    }
    public Shoe (){
    }

    public String getStyleCode(){
        return styleCode;
    }

    //Getters and setters
    public void setStyleCode(String styleCode) {
        this.styleCode = styleCode;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
}
