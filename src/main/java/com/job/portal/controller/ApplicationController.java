package com.job.portal.controller;

import com.job.portal.entity.Application;
import com.job.portal.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // Get applications by the logged-in recruiter
    @GetMapping("/recruiter")
    public List<Application> getApplicationsForRecruiter(@AuthenticationPrincipal UserDetails userDetails) {
        return applicationService.getApplicationsForRecruiter(userDetails.getUsername());
    }

    // Get applications submitted by the logged-in job seeker
    @GetMapping("/me")
    public List<Application> getApplicationsByJobSeeker(@AuthenticationPrincipal UserDetails userDetails) {
        return applicationService.getApplicationsByJobSeeker(userDetails.getUsername());
    }
}
