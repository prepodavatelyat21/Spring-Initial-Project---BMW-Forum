package uni.masters.web.spring.beans;

import javax.persistence.*;

@Entity
@Table(name = "offer")
public class OfferBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "city", length = 100)
    private String city;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id")
    private VehicleBean vehicle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserBean user;

    public OfferBean() { }

    public OfferBean(String city, VehicleBean vehicle, UserBean user) {
        this.city = city;
        this.vehicle = vehicle;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public VehicleBean getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleBean vehicle) {
        this.vehicle = vehicle;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
