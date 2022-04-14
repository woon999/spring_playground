package com.example.springrediscache.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springrediscache.model.Product;
import com.example.springrediscache.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductRepository productRepository;

	@PostMapping
	public Product save(@RequestBody Product product) {
		return productRepository.save(product);
	}

	@GetMapping
	public List<Product> getAll() {
		return productRepository.findAll();
	}

	@GetMapping("/{id}")
	@Cacheable(key = "#id", value = "product")
	public Product findOne(@PathVariable Long id) {
		log.info("Employee fetching from DB :" + id);
		Optional<Product> opProduct = productRepository.findById(id);
		if(opProduct.isEmpty()){
			throw new RuntimeException("해당 상품은 존재하지 않습니다.");
		}

		return opProduct.get();
	}

	@PutMapping("/{id}")
	@CachePut(key = "#id", value = "product")
	public Product update(@PathVariable Long id, @RequestBody Product product) {
		Product findProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 상품은 존재하지 않습니다."));

		findProduct.update(product);
		productRepository.save(findProduct);

		return findProduct;
	}

	@DeleteMapping("/{id}")
	@CacheEvict(key = "#id", value = "product")
	public String remove(@PathVariable(value = "id") Long id) {
		log.info("delete product from db and cache :" + id);
		productRepository.deleteById(id);
		return "delete " + id;
	}

}
