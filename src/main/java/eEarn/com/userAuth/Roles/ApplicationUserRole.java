package eEarn.com.userAuth.Roles;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static eEarn.com.userAuth.Roles.ApplicationUserPermission.*;


@RequiredArgsConstructor
@Getter
public enum ApplicationUserRole {
    ADMIN(Sets.newHashSet(BackendRead,BackendWrite,DataRead,DataWrite,BackendListener,BackendTeller)),
    BUSINESS(Sets.newHashSet(DataRead,DataWrite,BackendRead,BackendWrite)),
    USER(Sets.newHashSet(DataRead,DataWrite));
    @Autowired
    private final Set<ApplicationUserPermission> permissionSet;

    public Set<SimpleGrantedAuthority> getAuthority(){
              Set<SimpleGrantedAuthority> simpleGrantedAuthorities=getPermissionSet().stream()
                      .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                      .collect(Collectors.toSet());
              simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return simpleGrantedAuthorities;
    }
}
