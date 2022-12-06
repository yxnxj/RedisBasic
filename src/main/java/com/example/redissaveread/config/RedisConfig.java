package com.example.redissaveread.config;

import com.example.redissaveread.ItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, ItemDto> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        // Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(ItemDto.class);
        RedisTemplate<String, ItemDto> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // redisTemplate.setValueSerializer(new StringRedisSerializer());
        // redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        return redisTemplate;
    }

//    @Bean
//    public RedisTemplate<String, RedisTemplate<String, Object>> carts(RedisConnectionFactory redisConnectionFactory) {
//        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
//        // Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(ItemDto.class);
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        // redisTemplate.setValueSerializer(new StringRedisSerializer());
//        // redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
//
//        RedisTemplate<String, RedisTemplate<String, Object>> carts = new RedisTemplate<>();
//        carts.setConnectionFactory(redisConnectionFactory);
//        carts.setKeySerializer(new StringRedisSerializer());
//        // redisTemplate.setValueSerializer(new StringRedisSerializer());
//        // redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        carts.setValueSerializer(genericJackson2JsonRedisSerializer);
//
//        return carts;
//    }
}