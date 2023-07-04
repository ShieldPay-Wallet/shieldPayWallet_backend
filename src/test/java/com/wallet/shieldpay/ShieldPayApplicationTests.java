package com.wallet.shieldpay;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ShieldPayApplicationTests {

    @Test
    void contextLoads() {
        int value = (2-1);
        assertEquals( 1, value);
    }

}
