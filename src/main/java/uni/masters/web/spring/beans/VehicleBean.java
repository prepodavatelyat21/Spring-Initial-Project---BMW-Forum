package uni.masters.web.spring.beans;

import javax.persistence.*;

@Entity
@Table(name = "vehicle")
public class VehicleBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "description", length = 1000, nullable = true)
    private String description;

    @Column(name = "price", precision = 2)
    private double price;

    @Column(name = "picture", nullable = true, length = 250)
    private String picture;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserBean user;

    public VehicleBean() { }

    public VehicleBean(String model, String description, double price, UserBean user, String picture) {
        this.model = model;
        this.description = description;
        this.price = price;
        this.picture = picture;
        this.user = user;
    }
    public VehicleBean(String model, String description, double price, UserBean user) {
        this.model = model;
        this.description = description;
        this.price = price;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
