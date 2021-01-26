package com.linux.whatsappbot.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiWA {


    //@TODO create a yml properties file
    private static String API_URL = "";
    private static String TOKEN = "";


    public static CompletableFuture<Void> postJSON(URI uri,
                                                   Map<String, String> map)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(map);

        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode)
                .thenAccept(System.out::println);
    }

    public static void sendChatId(String chat_id) throws IOException {
        URI uri = URI.create(API_URL + "sendMessage?token=" + TOKEN);
        Map<String, String> map = new HashMap<>();
        map.put("body", "Your ID: " + chat_id);
        map.put("phone", chat_id);
        ApiWA.postJSON(uri, map);
    }

    public static void sendFile(String chat_id, String file_format) throws IOException {
        Map<String, String> formats = new HashMap<>();
        formats.put("doc", Base64Help.getDOC());
        formats.put("jpeg", Base64Help.getJPEG());
        formats.put("pdf", Base64Help.getPDFtring());
        formats.put("mp3", Base64Help.getMP3String());

        if (formats.containsKey(file_format)) {
            Map<String, String> map = new HashMap<>();
            map.put("phone", chat_id);
            map.put("body", formats.get(file_format));
            map.put("filename", "ThisIsFile");
            map.put("caption", "ThisIsCaption");
            URI uri = URI.create(API_URL + "sendFile?token=" + TOKEN);
            ApiWA.postJSON(uri, map);
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("phone", chat_id);
            map.put("body", "File not found");
            URI uri = URI.create(API_URL + "sendMessage?token=" + TOKEN);
            ApiWA.postJSON(uri, map);
        }
    }

    public static void sendOgg(String chat_id) throws IOException {
        URI uri = URI.create(API_URL + "sendAudio?token=" + TOKEN);
        String oggUri = "https://firebasestorage.googleapis.com/v0/b/chat-api-com.appspot.com/o/audio_2019-02-02_00-50-42.ogg?alt=media&token=a563a0f7-116b-4606-9d7d-172426ede6d1";
        Map<String, String> map = new HashMap<>();
        map.put("audio", oggUri);
        map.put("phone", chat_id);
        ApiWA.postJSON(uri, map);
    }

    public static void sendGeo(String chat_id) throws IOException {
        URI uri = URI.create(API_URL + "sendLocation?token=" + TOKEN);
        Map<String, String> map = new HashMap<>();
        map.put("lat", "52.53253");
        map.put("lng", "23.621578");
        map.put("address", "Your address");
        map.put("phone", chat_id);
        ApiWA.postJSON(uri, map);
    }

    public static void sendDefault(String chat_id) throws IOException {
        URI uri = URI.create(API_URL + "sendMessage?token=" + TOKEN);
        Map<String, String> map = new HashMap<>();
        map.put("phone", chat_id);
        map.put("body", "Menu:\nchatid - get chatid\ngeo - Get geolocation\n\nogg - Get a file with the entered format\nfile format - Получить файл с введенным форматом (doc, mp3, jpeg, pdf)\n group - To create a group");
        ApiWA.postJSON(uri, map);

    }

    public static void createGroup(String author) throws IOException {
        String phone = author.replace("@c.us", "");
        URI uri = URI.create(API_URL + "group?token=" + TOKEN);
        Map<String, String> map = new HashMap<>();
        map.put("groupName", "Group Java");
        map.put("phones", phone);
        map.put("messageText", "This is your group.");
        ApiWA.postJSON(uri, map);
    }

    public void post(String uri, String data) throws Exception {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();

        HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        System.out.println(response.statusCode());
    }
}
