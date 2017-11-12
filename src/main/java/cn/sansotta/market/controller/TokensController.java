package cn.sansotta.market.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.sansotta.market.domain.value.JwtToken;
import cn.sansotta.market.service.TokenService;
import cn.sansotta.market.service.UserService;

import static cn.sansotta.market.common.WebUtils.unauthorizedResponse;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RestController
@RequestMapping("/tokens")
public class TokensController {
    private final UserService userService;
    private final TokenService tokenService;

    public TokensController(UserService userService,
                            TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/{username}")
    public ResponseEntity<JwtToken>
    createToken(@PathVariable("username") String username,
                @RequestParam("password") String password) {
        if(!userService.checkPassword(username, password)) return unauthorizedResponse();
        return new ResponseEntity<>(tokenService.createToken(username), HttpStatus.CREATED);
    }
}
