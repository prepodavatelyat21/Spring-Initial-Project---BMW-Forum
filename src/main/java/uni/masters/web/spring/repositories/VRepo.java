package uni.masters.web.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.masters.web.spring.beans.UserBean;
import uni.masters.web.spring.beans.VehicleBean;

import java.util.List;

@Repository
public interface VRepo extends JpaRepository<VehicleBean, Integer> {
    List<VehicleBean> findByUser(UserBean user);
}
