package com.example.komunikator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles(profiles = "test")
class KomunikatorApplicationTests {

    @Test
    void contextLoads() {
    }

}
