package com.qa.api.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class GetApiCall {
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
    public void getUserApiTest() throws IOException {
         APIResponse response= requestContext.get("https://gorest.co.in/public/v2/users");
         int statusCode= response.status();
         assertEquals(statusCode,200);
        System.out.println("Print status code---------------------");
         System.out.println("Response  status code:"+statusCode);
        String statusResText= response.statusText();
        System.out.println("Print status Text---------------------");
        System.out.println(statusResText);

        ObjectMapper  objectMapper =new ObjectMapper();
        JsonNode jsonResNode= objectMapper.readTree(response.body());
        System.out.println("Print Response Body---------------------");
        System.out.println( jsonResNode.toPrettyString());
        System.out.println("Print URL---------------------");
        System.out.println("Print URL:"+response.url());
        System.out.println("Print Header ---------------------");
        Map<String,String> headerMap=new HashMap<>(response.headers());
        System.out.println("Header: "+headerMap);
       Assert.assertEquals(headerMap.get("server"),"cloudflare");
    }
    @Test
    public void specificUserApiTest() throws IOException {
        APIResponse response= requestContext.get("https://gorest.co.in/public/v2/users", RequestOptions.create()
                .setQueryParam("id","932594")
                .setQueryParam("status","active")

        );
        int statusCode= response.status();
        assertEquals(statusCode,200);
        System.out.println("Print status code---------------------");
        System.out.println("Response  status code:"+statusCode);
        String statusResText= response.statusText();
        System.out.println("Print status Text---------------------");
        System.out.println(statusResText);

        ObjectMapper  objectMapper =new ObjectMapper();
        JsonNode jsonResNode= objectMapper.readTree(response.body());
        System.out.println("Print Response Body---------------------");
        System.out.println( jsonResNode.toPrettyString());

    }
    @Test
    public void disposeResponseBodyTest(){
        System.out.println("------------Request 01----------------");
        APIResponse response= requestContext.get("https://gorest.co.in/public/v2/users");
        System.out.println("Response2 Text Before dispose: "+ response.text());
        response.dispose();  // Dispose method only dispose  response body it not dispose status, statusText, URl etc.
        int statusCode= response.status();
        assertEquals(statusCode,200);
        System.out.println("Print status code---------------------");
        System.out.println("Response  status code:"+statusCode);
        String statusResText= response.statusText();
        System.out.println("Print status Text---------------------");
        System.out.println(statusResText);
        try {
            System.out.println("Response Text after dispose: "+ response.text());

        }
        catch (PlaywrightException e){
            System.out.println("Response body is disposed:");
        }


        System.out.println("------------Request 02----------------");
        APIResponse ApiResponse= requestContext.get("     https://reqres.in/api/users?page=2");
        assertEquals(ApiResponse.status(),200);
        System.out.println("Print status code---------------------");
        System.out.println("Response  status code:"+statusCode);
        String statusResTxt= ApiResponse.statusText();
        System.out.println("Print status Text---------------------");
        System.out.println(statusResTxt);
        System.out.println("Response Text Before dispose: "+ ApiResponse.text());
        requestContext.dispose();
        try {
            System.out.println("Response1 Text after dispose: "+ response.text());

        }
        catch (PlaywrightException e){
            System.out.println("Response1 body is disposed:");
        }

        try {
            System.out.println("Response2 Text after dispose: "+ ApiResponse.text());

        }
        catch (PlaywrightException e){
            System.out.println("Response2 body is disposed:");
        }


    }

    @AfterTest
    public void tearDown(){
        playwright.close();
    }
}
