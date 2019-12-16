package uni.masters.web.spring.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.masters.web.spring.beans.OfferBean;
import uni.masters.web.spring.beans.UserBean;
import uni.masters.web.spring.beans.VehicleBean;
import uni.masters.web.spring.repositories.ORepo;
import uni.masters.web.spring.repositories.URepo;
import uni.masters.web.spring.repositories.VRepo;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
public class VehicleController {
    private VRepo vRepo;
    private ORepo oRepo;

    public VehicleController(VRepo vRepo, ORepo oRepo){
        this.vRepo = vRepo;
        this.oRepo = oRepo;
    }
    @PostMapping(path = "/vehicle/add")
    public String addVehicle(@RequestParam(value = "model") String model, @RequestParam(value = "description") String description,
            @RequestParam(value = "price") String price,@RequestParam(value = "picture") String picture, HttpSession session) {
        UserBean user = (UserBean) session.getAttribute("user");
        if(user != null) {
            if (model.equals("") || description.equals("")) {
                return "error";
            }
            VehicleBean vehicle = new VehicleBean();
            vehicle.setModel(model);
            vehicle.setDescription(description);
            vehicle.setPicture(picture);
            try {
                vehicle.setPrice(Double.parseDouble(price));
            } catch (NumberFormatException e) {
                return "error";
            }
            vehicle.setUser(user);
            vehicle = vRepo.saveAndFlush(vehicle);
            if(vehicle != null) {
                return vehicle.getId() + "";
            }
            return "-1";
        }
        return "error";
    }
    @GetMapping(path="/vehicle/edit")
    public ResponseEntity<VehicleBean> editVehicle(@RequestParam(value = "id") int id, HttpSession session){
        UserBean user = (UserBean) session.getAttribute("user");
		if (user == null) {
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
        Optional<VehicleBean> vehicle = vRepo.findById(id);
        if (!vehicle.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vehicle.get(), HttpStatus.OK);
    }
    @PostMapping(path = "/vehicle/edit")
    public ResponseEntity<VehicleBean> updateVehicle(@RequestParam(value = "id") int id, @RequestParam(value = "model") String model, @RequestParam(value = "description") String description,
                             @RequestParam(value = "price") String price, HttpSession session) {
        UserBean user = (UserBean) session.getAttribute("user");
        if(user != null) {
            if (model.equals("") || description.equals("")) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            Optional<VehicleBean> vehicle = vRepo.findById(id);
            if (!vehicle.isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            vehicle.get().setModel(model);
            vehicle.get().setDescription(description);
            vehicle.get().setUser(user);
            try {
                vehicle.get().setPrice(Double.parseDouble(price));
            } catch (NumberFormatException e) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(vRepo.save(vehicle.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
    @GetMapping(path = "/vehicle/all")
    public List<VehicleBean> getAllVehicles(){
        return vRepo.findAll();
    }
    @GetMapping(path = "/vehicle/all/forCurrentUser")
    public List<VehicleBean> getAllVehiclesForCurrentUser(HttpSession session){
        UserBean user = (UserBean)session.getAttribute("user");
        if (user == null) {
            return null;
        }
        return vRepo.findByUser(user);
    }
    @DeleteMapping(path = "/vehicle/delete")
    public ResponseEntity<Boolean> deleteVehicle(@RequestParam(value = "id") int id, HttpSession session){
        UserBean user = (UserBean)session.getAttribute("user");
        if(user == null) {
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
        Optional<VehicleBean> optionalVehicle = vRepo.findById(id);
        if(optionalVehicle.isPresent()) {
            VehicleBean vehicle = optionalVehicle.get();
            Optional<OfferBean> offer = Optional.ofNullable(oRepo.findByVehicle(vehicle));
            if(vehicle.getUser().getId() == user.getId()) {
                if (offer.isPresent()) {
                    oRepo.delete(offer.get());
                }
                vRepo.delete(vehicle);
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
}