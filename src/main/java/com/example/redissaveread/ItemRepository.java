package com.example.redissaveread;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
//    private final RedisTemplate<String, Object> redisTemplate;
//    private final RedisTemplate<String, RedisTemplate<String, Object>> carts;
    private final RedisTemplate<String, ItemDto> redisTemplate;
    public void addItem(ItemDto itemDto, Long memberId){
        String key = KeyGen.cartKeyGenerate(memberId);
        List<ItemDto> items = findByMemberId(memberId);
        items.add(itemDto);
        redisTemplate.opsForList().rightPushAll(key, items);
    }

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
