package lt.bit.post.dto;

import java.io.Serializable;

/**
 *
 * @author rimid
 */
public class AuthenticationRequestDTO implements Serializable{
    
    private String email;
    private String password;

    public AuthenticationRequestDTO() {

    }

    public AuthenticationRequestDTO(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }

    public String getEmail() {
        return this.email;
    }

    public final void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public final void setPassword(String password) {
        this.password = password;
    }
}
