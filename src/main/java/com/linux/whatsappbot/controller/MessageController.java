package com.linux.whatsappbot.controller;

import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("webhook")
public class MessageController {

    @PostMapping
    public String AnswerWebhook(@RequestBody RequestWebhook hook) throws IOException {
        for (Message message : hook.getMessages()) {
            if (message.getFromMe())
                continue;

            String option = message.getBody().split(" ")[0].toLowerCase();
            switch (option) {
                case "chatid" -> ApiWA.sendChatId(message.getChatId());
                case "file" -> {
                    final String[] texts = message.getBody().split(" ");
                    if (texts.length > 1)
                        ApiWA.sendFile(message.getChatId(), texts[1]);
                }
                case "ogg" -> ApiWA.sendOgg(message.getChatId());
                case "geo" -> ApiWA.sendGeo(message.getChatId());
                case "group" -> ApiWA.createGroup(message.getAuthor());
                default -> ApiWA.sendDefault(message.getChatId());
            }

        }
        return "ok";
    }
}

