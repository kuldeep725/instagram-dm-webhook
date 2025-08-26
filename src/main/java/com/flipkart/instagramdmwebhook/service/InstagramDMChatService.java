package com.flipkart.instagramdmwebhook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class InstagramDMChatService {

    private static final Logger logger = LoggerFactory.getLogger(InstagramDMChatService.class);

    @Value("${instagram.page.access.token}")
    private String PAGE_ACCESS_TOKEN;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String INSTAGRAM_GRAPH_API_URL = "https://graph.instagram.com/v21.0/me/messages";

    /**
     * Sends a DM to an Instagram user.
     * @param recipientIgId The Instagram-scoped ID (IGSID) of the recipient.
     * @param message The text message to send.
     */
    public void sendInstagramDm(String recipientIgId, String message) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectNode messageBody = objectMapper.createObjectNode();
            ObjectNode recipient = objectMapper.createObjectNode();
            recipient.put("id", recipientIgId);
            messageBody.set("recipient", recipient);

            ObjectNode messageContent = objectMapper.createObjectNode();
            messageContent.put("text", message);
            messageBody.set("message", messageContent);

            // Add access token to query parameter
            String url = INSTAGRAM_GRAPH_API_URL + "?access_token=" + PAGE_ACCESS_TOKEN;

            HttpEntity<String> request = new HttpEntity<>(messageBody.toString(), headers);

            logger.info("Sending DM to Instagram Graph API. URL: {}, Payload: {}", url, messageBody.toString());

            ResponseEntity<Object> response = restTemplate.postForEntity(url, request, Object.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("DM sent successfully to {}. Response: {}", recipientIgId, response.getBody());
            } else {
                logger.error("Failed to send DM to {}. Status: {}, Body: {}", recipientIgId, response.getStatusCode(), response.getBody());
            }

        } catch (Exception e) {
            logger.error("Error sending Instagram DM to {}: {}", recipientIgId, e.getMessage(), e);
        }
    }
}