package com.myhomeledger.app.web;

import com.myhomeledger.app.costcenter.dto.ProjectCreateRequest;
import com.myhomeledger.app.costcenter.service.ProjectService;
import com.myhomeledger.app.user.entity.UserEntity;
import com.myhomeledger.app.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebHomeController {

    private final UserRepository userRepository;
    private final ProjectService projectService;

    @GetMapping("/home")
    public String home(
            Authentication authentication,
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String message,
            Model model) {
        UUID userId = (UUID) authentication.getPrincipal();
        log.info("Home page requested for user {}", userId);
        populateDashboard(authentication, model);
        model.addAttribute("projectForm", new WebProjectNameForm());
        model.addAttribute("error", error);
        model.addAttribute("message", message);
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
        log.info("Create project submitted for user {}", userId);
        ProjectCreateRequest request = new ProjectCreateRequest();
        request.setProjectName(projectForm.getProjectName().trim());
        request.setUserId(userId);
        projectService.create(request);
        return "redirect:/home";
    }

    @PostMapping("/web/projects/{projectId}/delete")
    public String deleteProject(
            @PathVariable UUID projectId,
            Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        // HTML forms cannot send DELETE without JS; we intentionally use POST for this web action.
        log.info("Delete project submitted from home for user {} project {}", userId, projectId);
        var project = projectService.getById(projectId);
        if (!project.getUserId().equals(userId)) {
            log.warn("Delete project blocked: user {} does not own project {}", userId, projectId);
            return "redirect:/home";
        }
        try {
            projectService.delete(projectId);
            log.info("Project {} deleted by user {}", projectId, userId);
            return "redirect:/home?message=Project%20deleted";
        } catch (RuntimeException e) {
            log.error("Project delete failed for user {} project {}", userId, projectId, e);
            return "redirect:/home?error=" + urlEncode(e.getMessage());
        }
    }

    private void populateDashboard(Authentication authentication, Model model) {
        UUID userId = (UUID) authentication.getPrincipal();
        String username = userRepository.findById(userId)
                .map(UserEntity::getUserName)
                .orElse("there");
        model.addAttribute("username", username);
        model.addAttribute("projects", projectService.listByUserId(userId));
    }

    private static String urlEncode(String value) {
        if (value == null) {
            return "";
        }
        try {
            return java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8);
        } catch (RuntimeException e) {
            return "";
        }
    }
}
