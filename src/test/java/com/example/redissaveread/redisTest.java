package com.example.redissaveread;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class redisTest {

    @Autowired
    ItemRepository itemRepository;

    @Test//2649
    public void test_saveAll(){

        ItemDto itemDto = new ItemDto();
        List<ItemDto> items = new ArrayList<>();
        for(int i = 0; i < 100000; i++){
            itemDto.setName("item" + i);
            itemDto.setPrice(i);
            itemDto.setQuantity(i+1);
            items.add(itemDto);
        }


        long startTime = System.currentTimeMillis();
        itemRepository.saveAll(items, 1l);

        long endTime = System.currentTimeMillis();
        System.out.println(String.format("pipeline 저장 시간 : %20dms", endTime - startTime));
    }

    @Test//882
    public void test_readAll(){
        long startTime = System.currentTimeMillis();
        List<ItemDto> items = itemRepository.findByMemberId(1l);

        long endTime = System.currentTimeMillis();
        System.out.println(String.format("items 총 갯수 : %d개",items.size()));
        System.out.println(String.format("pipeline 조회 시간 : %dms", endTime - startTime));
    }
}
