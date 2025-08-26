package com.flipkart.instagramdmwebhook.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InstagramWebhookPayload {

    private String object;
    private List<Entry> entry;

    // Getters and Setters
    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return "InstagramWebhookPayload{" +
            "object='" + object + '\'' +
            ", entry=" + entry +
            '}';
    }

    /**
     * Represents a single entry in the webhook payload.
     */
    public static class Entry {
        private long time;
        private String id;
        private List<Messaging> messaging;

        // Getters and Setters
        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Messaging> getMessaging() {
            return messaging;
        }

        public void setMessaging(List<Messaging> messaging) {
            this.messaging = messaging;
        }

        @Override
        public String toString() {
            return "Entry{" +
                "time=" + time +
                ", id='" + id + '\'' +
                ", messaging=" + messaging +
                '}';
        }
    }

    /**
     * Represents a messaging event.
     */
    public static class Messaging {
        private Sender sender;
        private Recipient recipient;
        private long timestamp;
        private Message message;

        // Getters and Setters
        public Sender getSender() {
            return sender;
        }

        public void setSender(Sender sender) {
            this.sender = sender;
        }

        public Recipient getRecipient() {
            return recipient;
        }

        public void setRecipient(Recipient recipient) {
            this.recipient = recipient;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "Messaging{" +
                "sender=" + sender +
                ", recipient=" + recipient +
                ", timestamp=" + timestamp +
                ", message=" + message +
                '}';
        }
    }

    /**
     * Represents the sender of the message.
     */
    public static class Sender {
        private String id;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Sender{" +
                "id='" + id + '\'' +
                '}';
        }
    }

    /**
     * Represents the recipient of the message.
     */
    public static class Recipient {
        private String id;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Recipient{" +
                "id='" + id + '\'' +
                '}';
        }
    }

    /**
     * Represents the message content.
     */
    public static class Message {
        private String mid;
        private List<Attachment> attachments;
        @JsonProperty("is_echo")
        private String isEcho;

        // Getters and Setters
        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public List<Attachment> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<Attachment> attachments) {
            this.attachments = attachments;
        }

        public String getIsEcho() {
            return isEcho;
        }

        public void setIsEcho(String isEcho) {
            this.isEcho = isEcho;
        }

        @Override
        public String toString() {
            return "Message{" +
                "mid='" + mid + '\'' +
                ", attachments=" + attachments +
                '}';
        }
    }

    /**
     * Represents an attachment in the message.
     */
    public static class Attachment {
        private String type;
        private Payload payload;

        // Getters and Setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Payload getPayload() {
            return payload;
        }

        public void setPayload(Payload payload) {
            this.payload = payload;
        }

        @Override
        public String toString() {
            return "Attachment{" +
                "type='" + type + '\'' +
                ", payload=" + payload +
                '}';
        }
    }

    /**
     * Represents the payload of an attachment, such as a reel.
     */
    public static class Payload {
        @JsonProperty("reel_video_id")
        private String reelVideoId;
        private String title;
        private String url;

        // Getters and Setters
        public String getReelVideoId() {
            return reelVideoId;
        }

        public void setReelVideoId(String reelVideoId) {
            this.reelVideoId = reelVideoId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Payload{" +
                "reelVideoId='" + reelVideoId + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
        }
    }
}
