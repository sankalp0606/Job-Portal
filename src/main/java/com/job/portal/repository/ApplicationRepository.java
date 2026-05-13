package com.job.portal.repository;

import com.job.portal.entity.Application;
import com.job.portal.entity.Job;
import com.job.portal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByJob_Recruiter(User recruiter);

    List<Application> findByUser(User user);

    boolean existsByUserAndJob(User user, Job job);
}
