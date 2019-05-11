package com.fintech.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author d.mikheev 08.05.19
 */
@Entity
public class Client {
    @Id
    private Long id;
    private String name;
    private String phone;
    private String mail;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private Collection<Account> accounts = new ArrayList<>();
    private Timestamp created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Collection<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Collection<Account> accounts) {
        this.accounts = accounts;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
}
