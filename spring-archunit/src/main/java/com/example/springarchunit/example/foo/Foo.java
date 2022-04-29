package com.example.springarchunit.example.foo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.springarchunit.example.bar.Bar;

@Component
public class Foo {

	/**
	 * rule check error!
	 */
	// @Autowired
	// private Bar bar;
}
