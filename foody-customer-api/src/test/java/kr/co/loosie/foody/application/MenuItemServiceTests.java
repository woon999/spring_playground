package kr.co.loosie.foody.application;

import kr.co.loosie.foody.application.MenuItemService;
import kr.co.loosie.foody.domain.MenuItem;
import kr.co.loosie.foody.domain.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MenuItemServiceTests {

    private MenuItemService menuItemService;

    @Mock
    private MenuItemRepository menuItemRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        menuItemService = new MenuItemService(menuItemRepository);
    }


    @Test
    public void bulkUpdate() {
        List<MenuItem> menuItems = new ArrayList<>();

//      추가
        menuItems.add(MenuItem.builder().name("Kimchi").build());
//      수정
        menuItems.add(MenuItem.builder().id(12L).name("Gukbob").build());
//      삭제
        menuItems.add(MenuItem.builder().id(1004L).destroy(true).build());

        menuItemService.bulkUpdate(1L, menuItems);

        verify(menuItemRepository, times(2)).save(any());
        verify(menuItemRepository, times(1)).deleteById(eq(1004L));

    }

}