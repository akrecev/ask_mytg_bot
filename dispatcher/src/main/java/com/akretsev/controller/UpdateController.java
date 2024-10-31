package com.akretsev.controller;

import com.akretsev.utils.MessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class UpdateController {
    private final MessageProcessor messageProcessor;

    private TelegramBot telegramBot;

    public UpdateController(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdates(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (update.getMessage() != null) {
            distributeMessageByType(update);
        } else {
            log.error("Received unsupported message type {}", update);
        }
    }

    private void distributeMessageByType(Update update) {
        Message message = update.getMessage();

        if (message.getText() != null) {
            processTextMessage(update);
        } else if (message.getDocument() != null) {
            processDocMessage(update);
        } else if (message.getPhoto() != null) {
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void processTextMessage(Update update) {
        SendMessage sendMessage = messageProcessor.generateSandMessageWithText(
                update, update.getMessage().getText());
        setView(sendMessage);
    }

    private void processDocMessage(Update update) {}

    private void processPhotoMessage(Update update) {}

    private void setUnsupportedMessageTypeView(Update update) {
        SendMessage sendMessage =
                messageProcessor.generateSandMessageWithText(update, "Неподдерживаемый тип сообщения");
        setView(sendMessage);
    }

    private void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }
}
