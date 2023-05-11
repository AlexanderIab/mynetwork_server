package com.iablonski.mynetwork.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, updatable = false)
    private String username;
    @Column(nullable = false)
    private String firstname;
    @Column(nullable = false)
    private String lastname;
    @Column(unique = true)
    private String email;
    @Column(columnDefinition = "text")
    private String bio;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "user", orphanRemoval = true)
    private List<Post> postList = new ArrayList<>();
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime created;
    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    public User(Long id,
                String nickname,
                String email,
                String password,
                Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = nickname;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    @PrePersist
    public void onCreate() {
        this.created = LocalDateTime.now();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
