package com.qa.api.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CreateUserPostApi {

    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;
    String emailID;
    @BeforeClass
    public void setup(){
        playwright= Playwright.create();
        request= playwright.request();
        requestContext= request.newContext();
    }
    public String getRandomEmailId(){
         emailID="Sandeeptest"+System.currentTimeMillis()+"@gmail.in";
        return emailID;
    }

    @Test
    public void createUserTest() throws IOException {
        Map<String,Object> data=new HashMap<String, Object>();
        data.put("name","Sandeep");
        data.put("email",getRandomEmailId());
        data.put("status","active");
        data.put("gender","male");
        data.put("age","28");

       APIResponse postResponse= requestContext.post("\n" +
                "https://gorest.co.in/public/v2/users", RequestOptions.create()
                .setHeader("Content-Type","application/json")
                .setHeader("Authorization","Bearer abd4292b422d9e9b8b599193d7b57f73d2676486dac4fd2501de11e9799314b7")
                .setData(data));

        Assert.assertEquals(postResponse.status(),201);
        Assert.assertEquals(postResponse.statusText(),"Created");
        System.out.println(postResponse.text());

        ObjectMapper objectMapper=new ObjectMapper();
       JsonNode jsonNodeData= objectMapper.readTree(postResponse.body());
        System.out.println(jsonNodeData.toPrettyString());


        //Fetch UserID
       String userId= jsonNodeData.get("id").asText();
       //Get call: Fetch same userID
        System.out.println("==========Verify Get response:----------------------");

    APIResponse getResponse=  requestContext.get("https://gorest.co.in/public/v2/users/"+userId,
                RequestOptions.create()
                        .setHeader("Authorization","Bearer abd4292b422d9e9b8b599193d7b57f73d2676486dac4fd2501de11e9799314b7"));
    Assert.assertEquals(getResponse.status(),200);
    Assert.assertEquals(getResponse.statusText(),"OK");
    Assert.assertTrue(getResponse.text().contains(userId));
    Assert.assertTrue(getResponse.text().contains("Sandeep"));
        Assert.assertTrue(getResponse.text().contains(emailID));

    }



    @AfterTest
    public void tearDown(){
        playwright.close();
    }
}
