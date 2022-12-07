package com.example.redissaveread;

import com.example.redissaveread.config.CacheNames;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.example.redissaveread.config.CacheNames.POST;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
//    private final RedisTemplate<String, Object> redisTemplate;
//    private final RedisTemplate<String, RedisTemplate<String, Object>> carts;
    private final RedisTemplate<String, ItemDto> redisTemplate;
//    @Cacheable(value = CacheNames.POST)
    public void addItem(ItemDto itemDto, Long memberId){
        String key = KeyGen.cartKeyGenerate(memberId);
//        List<ItemDto> items = findByMemberId(memberId);
//        items.add(itemDto);
//        redisTemplate.opsForList().rightPushAll(key, items);
        redisTemplate.opsForList().rightPush(key, itemDto);
//        redisTemplate.expire(key, 60, TimeUnit.SECONDS);
    }

    @Cacheable(value = CacheNames.POST)
    public List<ItemDto> findByMemberId(Long memberId){
        String key = KeyGen.cartKeyGenerate(memberId);
        Long len = redisTemplate.opsForList().size(key);
        return len == 0 ? new ArrayList<>() : redisTemplate.opsForList().range(key, 0, len-1);
    }


    public void saveAll(List<ItemDto> items, Long cartId){
        RedisSerializer keySerializer = redisTemplate.getStringSerializer();
        RedisSerializer valueSerializer = redisTemplate.getValueSerializer();

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            items.forEach(itemDto -> {
                String key = KeyGen.cartKeyGenerate(cartId);
                connection.listCommands().rPush(keySerializer.serialize(key),
                        valueSerializer.serialize(itemDto));
            });
            return null;
        });
    }

}
