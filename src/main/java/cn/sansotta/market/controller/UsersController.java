package cn.sansotta.market.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.sansotta.market.domain.value.User;
import cn.sansotta.market.service.Authorized;
import cn.sansotta.market.service.UserService;

import static cn.sansotta.market.common.WebUtils.badRequestResponse;
import static cn.sansotta.market.common.WebUtils.createdResponse;
import static cn.sansotta.market.common.WebUtils.noContentResponse;
import static cn.sansotta.market.common.WebUtils.okResponse;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RestController
@ExposesResourceFor(User.class)
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {this.userService = userService;}

    @Authorized
    @PostMapping
    public ResponseEntity
    createUser(User user) {
        if(userService.newUser(user)) return createdResponse();
        else return badRequestResponse();
    }

    @Authorized
    @DeleteMapping("/{username}")
    public ResponseEntity
    deleteUser(@PathVariable("username") String username) {
        if(userService.removeUser(username)) return noContentResponse();
        else return badRequestResponse();
    }

    @Authorized
    @PutMapping("/{username}/password")
    public ResponseEntity
    modifyPassword(@PathVariable("username") String username,
                   @RequestParam("password") String password) {
        if(userService.modifyPassword(username, password)) return okResponse();
        else return badRequestResponse();
    }
}
