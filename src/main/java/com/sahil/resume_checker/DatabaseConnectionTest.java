package com.sahil.resume_checker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnectionTest implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseConnectionTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        jdbcTemplate.queryForList("""
            SELECT
                DATABASE() AS current_database,
                @@hostname AS mysql_server,
                @@port AS mysql_port,
                CURRENT_USER() AS mysql_user
            """).forEach(System.out::println);

        jdbcTemplate.queryForList("""
            SHOW TABLES
            """).forEach(System.out::println);
    }

}
