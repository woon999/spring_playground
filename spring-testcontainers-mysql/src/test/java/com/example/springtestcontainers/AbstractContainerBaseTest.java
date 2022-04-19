package com.example.springtestcontainers;

import org.testcontainers.containers.MySQLContainer;

public abstract class AbstractContainerBaseTest {
	static final String MYSQL_IMAGE = "mysql:8";
	static final MySQLContainer MY_SQL_CONTAINER;
	static {
		MY_SQL_CONTAINER = new MySQLContainer(MYSQL_IMAGE);
		MY_SQL_CONTAINER.start();
	}

}
