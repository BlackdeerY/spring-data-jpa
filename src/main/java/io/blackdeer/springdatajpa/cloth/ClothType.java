package io.blackdeer.springdatajpa.cloth;

public enum ClothType {
    SHIRT("셔츠"),
    PANTS("바지"),
    JACKET("재킷");

    private final String korean;

    private ClothType(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}
