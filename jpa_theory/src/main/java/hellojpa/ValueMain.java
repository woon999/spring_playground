package hellojpa;

import hellojpa.embedded.Address;

public class ValueMain {
    public static void main(String[] args) {
//        int a = 10;
//        int b = a;
//
//        b = 20;
//
//        System.out.println("a = " + a);
//        System.out.println("b = " + b);

//        Integer a = new Integer(10);
//        Integer b = a;
//
//        System.out.println("a = " + a);
//        System.out.println("b = " + b);

        int a = 10;
        int b = 10;
        System.out.println(a==b);

        Address address1 = new Address("city", "street", "zipcode");
        Address address2 = new Address("city", "street", "zipcode");
        System.out.println(address1 == address2);
        System.out.println(address1.equals(address2));


    }
}
