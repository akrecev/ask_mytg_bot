package com.akretsev.controller;

import static com.akretsev.model.RabbitQueue.*;

import com.akretsev.service.UpdateProducer;
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
    private final UpdateProducer updateProducer;

    private TelegramBot telegramBot;

    public UpdateController(MessageProcessor messageProcessor, UpdateProducer updateProducer) {
        this.messageProcessor = messageProcessor;
        this.updateProducer = updateProducer;
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
            log.error("Unsupported message type is received: {}", update);
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
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    private void processDocMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        setFileReceivedView(update);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        setFileReceivedView(update);
    }

    private void setUnsupportedMessageTypeView(Update update) {
        SendMessage sendMessage =
                messageProcessor.generateSandMessageWithText(update, "Неподдерживаемый тип сообщения");
        setView(sendMessage);
    }

    private void setFileReceivedView(Update update) {
        SendMessage sendMessage =
                messageProcessor.generateSandMessageWithText(update, "Файл получен. Идет обработка...");
        setView(sendMessage);
    }

    private void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }
}
