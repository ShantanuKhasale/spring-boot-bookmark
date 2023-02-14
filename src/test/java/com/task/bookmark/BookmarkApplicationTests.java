package com.task.bookmark;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class BookmarkApplicationTests extends AbstractContainerBaseTest {
    @Test
    void contextLoads() {
        System.out.println(postgreSQLContainer.getDatabaseName());
        System.out.println(postgreSQLContainer.getJdbcUrl());
        System.out.println(postgreSQLContainer.getUsername());
        System.out.println(postgreSQLContainer.getPassword());
        System.out.println(postgreSQLContainer.getBinds());
        System.out.println(postgreSQLContainer.getExposedPorts());
        System.out.println(postgreSQLContainer.getPortBindings());
//        System.out.println(postgreSQLContainer.getMappedPort(5432));
    }

}
