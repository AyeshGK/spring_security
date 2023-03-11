package com.noctus.spring_security.payload.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Pattern.List({
            @Pattern(regexp = "^(?=.*[0-9]).{8,}$", message = "Password must be at least 8 characters long and contain at least one digit"),
            @Pattern(regexp = "^(?=.*[a-z]).{8,}$", message = "Password must be at least 8 characters long and contain at least one lowercase letter"),
            @Pattern(regexp = "^(?=.*[A-Z]).{8,}$", message = "Password must be at least 8 characters long and contain at least one uppercase letter"),
            @Pattern(regexp = "^(?=.*[@#$%^&+=]).{8,}$", message = "Password must be at least 8 characters long and contain at least one special character (@#$%^&+=)")
    })
    private String password;

}
