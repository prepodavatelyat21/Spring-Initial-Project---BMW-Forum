package uni.masters.web.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.masters.web.spring.beans.OfferBean;
import uni.masters.web.spring.beans.VehicleBean;

@Repository
public interface ORepo extends JpaRepository<OfferBean, Integer> {
    OfferBean findByVehicle(VehicleBean vehicle);
}
