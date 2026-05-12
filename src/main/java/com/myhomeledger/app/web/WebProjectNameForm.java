package com.myhomeledger.app.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WebProjectNameForm {

    @NotBlank(message = "Project name is required")
    @Size(min = 1, max = 255)
    private String projectName;
}
