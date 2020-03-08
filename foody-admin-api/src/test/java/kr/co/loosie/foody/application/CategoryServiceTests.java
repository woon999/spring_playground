package kr.co.loosie.foody.application;

import kr.co.loosie.foody.domain.Category;
import kr.co.loosie.foody.domain.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class CategoryServiceTests {

    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        categoryService = new CategoryService(categoryRepository);
    }


    @Test
    public void getCategories(){
        List<Category> mockCategories = new ArrayList<>();
        mockCategories.add(Category.builder().name("Korean Food").build());

        given(categoryRepository.findAll()).willReturn(mockCategories);

        List<Category> categories = categoryService.getCategories();
        Category category = categories.get(0);
        assertThat(category.getName(),is("Korean Food"));
    }


    @Test
    public void addCategory(){
        Category category =categoryService.addCategory("Korean Food");

        verify(categoryRepository).save(any());


        assertThat(category.getName(),is("Korean Food"));
    }


}