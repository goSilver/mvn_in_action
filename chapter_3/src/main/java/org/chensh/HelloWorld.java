package org.chensh;

public class HelloWorld {
    public static void main(String[] args) {
        System.out.println(new HelloWorld().sayHello());
    }

    public String sayHello(){
        return "hello maven";
    }
}