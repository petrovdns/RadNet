package com.petrovdns.radnet.entity;

import com.petrovdns.radnet.entity.abstractEntity.AbstractEntity;
import com.petrovdns.radnet.entity.enums.ERole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class User extends AbstractEntity {
    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String userName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(columnDefinition = "text")
    private String bio;

    @ManyToMany(mappedBy = "likedUsers")
    private Set<Post> likedPosts = new HashSet<>();

    @Column(length = 3000)
    private String password;

    @ElementCollection(targetClass = ERole.class)
    @CollectionTable(name = "user_role",
    joinColumns = @JoinColumn(name = "user_id")) //referencedColumnName = "id"
    private Set<ERole> role = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Transient
    private Set<GrantedAuthority> authorities;
}
