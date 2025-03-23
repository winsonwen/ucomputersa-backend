package com.ucomputersa.monolithic.domain;

import com.ucomputersa.monolithic.constant.RoleEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


@Getter
@Setter
@Data
@Builder
@Document
public class User implements UserDetails {
    @Id
    private String id;

    private String username;
    private String password;
    @NotNull
    private String firstName;
    private String lastName;

    private String email;

    @Indexed(unique = true)
    private String phoneNumber;
    private String countryCode;
    private LocalDateTime createdAt;
    private LocalDateTime modificationDate;
    private LocalDateTime lastLoginDate;

    private UserStatus status;
    private Dob dayOfBirth;
    private Address address;
    private String language;
    private List<String> optionalLanguages;

    // icon
    private byte[] avatar;
    private String rewardsPoints;

    private List<String> favoriteIds;
    private List<String> postIds;

    private List<RoleEnum> roles = new ArrayList<>();

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
