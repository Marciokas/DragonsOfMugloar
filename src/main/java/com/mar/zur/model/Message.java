package com.mar.zur.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mar.zur.util.EncryptionUtils;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

    private String adId;
    private String message;
    private String probability;
    private int encrypted;
    private int reward;
    private int expiresIn;

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getMessage() {
        return message;
    }

    public int getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(int encrypted) {
        this.encrypted = encrypted;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public void decrementExpiration() {
        setExpiresIn(getExpiresIn() - 1);
    }

    public boolean isMessageValid() {
        return expiresIn > 0;
    }

    public String getDecryptedAdId() {
        if (encrypted > 1) {
            System.out.println("Figure out what kind of encryption is used!");
        }
        return encrypted == 0 ? adId : EncryptionUtils.decryptStringBase64(adId);
    }

    @Override
    public String toString() {
        return "Message{" +
                "adId='" + adId + '\'' +
                ", reward=" + reward +
                ", expiresIn=" + expiresIn +
                ", probability='" + probability + '\'' +
                ", encrypted='" + encrypted + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }
        Message message1 = (Message) o;
        return reward == message1.reward &&
                expiresIn == message1.expiresIn &&
                adId.equals(message1.adId) &&
                Objects.equals(message, message1.message) &&
                Objects.equals(encrypted, message1.encrypted) &&
                Objects.equals(probability, message1.probability);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adId, message, probability, encrypted, reward, expiresIn);
    }
}
