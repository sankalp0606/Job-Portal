package com.job.portal.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.job.portal.dto.JobDTO;
import com.job.portal.entity.Job;
import com.job.portal.service.JobService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // Create job (Recruiter only)
    @PostMapping("/create")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Job> createJob(@RequestBody JobDTO jobDTO, Principal principal) {
        Job createdJob = jobService.createJob(jobDTO, principal.getName());
        return ResponseEntity.ok(createdJob);
    }

    // Get all jobs with pagination, sorting and keyword search
    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size",defaultValue = "10") int size,
            @RequestParam(name = "sortBy",defaultValue = "id") String sortBy,
            @RequestParam(name = "keyword",defaultValue = "") String keyword
    ) {
        List<Job> jobs = jobService.getAllJobs(page, size, sortBy, keyword);
        return ResponseEntity.ok(jobs);
    }

    // Delete job (Recruiter only)
    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<String> deleteJob(@PathVariable Long jobId, Principal principal) {
        jobService.deleteJob(jobId, principal.getName());
        return ResponseEntity.ok("Job deleted successfully.");
    }

    // Apply to a job (Job Seeker only)
    @PostMapping("/{jobId}/apply")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<String> applyToJob(@PathVariable Long jobId, Principal principal) {
        jobService.applyToJob(jobId, principal.getName());
        return ResponseEntity.ok("Applied successfully.");
    }
}
