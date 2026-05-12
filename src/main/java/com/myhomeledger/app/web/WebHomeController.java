package com.myhomeledger.app.web;

import com.myhomeledger.app.costcenter.dto.ProjectCreateRequest;
import com.myhomeledger.app.costcenter.service.ProjectService;
import com.myhomeledger.app.user.entity.UserEntity;
import com.myhomeledger.app.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class WebHomeController {

    private final UserRepository userRepository;
    private final ProjectService projectService;

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        populateDashboard(authentication, model);
        model.addAttribute("projectForm", new WebProjectNameForm());
        return "home";
    }

    @PostMapping("/web/projects")
    public String createProject(
            @Valid @ModelAttribute("projectForm") WebProjectNameForm projectForm,
            BindingResult bindingResult,
            Authentication authentication,
            Model model) {
        populateDashboard(authentication, model);
        if (bindingResult.hasErrors()) {
            model.addAttribute("projectFormOpen", true);
            return "home";
        }
        UUID userId = (UUID) authentication.getPrincipal();
        ProjectCreateRequest request = new ProjectCreateRequest();
        request.setProjectName(projectForm.getProjectName().trim());
        request.setUserId(userId);
        projectService.create(request);
        return "redirect:/home";
    }

    private void populateDashboard(Authentication authentication, Model model) {
        UUID userId = (UUID) authentication.getPrincipal();
        String username = userRepository.findById(userId)
                .map(UserEntity::getUserName)
                .orElse("there");
        model.addAttribute("username", username);
        model.addAttribute("projects", projectService.listByUserId(userId));
    }
}
