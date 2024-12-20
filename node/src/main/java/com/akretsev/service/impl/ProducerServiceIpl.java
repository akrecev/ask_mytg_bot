package com.akretsev.service.impl;

import static com.akretsev.model.RabbitQueue.ANSWER_MESSAGE;

import com.akretsev.service.ProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class ProducerServiceIpl implements ProducerService {
    private final RabbitTemplate rabbitTemplate;

    public ProducerServiceIpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produceAnswer(SendMessage sendMessage) {
        rabbitTemplate.convertAndSend(ANSWER_MESSAGE, sendMessage);
    }
}
