package uni.masters.web.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.masters.web.spring.beans.UserBean;

@Repository
public interface URepo extends JpaRepository<UserBean, Integer> {
    UserBean findByUsername(String username);
    UserBean findByEmail(String email);
    UserBean findUserByUsernameAndPassword(String username, String password);
}