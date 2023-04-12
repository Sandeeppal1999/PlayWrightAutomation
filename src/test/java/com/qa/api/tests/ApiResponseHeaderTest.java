package com.qa.api.tests;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class ApiResponseHeaderTest {

    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;
    @BeforeClass
    public void setup(){
        playwright= Playwright.create();
        request= playwright.request();
        requestContext= request.newContext();
    }

    @Test
    public void getHeaderTest(){

        APIResponse response= requestContext.get("https://gorest.co.in/public/v2/users");
        int statusCode= response.status();
        assertEquals(statusCode,200);
        System.out.println("---------Print status code---------------------");
        System.out.println("Response  status code:"+statusCode);
        //using map
        System.out.println("Header using map--------------------");
        Map<String,String> headerMap=new HashMap<>(response.headers());
        System.out.println("Header: "+headerMap);
        headerMap.forEach((k,v)-> System.out.println(k+":"+v));
        System.out.println("Header size : "+  headerMap.size());

        System.out.println("Header using Array--------------------");
        // using Arrays
        List<HttpHeader> headersList= response.headersArray();
        for(HttpHeader e:headersList){
            System.out.println(e.name+":"+e.value);
        }

    }





    @AfterTest
    public void tearDown(){
        playwright.close();
    }
}
