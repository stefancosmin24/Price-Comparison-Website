package eu.cosminstefan;

import javax.persistence.*;

@Entity
@Table(name="shoe_instance")

public class ShoeInstance {


        @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="id")
        int id;


        @Column(name="size")
        int size;

        @ManyToOne
        @JoinColumn(name="shoe_id", nullable = false)
        Shoe shoe;



    //Getters and setters
    public Shoe getShoe() { return shoe; }
    public void setShoe(Shoe shoe) {this.shoe = shoe;}
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
}
