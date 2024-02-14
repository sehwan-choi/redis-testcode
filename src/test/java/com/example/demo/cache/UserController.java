package com.example.demo.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/init")
    public String init() {
        service.init();
        return "OK";
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return service.getUser(id);
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable("id") Long id, @RequestParam("name") String name) {
        return service.updateUser(id, name);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        service.deleteUser(id);
        return "OK";
    }
}
