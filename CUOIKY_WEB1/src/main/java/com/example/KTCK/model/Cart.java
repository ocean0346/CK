package com.example.KTCK.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // List of cart items
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> items = new ArrayList<>();

    // New field for session ID (or user ID)
    @Column(name = "session_id", nullable = false)
    private String sessionId;

    // New field for the cart creation date
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    // Constructor for setting sessionId and createdDate at the time of cart creation
    public Cart(String sessionId) {
        this.sessionId = sessionId;
        this.createdDate = LocalDateTime.now();
    }

    // Default constructor (for JPA)
    public Cart() {
    }
}
