package kr.co.loosie.foody.application;

import kr.co.loosie.foody.domain.Category;
import kr.co.loosie.foody.domain.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories;
    }

    public Category addCategory(String name) {
        Category category = Category.builder().name("Korean Food").build();

        categoryRepository.save(category);

        return category;
    }
}
