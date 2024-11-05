package com.skillforge.backend.service.impl;

import com.skillforge.backend.dto.DiscussionDTO;
import com.skillforge.backend.dto.DiscussionReplyDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.entity.*;
import com.skillforge.backend.exception.InternalServerException;
import com.skillforge.backend.exception.ResourceNotFoundException;
import com.skillforge.backend.repository.CourseRepository;
import com.skillforge.backend.repository.DiscussionReplyRepository;
import com.skillforge.backend.repository.DiscussionRepository;
import com.skillforge.backend.repository.EmployeeCourseRepository;
import com.skillforge.backend.service.DiscussionService;
import com.skillforge.backend.utils.ObjectMappers;
import com.skillforge.backend.utils.ROLES;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiscussionServiceIMPL implements DiscussionService {

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private DiscussionReplyRepository discussionReplyRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EmployeeCourseRepository employeeCourseRepository;


    @Override
    public List<DiscussionDTO> getCourseDiscussions(String courseId, Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            String role = user.getRole();
            if(role.equals(ROLES.EMPLOYEE.toString())) {
                EmployeeCourses employeeCourses = employeeCourseRepository.findByUserIdAndCourseId(user.getUserId(),courseId);
                if(employeeCourses==null) {
                    throw new ResourceNotFoundException();
                }
            }
            List<Discussions> discussions = discussionRepository.findByCourseCourseid(courseId);
            List<DiscussionDTO> discussionDTOS = new ArrayList<>();
            for(Discussions discussion : discussions) {
                discussionDTOS.add(ObjectMappers.discussiontoDiscussionDTO(discussion));
            }
            return discussionDTOS;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public GenericDTO postDiscussion(String courseId, DiscussionDTO discussionDTO, Principal connectedUser) {
        try {
            Course course = courseRepository.findByCourseid(courseId);
            if(course==null) {
                throw new ResourceNotFoundException();
            }
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            Discussions discussions = ObjectMappers.discussionDTOtodiscussion(discussionDTO);
            discussions.setCourse(course);
            discussions.setCreatedby(user.getUsername());
            discussions.setCreatedat(LocalDateTime.now());
            discussionRepository.save(discussions);
            return GenericDTO.builder()
                    .message("Discussion Posted Successfully")
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public GenericDTO replyToDiscussion(DiscussionReplyDTO replyDTO, String discussionId, Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            Discussions discussions = discussionRepository.findById(discussionId);
            if(discussions==null) {
                throw new ResourceNotFoundException();
            }
            String role = user.getRole();
            if(role.equals(ROLES.EMPLOYEE.toString())) {
                EmployeeCourses employeeCourses = employeeCourseRepository.findByUserIdAndCourseId(user.getUserId(),discussions.getCourse().getCourseid());
                if(employeeCourses==null) {
                    throw new ResourceNotFoundException();
                }
            }
            DiscussionReply discussionReply = ObjectMappers.discussionReplyDTOtoDiscussionReply(replyDTO);
            discussionReply.setDiscussions(discussions);
            discussionReply.setRepliedBy(user.getUsername());
            discussionReply.setRepliedat(LocalDateTime.now());
            discussionReplyRepository.save(discussionReply);
            return GenericDTO.builder()
                    .message("Reply To Discussion Posted Successfully")
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    @Transactional
    public GenericDTO deleteDiscussion(String discussionId) {
        try {
            Discussions discussions = discussionRepository.findById(discussionId);
            if(discussions == null) {
                throw new ResourceNotFoundException();
            }
            discussionRepository.delete(discussions);
            return GenericDTO.builder()
                    .message("Discussion Delete Successfully")
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

}
