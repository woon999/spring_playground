package com.example.springrediscache.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springrediscache.domain.Product;
import com.example.springrediscache.repository.ProductDao;
import com.example.springrediscache.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product")
@EnableCaching
@RequiredArgsConstructor
public class ProductController {

	// private final ProductDao productDao;
	private final ProductRepository productRepository;

	@PostMapping
	public Product save(@RequestBody Product product){
		return productRepository.save(product);
	}

	@GetMapping
	public List<Product> getAll(){
		return productRepository.findAll();
	}

	@GetMapping("/{id}")
	public Product findOne(@PathVariable Long id){
		Optional<Product> opProduct = productRepository.findById(id);
		if(opProduct.isEmpty()){
			throw new RuntimeException("해당 상품은 존재하지 않습니다.");
		}

		return opProduct.get();
	}

	@DeleteMapping("/{id}")
	public String remove(@PathVariable Long id){
		productRepository.deleteById(id);
		return "delete " + id;
	}

}
