/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bmc.event;

import com.fasterxml.jackson.databind.JsonNode;

/**
 *
 * @author santopat
 */
public class CreateJiraIssues {

    public static void main(String args[]) {
        Util util = new Util();
        if (args.length == 4) {

            String createIssueString = "{\n"
                    + "    \"fields\": {\n"
                    + "       \"project\":\n"
                    + "       { \n"
                    + "          \"key\": \"BMW\"\n"
                    + "       },\n"
                    + "       \"summary\": \"REST ye merry gentlemen.\",\n"
                    + "       \"description\": \"Creating of an issue using project keys and issue type names using the REST API\",\n"
                    + "       \"issuetype\": {\n"
                    + "          \"name\": \"Task\"\n"
                    + "       }\n"
                    + "   }\n"
                    + "}";
            String url = args[0];//"http://clm-pun-019273:8181/rest/api/2/issue/";
            String userName = args[1];//"ravikanth.k69";
            String password = args[2];//"bmcAdm1n";
            int numberOfissuesNeedToCreate = Integer.parseInt(args[3]);
            String authCode = util.getAuthCode(userName, password);
            int totalJiraTicketsCount = 0;
            for (int i = 1; i <= numberOfissuesNeedToCreate; i++) {
                try {
                    JsonNode responseNode = null;
                    responseNode = util.createIssue(url, createIssueString, authCode);
                    String key = responseNode.get("key").asText();
                    totalJiraTicketsCount++;
                    System.out.println("Created Key {}" + key);
                } catch (Exception ex) {
                    System.out.println("Exception occured while creating issue {}" + ex.getMessage());
                }
            }
            System.out.println("totalJiraTicketsCount {}" + totalJiraTicketsCount);
        } else {
            System.out.println("Pass only 4 arguments");
        }
    }
}
