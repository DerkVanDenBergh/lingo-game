package com.lingogame.configuration.security;

import com.lingogame.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ISecurityService securityService;

    @PostMapping(path = "/registration")
    public String registration(@RequestBody MultiValueMap<String, String> formData) {

        String username = formData.getFirst("username");
        String password = formData.getFirst("password");

        if (userService.findByUsername(username) != null) {
            return "registration";
        }

        User user = new User(username, password);

        userService.save(user);


        securityService.autoLogin(user.getUsername(), user.getPassword());

        return "redirect:/home";
    }
}
