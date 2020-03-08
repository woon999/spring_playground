package kr.co.loosie.foody.interfaces;

import kr.co.loosie.foody.application.CategoryService;
import kr.co.loosie.foody.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> list(){
        List<Category> categories = categoryService.getCategories();
        return categories;
    }

    @PostMapping("/categories")
    public ResponseEntity<?> create(
            @RequestBody Category resource
    ) throws URISyntaxException {
        String name = resource.getName();

        Category category = categoryService.addCategory(name);

        String url = "/regions/" + category.getId();
        return ResponseEntity.created(new URI(url)).body("{}");
    }

}
