package ru.maxawergy.cartservice.dto;

public class ItemDTO {
    private int count;
    private Long itemId;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
