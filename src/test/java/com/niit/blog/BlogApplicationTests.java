package com.niit.blog;

import com.niit.blog.util.MD5Util;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlogApplicationTests {

    @Test
    void contextLoads() {
        String string = MD5Util.MD5Encode("1234", "utf-8");
        System.out.println("string = " + string);
    }

}
