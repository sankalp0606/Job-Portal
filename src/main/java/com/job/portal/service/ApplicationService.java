package com.job.portal.service;

import com.job.portal.entity.Application;
import com.job.portal.entity.User;
import com.job.portal.enums.Role;
import com.job.portal.repository.ApplicationRepository;
import com.job.portal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    // ✅ Get all applications posted to the recruiter's jobs
    public List<Application> getApplicationsForRecruiter(String recruiterEmail) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        if (recruiter.getRole() != Role.RECRUITER) {
            throw new RuntimeException("Only recruiters can view applications");
        }

        return applicationRepository.findByJob_Recruiter(recruiter);
    }

    // ✅ Get applications submitted by a specific job seeker
    public List<Application> getApplicationsByJobSeeker(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.JOB_SEEKER) {
            throw new RuntimeException("Only job seekers can view their applications");
        }

        return applicationRepository.findByUser(user);
    }
}
