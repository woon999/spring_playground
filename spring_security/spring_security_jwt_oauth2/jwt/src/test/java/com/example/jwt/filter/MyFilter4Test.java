package com.example.jwt.filter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;

class MyFilter4Test {

	@Test
	void test(){
		try{
			get("http://127.0.0.1:8080/transfer/12345678");
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public void get(String uri) throws Exception{
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(uri))
			.GET()
			.build();

		HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());
	}

	interface a{}
	interface b{}
	interface c extends a,b{}
}
