package com.structurizr.dsl;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.util.regex.Pattern;

abstract class AbstractParser {

    private static final int HTTP_OK_STATUS = 200;

    private static final Pattern VIEW_KEY_PATTERN = Pattern.compile("[\\w-]+");

    void validateViewKey(String key) {
        if (!VIEW_KEY_PATTERN.matcher(key).matches()) {
            throw new RuntimeException("View keys can only contain the following characters: a-zA-0-9_-");
        }
    }

    String removeNonWordCharacters(String name) {
        return name.replaceAll("\\W", "");
    }

    protected String readFromUrl(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createSystem()) {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);

            if (response.getCode() == HTTP_OK_STATUS) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

        return "";
    }

}