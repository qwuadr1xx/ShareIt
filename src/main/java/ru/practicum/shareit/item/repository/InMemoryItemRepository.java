package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {
    private long localId = 1;
    private final Map<Long, Item> items;
    private final Map<Long, List<Item>> itemToUser;

    @Override
    public Optional<Item> getItemById(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> getItemsByOwner(long userId) {
        return itemToUser.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public List<Item> searchItems(String text) {
        String searchText = text.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getAvailable() &&
                        (item.getName().toLowerCase().contains(searchText) ||
                                item.getDescription().toLowerCase().contains(searchText)))
                .collect(Collectors.toList());
    }

    @Override
    public Item saveItem(Item item, long userId) {
        Item localItem = item.toBuilder()
                .id(generateId())
                .ownerId(userId)
                .build();
        items.put(localItem.getId(), localItem);
        List<Item> itemsByOwner = itemToUser.computeIfAbsent(userId, l -> new ArrayList<>());
        itemsByOwner.add(localItem);
        return localItem;
    }

    private long generateId() {
        return localId++;
    }
}
