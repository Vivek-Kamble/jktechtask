package jktech.task.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {
    @NotBlank
    @Schema(description = "Username")
    @Size(min = 4, max = 20)
    private String username;

    @NotBlank
    @Schema(description = "password")
    @Size(min = 6, max = 100)
    private String password;
}
