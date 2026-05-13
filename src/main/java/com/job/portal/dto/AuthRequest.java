package com.job.portal.dto;

import com.job.portal.enums.Role;
import lombok.Data;

@Data
public class AuthRequest {
    private String name;
    private String email;
    private String password;
    private Role role;  // Optional: JOB_SEEKER or RECRUITER
}
