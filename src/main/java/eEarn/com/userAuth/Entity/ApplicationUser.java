package eEarn.com.userAuth.Entity;


import eEarn.com.userAuth.Roles.ApplicationUserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(nullable = false,
            length = 50)
    private String firstName;
    @Column(length = 50)
    private String lastName;
    @Column(unique = true)
    private String username;
    private String password;

    @Column(unique = true
            , nullable = false)
    private String email;
    @Column(unique = true
            , nullable = false)
    private String phoneNumber;
    @Enumerated(STRING)
    private ApplicationUserRole userRole;
    private boolean isEnabled = false;
    private boolean isCredentialsNonExpired = false;
    private boolean isAccountNonLocked = false;
    private boolean isAccountNonExpired = false;

    private String accountType;

   /* @OneToMany(mappedBy = "ownerId")
    private List<GlobalCard> globalCardOwner;

    @OneToMany(mappedBy = "visitorId")
    private List<GlobalCard> globalCardVisitor;*/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRole.getAuthority();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }


}
