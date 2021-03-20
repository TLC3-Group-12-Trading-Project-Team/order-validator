package com.example.Redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class OrderMessagePublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    public OrderMessagePublisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic topic){
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    public void publish(String message){
        this.redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}