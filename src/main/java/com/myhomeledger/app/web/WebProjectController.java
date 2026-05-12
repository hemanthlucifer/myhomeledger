package com.myhomeledger.app.web;

import com.myhomeledger.app.costcenter.dto.BillCreateRequest;
import com.myhomeledger.app.costcenter.dto.BillFilterCriteria;
import com.myhomeledger.app.costcenter.dto.BillResponse;
import com.myhomeledger.app.costcenter.dto.ProjectResponse;
import com.myhomeledger.app.costcenter.service.BillService;
import com.myhomeledger.app.costcenter.service.CostService;
import com.myhomeledger.app.costcenter.service.ProjectService;
import com.myhomeledger.app.user.entity.UserEntity;
import com.myhomeledger.app.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebProjectController {

    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final BillService billService;
    private final CostService costService;

    @GetMapping("/web/projects/{projectId}")
    public String projectPage(
            @PathVariable UUID projectId,
            @RequestParam(required = false) Integer costId,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate billDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate billDateTo,
            Authentication authentication,
            Model model) {
        UUID userId = (UUID) authentication.getPrincipal();
        log.info("Project page requested for user {} project {}", userId, projectId);
        ProjectResponse project = requireOwnedProject(projectId, userId);
        if (project == null) {
            log.warn("Project page blocked: user {} does not own project {}", userId, projectId);
            return "redirect:/home";
        }
        BillFilterCriteria criteria = new BillFilterCriteria(projectId, costId, null, minAmount, maxAmount, billDateFrom, billDateTo);
        populateProjectModel(userId, project, criteria, model, defaultBillCreateForm());
        return "project";
    }

    @PostMapping("/web/projects/{projectId}/bills")
    public String createBill(
            @PathVariable UUID projectId,
            @Valid @ModelAttribute("billCreateForm") WebBillCreateForm billCreateForm,
            BindingResult bindingResult,
            Authentication authentication,
            Model model) {
        UUID userId = (UUID) authentication.getPrincipal();
        log.info("Create bill submitted for user {} project {}", userId, projectId);
        ProjectResponse project = requireOwnedProject(projectId, userId);
        if (project == null) {
            log.warn("Create bill blocked: user {} does not own project {}", userId, projectId);
            return "redirect:/home";
        }
        if (bindingResult.hasErrors()) {
            BillFilterCriteria criteria = new BillFilterCriteria(projectId, null, null, null, null, null, null);
            populateProjectModel(userId, project, criteria, model, billCreateForm);
            model.addAttribute("billCreateDialogOpen", true);
            return "project";
        }
        BillCreateRequest request = new BillCreateRequest();
        request.setProjectId(projectId);
        request.setCostId(billCreateForm.getCostId());
        request.setAmount(billCreateForm.getAmount());
        request.setBillDate(billCreateForm.getBillDate());
        request.setItems(billCreateForm.getItems().trim());
        billService.create(request);
        log.info("Bill created for user {} project {}", userId, projectId);
        return "redirect:/web/projects/" + projectId;
    }

    @PostMapping("/web/projects/{projectId}/bills/{billId}/delete")
    public String deleteBill(
            @PathVariable UUID projectId,
            @PathVariable UUID billId,
            Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        // HTML forms cannot send DELETE without JS; we intentionally use POST for this web action.
        log.info("Delete bill submitted for user {} project {} bill {}", userId, projectId, billId);
        ProjectResponse project = requireOwnedProject(projectId, userId);
        if (project == null) {
            log.warn("Delete bill blocked: user {} does not own project {}", userId, projectId);
            return "redirect:/home";
        }
        var bill = billService.getById(billId);
        if (!projectId.equals(bill.getProjectId())) {
            log.warn("Delete bill blocked: bill {} not in project {}", billId, projectId);
            return "redirect:/web/projects/" + projectId;
        }
        billService.delete(billId);
        log.info("Bill {} deleted by user {} from project {}", billId, userId, projectId);
        return "redirect:/web/projects/" + projectId;
    }

    @PostMapping("/web/projects/{projectId}/delete-from-project")
    public String deleteProjectFromProjectPage(
            @PathVariable UUID projectId,
            Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        // HTML forms cannot send DELETE without JS; we intentionally use POST for this web action.
        log.info("Delete project submitted from project page for user {} project {}", userId, projectId);
        ProjectResponse project = requireOwnedProject(projectId, userId);
        if (project == null) {
            log.warn("Delete project blocked: user {} does not own project {}", userId, projectId);
            return "redirect:/home";
        }
        try {
            projectService.delete(projectId);
            log.info("Project {} deleted by user {}", projectId, userId);
            return "redirect:/home?message=Project%20deleted";
        } catch (RuntimeException e) {
            log.error("Project delete failed for user {} project {}", userId, projectId, e);
            return "redirect:/web/projects/" + projectId + "?error=" + urlEncode(e.getMessage());
        }
    }

    private ProjectResponse requireOwnedProject(UUID projectId, UUID userId) {
        ProjectResponse project = projectService.getById(projectId);
        if (!project.getUserId().equals(userId)) {
            return null;
        }
        return project;
    }

    private static WebBillCreateForm defaultBillCreateForm() {
        WebBillCreateForm form = new WebBillCreateForm();
        form.setBillDate(LocalDate.now());
        return form;
    }

    private void populateProjectModel(
            UUID userId,
            ProjectResponse project,
            BillFilterCriteria criteria,
            Model model,
            WebBillCreateForm billCreateForm) {
        String username = userRepository.findById(userId)
                .map(UserEntity::getUserName)
                .orElse("there");
        model.addAttribute("username", username);
        model.addAttribute("project", project);
        List<BillResponse> bills = billService.listFiltered(userId, criteria);
        BigDecimal billsTotal = bills.stream()
                .map(BillResponse::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("bills", bills);
        model.addAttribute("billsTotal", billsTotal);
        model.addAttribute("filterCostId", criteria.costId());
        model.addAttribute("filterMinAmount", criteria.minAmount());
        model.addAttribute("filterMaxAmount", criteria.maxAmount());
        model.addAttribute("filterBillDateFrom", criteria.billDateFrom() != null ? criteria.billDateFrom().toString() : "");
        model.addAttribute("filterBillDateTo", criteria.billDateTo() != null ? criteria.billDateTo().toString() : "");
        model.addAttribute("costs", costService.getAll());
        model.addAttribute("billCreateForm", billCreateForm);
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
