package com.ucomputersa.auth.domain.entity;

import com.ucomputersa.common.enumObject.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity implements UserDetails {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "member_id", unique = true, nullable = false)
    @Id
    private String memberId;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true)
    private String phone;

    @Column
    private String avatar;

    @Column
    private String password;


    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "modification_date", nullable = false)
    private LocalDateTime modificationDate;

    @Column(name = "last_login_date", nullable = false)
    private LocalDateTime lastLoginDate;

    @Column(name = "is_oauth_enable", nullable = false)
    private Boolean isOauthEnable;

    @Column(nullable = false)
    private List<RoleEnum> roles;

    @Column
    @OneToMany(mappedBy = "addressId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AddressEntity> address = new java.util.ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        LinkedList<SimpleGrantedAuthority> list = new LinkedList<>();
        for (RoleEnum roleEnum : roles) {
            list.add(new SimpleGrantedAuthority(roleEnum.name()));
        }
        return list;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }

}
