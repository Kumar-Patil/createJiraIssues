/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bmc.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author santopat
 */
public class Util {

    private static final Logger log = LoggerFactory.getLogger(Util.class);
    public static final String JIRA_BASIC = "Basic ";

    public String getAuthCode(final String userName, String password) {
        byte[] encoded = Base64.encodeBase64((userName + ":" + password).getBytes());
        return new String(encoded);
    }

    public JsonNode createIssue(final String finalUrl, String data , String authCode) {
        JsonNode responseNode = null;
        HttpClient httpClient = null;
        boolean isSuccessful = false;
        int retryCount = 0;
        while (!isSuccessful && retryCount <= 3) {
            try {
                httpClient = HttpClientBuilder.create().build();
                HttpPost httpPost = new HttpPost(finalUrl);
                httpPost.addHeader("Authorization", JIRA_BASIC + authCode);
                httpPost.addHeader("Content-Type", "application/json");
                httpPost.addHeader("accept", "application/json");
                httpPost.addHeader("User-Agent", "JiraIntegration");
                Charset charsetD = Charset.forName("UTF-8");
                StringEntity postingString = new StringEntity(data, charsetD);
                httpPost.setEntity(postingString);
                HttpResponse response = httpClient.execute(httpPost);
                String responce = EntityUtils.toString(response.getEntity());
                ObjectMapper objectMapper = new ObjectMapper();
                responseNode = objectMapper.readTree(responce);
                return responseNode;
            } catch (IOException | ParseException ex) {
                if (retryCount < 3) {
                    retryCount++;
                    try {
                        log.error("[Retry  {} ], Waiting for {} sec before trying again ......" + retryCount, 5000);
                        Thread.sleep(5000);
                    } catch (InterruptedException e1) {
                        log.error("Thread interrupted ......");
                    }
                    continue;
                } else {
                    System.out.println("Exception occured while creating tickets");
                }

            }
        }
        return responseNode;
    }

}
