package com.example.redissaveread.config;

import com.example.redissaveread.ItemDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRedisHttpSession
@EnableCaching
@RequiredArgsConstructor
public class RedisConfig {

    private final ObjectMapper objectMapper;

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
    @Bean
    public RedisCacheManager redisCacheManager() {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper))
                );

        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        redisCacheConfigurationMap
                .put(CacheNames.POST, redisCacheConfiguration.entryTtl(Duration.ofMinutes(5)));
        redisCacheConfigurationMap
                .put(CacheNames.GET, redisCacheConfiguration.entryTtl(Duration.ofMinutes(5)));
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory())
                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .cacheDefaults(redisCacheConfiguration)
                .build();

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