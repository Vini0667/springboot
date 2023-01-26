package com.fourcatsdev.aula01.Model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Papel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String papel; 

    @ManyToMany(mappedBy = "papeis", fetch = FetchType.EAGER)
    private List<Usuario> users;

    public Papel() { }

    public Papel(String papel) {
        this.papel = papel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public List<Usuario> getUsers() {
        return users;
    }

    public void setUsers(List<Usuario> users) {
        this.users = users;
    }
}
