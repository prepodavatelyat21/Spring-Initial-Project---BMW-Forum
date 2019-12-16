package uni.masters.web.spring.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;
import uni.masters.web.spring.beans.UserBean;
import uni.masters.web.spring.repositories.URepo;

import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
public class Login {
    private URepo uRepo;

    public Login(URepo uRepo) {
        this.uRepo = uRepo;
    }

    private String hashPassword(String text) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            byte[] bytes = md.digest();
            for (int i = 0; i < bytes.length; i++) {
                sb.append((char) bytes[i]);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    @PostMapping(path = "/login")
    public String login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password, HttpSession session) {
        UserBean user = uRepo.findUserByUsernameAndPassword(username, hashPassword(password));
        if (user != null) {
            session.setAttribute("user", user);
            return "dashboard.html";
        }
        return "";
    }

    @PostMapping(path = "/register")
    public UserBean register(@RequestParam(value = "email") String email,
                             @RequestParam(value = "username") String username, @RequestParam(value = "password") String password,
                             @RequestParam(value = "confirmPassword") String repeatPassword) {
        Optional<UserBean> userWithSameName = Optional.ofNullable(uRepo.findByUsername(username));
        Optional<UserBean> userWithSameEmail = Optional.ofNullable(uRepo.findByEmail(email));
        if(userWithSameName.isPresent() || userWithSameEmail.isPresent()) {
            return null;
        }
        if (password.equals(repeatPassword)) {
            UserBean user = new UserBean(username, email, hashPassword(password));
            return uRepo.saveAndFlush(user);
        }
        return null;
    }
    @GetMapping(path = "/validate/username")
    public ResponseEntity<Boolean> validateUsername(@RequestParam(value = "username") String username){
        Optional<UserBean> user = Optional.ofNullable(uRepo.findByUsername(username));
        if(user.isPresent()) {
            return new ResponseEntity<>(true, HttpStatus.IM_USED);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }
    @GetMapping(path = "/validate/email")
    public ResponseEntity<Boolean> validateEmail(@RequestParam(value = "email") String email){
        Optional<UserBean> user = Optional.ofNullable(uRepo.findByEmail(email));
        if(user.isPresent()) {
            return new ResponseEntity<>(true, HttpStatus.IM_USED);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }
    @GetMapping(path = "/currentUser")
    public ResponseEntity<Integer> getCurrentUser(HttpSession session){
        UserBean user = (UserBean) session.getAttribute("user");
        if(user != null) {
            return new ResponseEntity<>(user.getId(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping(path="/logout")
    public ResponseEntity<Boolean> logout(HttpSession session){
        UserBean user = (UserBean)session.getAttribute("user");
        if(user != null) {
            session.invalidate();
            return  new ResponseEntity<>(true, HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
