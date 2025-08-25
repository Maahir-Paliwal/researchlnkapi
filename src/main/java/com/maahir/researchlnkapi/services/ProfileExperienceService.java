package com.maahir.researchlnkapi.services;

import com.maahir.researchlnkapi.model.repositories.ProfileExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileExperienceService {
    private final ProfileExperienceRepository profileExperienceRepository;
    private final Rele

    @Autowired
    public ProfileExperienceService(ProfileExperienceRepository profileExperienceRepository){
        this.profileExperienceRepository = profileExperienceRepository;
    }


}
