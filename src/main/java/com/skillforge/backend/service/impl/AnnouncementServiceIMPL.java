package com.skillforge.backend.service.impl;

import com.skillforge.backend.dto.AnnouncementDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.entity.Announcement;
import com.skillforge.backend.entity.Course;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.exception.InternalServerException;
import com.skillforge.backend.exception.ResourceNotFoundException;
import com.skillforge.backend.repository.AnnouncementRepository;
import com.skillforge.backend.repository.CourseRepository;
import com.skillforge.backend.service.AnnouncementService;
import com.skillforge.backend.utils.ObjectMappers;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnnouncementServiceIMPL implements AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public GenericDTO postAnnouncement(AnnouncementDTO announcementDTO, String courseId, Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            Course course =  courseRepository.findByCourseid(courseId);
            if(course==null) {
                throw new ResourceNotFoundException();
            }
            Announcement announcement = ObjectMappers.announcementDTOToAnnouncement(announcementDTO);
            announcement.setCreatedby(user.getUsername());
            announcement.setCourse(course);
            announcementRepository.save(announcement);
            return GenericDTO.builder()
                    .message("Announcement Posted Successfully")
                    .build();
        }catch (Exception e) {
            if(e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException();
            }
            throw new InternalServerException();
        }
    }

    @Override
    public List<AnnouncementDTO> getCourseAnnouncements(String courseId) {
        try {
            List<Announcement> courseAnnouncements = announcementRepository.findByCourseCourseid(courseId);
            List<AnnouncementDTO> announcementDTOS = new ArrayList<>();
            if(courseAnnouncements.isEmpty()) {
                return announcementDTOS;
            }
            for(Announcement announcement: courseAnnouncements) {
                announcementDTOS.add(ObjectMappers.announcementToDTO(announcement));
            }
            return announcementDTOS;
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    @Transactional
    public GenericDTO deleteAnnouncement(String announcementId) {
        try {
            Announcement announcement = announcementRepository.findById(announcementId);
            if(announcement==null) {
                throw new ResourceNotFoundException();
            }
            announcementRepository.deleteById(announcementId);
            return GenericDTO.builder()
                    .message("Announcement Deleted Successfully")
                    .build();
        }catch (Exception e) {
            if(e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException();
            }
            throw new InternalServerException();
        }
    }

    @Override
    public GenericDTO editAnnouncement(String announcementId, AnnouncementDTO announcementDTO, Principal connectedUser) {
        try {
            Announcement announcement = announcementRepository.findById(announcementId);
            if(announcement==null) {
                throw new ResourceNotFoundException();
            }
            announcement.setUpdatedat(LocalDateTime.now());
            announcement.setTitle(announcementDTO.getTitle());
            announcement.setDescription(announcementDTO.getDescription());
            announcementRepository.save(announcement);
            return  GenericDTO.builder()
                    .message("Announcement Updated Successfully")
                    .build();
        } catch (Exception e) {
            if(e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException();
            }
            throw new InternalServerException();
        }
    }

    @Override
    public AnnouncementDTO getAnnouncement(String announcementId) {
        try {
            Announcement announcement = announcementRepository.findById(announcementId);
            if(announcement==null) {
                throw new ResourceNotFoundException();
            }
            return ObjectMappers.announcementToDTO(announcement);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

}
