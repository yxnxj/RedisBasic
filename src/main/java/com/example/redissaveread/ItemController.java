package com.example.redissaveread;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemRepository itemRepository;


    @PostMapping("/{id}")
    public String save(@PathVariable(name = "id") Long memberId, @RequestBody ItemDto itemDto){
        itemRepository.addItem(itemDto, memberId);
        log.info("save cart to cache :" + memberId +" - [" + itemDto + "]");
        return "success caching";
    }

    @GetMapping("/{id}")
    public Object getByMemberId(@PathVariable(name = "id") Long memberId){
        log.info("find cart by member id :" + memberId);
        return itemRepository.findByMemberId(memberId);
    }
}
