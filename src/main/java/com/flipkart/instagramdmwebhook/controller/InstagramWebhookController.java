package com.flipkart.instagramdmwebhook.controller;

import com.flipkart.instagramdmwebhook.model.InstagramWebhookPayload;
import com.flipkart.instagramdmwebhook.service.InstagramDMChatService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

// ... (imports)

@RestController
@RequestMapping("/webhook")
public class InstagramWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(InstagramWebhookController.class);

    @Value("${instagram.verify.token}")
    private String VERIFY_TOKEN;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InstagramDMChatService instagramDMChatService; // Inject the service

    public InstagramWebhookController(InstagramDMChatService instagramDMChatService) {
        this.instagramDMChatService = instagramDMChatService;
    }
    /**
     * Webhook Verification Endpoint (GET request)
     * Instagram sends a GET request to verify your callback URL.
     */
    @GetMapping
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String verifyToken) {

        logger.info("Webhook verification received. Mode: {}, Challenge: {}, Verify Token: {}", mode, challenge, verifyToken);

        if ("subscribe".equals(mode) && VERIFY_TOKEN.equals(verifyToken)) {
            logger.info("Webhook verified successfully!");
            return new ResponseEntity<>(challenge, HttpStatus.OK);
        } else {
            logger.warn("Webhook verification failed. Invalid mode or verify token.");
            return new ResponseEntity<>("Verification token mismatch", HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Webhook Event Endpoint (POST request)
     * Instagram sends POST requests with DM notifications.
     */
    @PostMapping
    public ResponseEntity<String> handleWebhookEvent(@RequestBody InstagramWebhookPayload payload) {
        logger.info("Webhook event received.");

        try {
            String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
            logger.info("Incoming Payload:\n{}", prettyJson);

            InstagramWebhookPayload.Messaging messaging = payload.getEntry().stream().findFirst()
                    .flatMap(entry -> entry.getMessaging().stream().findFirst())
                    .orElseThrow(() -> new RuntimeException("Webhook payload missing"));

            if(Boolean.parseBoolean(messaging.getMessage().getIsEcho())) {
                logger.warn("Echo request received. Ignoring");
                return new ResponseEntity<>(HttpStatus.OK);
            }

            InstagramWebhookPayload.Attachment attachment = messaging.getMessage().getAttachments().stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("Message missing attachments"));

            String cdn = attachment.getPayload().getUrl();
            processAndReplyToDm(cdn, messaging.getSender().getId());
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error processing webhook event: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Async // This method will run in a separate thread
    public void processAndReplyToDm(String cdn, String senderIgId) {
        logger.info("Asynchronously processing DM from senderId : {}, cdn : {}", senderIgId, cdn);
        // Simulate some processing time
        try {
            Thread.sleep(2000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String replyMessage = String.format("CDN : %s\nUserID : %s", cdn, senderIgId);
        instagramDMChatService.sendInstagramDm(senderIgId, replyMessage);
        logger.info("Asynchronous reply sent to {}", senderIgId);
    }
}
