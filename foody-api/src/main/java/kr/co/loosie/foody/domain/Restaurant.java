package kr.co.loosie.foody.domain;

public class Restaurant {
    public String getName() {
        return"";
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }


    public void addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
    }

    public void setMenuItem(List<MenuItem> menuItems) {
        for(MenuItem menuItem : menuItems){
            addMenuItem(menuItem);
        }
    }
}
