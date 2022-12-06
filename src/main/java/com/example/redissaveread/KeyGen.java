package com.example.redissaveread;

public class KeyGen {
    private static final String CART_KEY = "cart";

    public static String cartKeyGenerate(Long memberId){
        return CART_KEY + ":" + memberId;
    }
}
