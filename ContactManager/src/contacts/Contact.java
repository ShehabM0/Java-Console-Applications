package contacts;

import java.time.LocalDateTime;

class Contact {
    private String name, number;
    private LocalDateTime createdAt, updatedAt;

    Contact(String name, String number) {
        this.name = name;
        this.number = number;
        createdAt = LocalDateTime.now().withSecond(0).withNano(0);
        updatedAt = LocalDateTime.now().withSecond(0).withNano(0);
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void updateUpdateAt() {
        updatedAt = LocalDateTime.now().withSecond(0).withNano(0);
    }

    public String getName() {
        return name.isEmpty() ? "[no data]" : name;
    }

    public String getNumber() {
        return number.isEmpty() ? "[no data]" : number;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
