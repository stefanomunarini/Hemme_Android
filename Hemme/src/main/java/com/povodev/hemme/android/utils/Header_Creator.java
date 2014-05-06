package com.povodev.hemme.android.utils;


import org.springframework.http.HttpHeaders;

public class Header_Creator {

    public static HttpHeaders create(){

        String salt = Encoding_MD5.getMD5EncryptedString("povodevforhemmeABC");

        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "multipart/form-data");
//        headers.set("enctype", "multipart/form-data");
//        headers.set("method", "post");
        headers.set("salt",salt);

        return  headers;
    }
}
