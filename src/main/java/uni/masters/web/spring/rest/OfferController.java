package uni.masters.web.spring.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uni.masters.web.spring.beans.OfferBean;
import uni.masters.web.spring.beans.UserBean;
import uni.masters.web.spring.beans.VehicleBean;
import uni.masters.web.spring.repositories.ORepo;
import uni.masters.web.spring.repositories.VRepo;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
public class OfferController {
    private ORepo oRepo;
    private VRepo vRepo;

    public OfferController(ORepo oRepo, VRepo vRepo) {
        this.oRepo = oRepo;
        this.vRepo = vRepo;
    }
    @PostMapping(path = "/offer/add")
    public OfferBean addOffer(@RequestParam(value = "city") String city, @RequestParam(value = "vehicleId") String vehicleId, HttpSession session) {
        UserBean user = (UserBean) session.getAttribute("user");
        if(user == null || city.equals("") || vehicleId.equals("")) {
            return null;
        }
        Optional<VehicleBean> vehicle = vRepo.findById(Integer.parseInt(vehicleId));
        if (!vehicle.isPresent()) {
            return null;
        }
        Optional<OfferBean> existingOffer = Optional.ofNullable(oRepo.findByVehicle(vehicle.get()));
        if (existingOffer.isPresent()) {
            return null;
        }
        OfferBean offer = new OfferBean();
        offer.setCity(city);
        offer.setVehicle(vehicle.get());
        offer.setUser(user);
        return oRepo.saveAndFlush(offer);
    }
}
