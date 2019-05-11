package com.fintech.domain;

import javax.persistence.*;

/**
 * @author d.mikheev 08.05.19
 */
@Entity
public class Account {
    @Id
    private String id;
    private Long amount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    private Client client;

    public Account(String id, Long amount, Client client) {
        this.id = id;
        this.amount = amount;
        this.client = client;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
