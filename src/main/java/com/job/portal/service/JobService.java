package com.job.portal.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.job.portal.dto.JobDTO;
import com.job.portal.entity.Application;
import com.job.portal.entity.Job;
import com.job.portal.entity.User;
import com.job.portal.enums.Role;
import com.job.portal.repository.ApplicationRepository;
import com.job.portal.repository.JobRepository;
import com.job.portal.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public Job createJob(JobDTO jobDTO, String recruiterEmail) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        if (recruiter.getRole() != Role.RECRUITER) {
            throw new RuntimeException("Only recruiters can post jobs");
        }

        Job job = Job.builder()
                .title(jobDTO.getTitle())
                .description(jobDTO.getDescription())
                .company(jobDTO.getCompany())
                .location(jobDTO.getLocation())
                .postedAt(LocalDate.now())
                .recruiter(recruiter)
                .build();

        return jobRepository.save(job);
    }

    // ✅ Get all jobs with optional search and pagination
    public List<Job> getAllJobs(int page, int size, String sortBy, String keyword) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy).descending());

        if (keyword != null && !keyword.isEmpty()) {
            return jobRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    keyword, keyword, pageRequest);
        } else {
            return jobRepository.findAll(pageRequest).getContent();
        }
    }

    // ✅ Delete a job (Recruiter only)
    public void deleteJob(Long jobId, String recruiterEmail) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiter().getEmail().equals(recruiterEmail)) {
            throw new RuntimeException("You are not authorized to delete this job");
        }

        jobRepository.delete(job);
    }

    // ✅ Apply to a job (Job Seeker only)
    public void applyToJob(Long jobId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.JOB_SEEKER) {
            throw new RuntimeException("Only job seekers can apply to jobs");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Optional: Prevent duplicate applications
        boolean alreadyApplied = applicationRepository.existsByUserAndJob(user, job);
        if (alreadyApplied) {
            throw new RuntimeException("You have already applied to this job");
        }

        Application application = Application.builder()
                .user(user)
                .job(job)
                .appliedDate(LocalDate.now())
                .build();

        applicationRepository.save(application);
    }
}
