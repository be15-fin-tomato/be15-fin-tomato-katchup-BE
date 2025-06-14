package be15fintomatokatchupbe.config.security.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@Getter
public class CustomUserDetail implements UserDetails {
    private final Long userId;
    private final String loginId;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetail(Long userId, String loginId, String password, List<GrantedAuthority> authorities) {
        this.userId = userId;
        this.loginId = loginId;
        this.password = password;
        this.authorities = authorities != null ? authorities : List.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
