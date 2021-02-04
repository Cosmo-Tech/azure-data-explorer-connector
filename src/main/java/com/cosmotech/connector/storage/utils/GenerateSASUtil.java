package com.cosmotech.connector.storage.utils;

import java.nio.charset.StandardCharsets;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class in order to generate Shared Access Signature.
 * The Shared Access Signature allows user to access blobs, services, object stored in Azure Storage
 * @see "https://docs.microsoft.com/fr-fr/rest/api/storageservices/create-account-sas"
 */
public final class GenerateSASUtil {

  private final static String SHARED_ACCESS_SIGNATURE_DATA_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  private final static String TIMEZONE_UTC = "UTC";
  private final static String UTF8_ENCODING = "UTF-8";
  private final static String HMAC_SHA256 = "HmacSHA256";
  public static final String AZURE_API_VERSION="2019-12-12";

  /**
   * Generate a Shared Access Signature
   * N.B: protocol defined is HTTPS
   * @param accountName the storage account name
   * @param key the storage account key
   * @param permissions the permissions
   * @param service the service authorized
   * @param resourceType the resource authorized
   * @param duration the number of minute for the token duration
   * @return the Shared Access Signature
   */
  public static String getFileSAS(
      String accountName,
      String key,
      String permissions,
      String service,
      String resourceType,
      int duration){

    DateFormat dateFormat = new SimpleDateFormat(SHARED_ACCESS_SIGNATURE_DATA_FORMAT);
    dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE_UTC));
    Date startDate = new Date();
    String start = dateFormat.format(startDate);
    Calendar endCal = Calendar.getInstance();
    endCal.add(Calendar.MINUTE,duration);
    String expiry = dateFormat.format(endCal.getTime());

    String spr = "https";
    String rawSignature = constructSignature(
        accountName,
        permissions,
        service,
        resourceType,
        start,
        expiry,
        spr);

    String encryptedSignature = getHMAC256(key, rawSignature);

    try{
      return "sv=" + AZURE_API_VERSION +
                    "&ss=" + service +
                    "&srt=" + resourceType +
                    "&sp=" + permissions +
                    "&se=" + URLEncoder.encode(expiry, UTF8_ENCODING) +
                    "&st=" + URLEncoder.encode(start, UTF8_ENCODING) +
                    "&spr=" + spr +
                    "&sig=" + URLEncoder.encode(encryptedSignature, UTF8_ENCODING);
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * Construct the raw signature
   * @param accountName the storage account name
   * @param permissions the permissions
   * @param service the service authorized
   * @param resourceType the resource authorized
   * @param expiry the expire time formatted
   * @param start the start time formatted
   * @param spr the protocol used
   * @return the raw signature to encrypt
   */
  @NotNull
  private static String constructSignature(String accountName, String permissions,
      String service, String resourceType, String start, String expiry, String spr) {
    return accountName + "\n" +
        permissions +"\n" +
        service +"\n" +
        resourceType +"\n" +
        start + "\n" +
        expiry + "\n" +
                "\n" +
        spr +"\n" +
        AZURE_API_VERSION +"\n";
  }

  /**
   * Encrypt the signature
   * @param accountKey the account key
   * @param signStr the signature to encrypt
   * @return the signature encrypted
   */
  private static String getHMAC256(String accountKey, String signStr) {
    String signature;
    try {
      SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(accountKey),
          HMAC_SHA256);
      Mac sha256HMAC = Mac.getInstance(HMAC_SHA256);
      sha256HMAC.init(secretKey);
      signature = Base64.getEncoder().encodeToString(sha256HMAC.doFinal(signStr.getBytes(
          StandardCharsets.UTF_8)));
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    return signature;
  }
}