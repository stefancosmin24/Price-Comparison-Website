package eu.cosminstefan;

import javax.persistence.*;

@Entity
@Table(name="comparison")

public class Comparison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int comparisonId;

    @Column(name="name")
    private String name;

    @Column(name="price")
    private double price;

    @Column(name="url")
    private String url;

    @ManyToOne
    @JoinColumn(name="comparison_id", nullable=false)
    ShoeInstance shoeInstance;

    public String toString(){
        return "Comparison ID: " + comparisonId + "; Name: " + name + "; url= " + url + "; price= £" + price;
    }
    public Comparison (){
    }

    //Getters and setters
    public ShoeInstance getShoeInstance() { return shoeInstance; }
    public void setShoeInstance(ShoeInstance shoeInstance) { this.shoeInstance = shoeInstance; }
    public void setComparisonId(int comparisonId) {
        this.comparisonId = comparisonId;
    }
    public int getComparisonId() {
        return comparisonId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice(){return price;}
    public String getUrl() {
        return url;
    }
    public String getName() {
        return name;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
