package eEarn.com.userAuth.Roles;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApplicationUserPermission {
    BackendRead("backend:read"),
    BackendWrite("backend:write"),
    BackendListener("backend:listener"),
    BackendTeller("backed:teller"),
    DataRead("data:read"),
    DataWrite("data:write");
    private final String Permission;


}
