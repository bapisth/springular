package com.hemendra.springular.resource;

import com.hemendra.springular.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author Hemendra
 */
@RestController
//@RequestMapping("/user")
public class UserResource {
    @GetMapping("/user")
    public List<User> getAll() {

        return Arrays.asList(
                new User("Hemendra", "BBSR", "9090123456", "MCA"),
                new User("Sabya", "CTC", "9040456987", "B.Tech"),
                new User("Pradeep", "BLS", "9776123908", "B.SC")
                );
    }
}
