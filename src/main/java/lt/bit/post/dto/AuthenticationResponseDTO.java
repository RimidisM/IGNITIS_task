package lt.bit.post.dto;

import java.io.Serializable;

/**
 *
 * @author rimid
 */
public class AuthenticationResponseDTO implements Serializable {

    private String jwtToken;

    public AuthenticationResponseDTO(String jwttoken) {
        this.jwtToken = jwttoken;
    }
    
    public AuthenticationResponseDTO() {
    }

    public String getJwtToken() {
        return this.jwtToken;
    }
}
