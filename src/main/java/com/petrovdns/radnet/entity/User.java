package com.petrovdns.radnet.entity;

import com.petrovdns.radnet.entity.abstractEntity.AbstractEntity;
import com.petrovdns.radnet.entity.enums.ERole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
public class User extends AbstractEntity implements UserDetails {
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
    private Set<ERole> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    public User() {
    }

    private User(Long id, String userName, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        setId(id);
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static User create(Long id, String userName, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        return new User(
                id, userName, email, password, authorities
        );
    }

    public static User create(String userName, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        return new User(
                null, userName, email, password, authorities
        );
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
