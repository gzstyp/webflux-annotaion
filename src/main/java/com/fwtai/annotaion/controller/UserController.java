package com.fwtai.annotaion.controller;

import com.fwtai.annotaion.bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    final Map<Long,User> users = new HashMap<>();

    @PostConstruct
    public void init() throws Exception {
        users.put(1L, new User(1, "Jack", "Smith", 18));
        users.put(2L, new User(2, "Peter", "Johnson", 36));
    }

    /**
     * 获取所有用户,http://127.0.0.1:8081/api/user/index
     * @return
    */
    @GetMapping("/index")
    public Flux<User> getAll() {
        return Flux.fromIterable(users.entrySet().stream()
            .map(Map.Entry::getValue)
            .collect(Collectors.toList()));
    }

    /**
     * 获取单个用户
     * @param id
     * @return
    */
    @GetMapping("/{id}")
    public Mono<User> getCustomer(@PathVariable Long id) {
        return Mono.justOrEmpty(users.get(id));
    }

    /**
     * 创建用户
     * @param user
     * @return
    */
    @PostMapping("/post")
    public Mono<ResponseEntity<String>> postUser(@RequestBody User user) {
        users.put(user.getId(), user);
        logger.info("########### POST:" + user);
        return Mono.just(new ResponseEntity<>("Post Successfully!", HttpStatus.CREATED));
    }

    /**
     * 修改用户
     * @param id
     * @param user
     * @return
    */
    @PutMapping("/put/{id}")
    public Mono<ResponseEntity<User>> putCustomer(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        users.put(id, user);
        System.out.println("########### PUT:" + user);
        return Mono.just(new ResponseEntity<>(user, HttpStatus.CREATED));
    }

    /**
     * 删除用户
     * @param id
     * @return
    */
    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<String>> deleteMethod(@PathVariable Long id) {
        users.remove(id);
        return Mono.just(new ResponseEntity<>("Delete Successfully!", HttpStatus.ACCEPTED));
    }
}