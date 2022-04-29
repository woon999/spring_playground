package com.example.springarchunit.example.bar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.springarchunit.example.foo.Foo;

@Component
public class Bar {

	@Autowired
	private Foo foo;
}
