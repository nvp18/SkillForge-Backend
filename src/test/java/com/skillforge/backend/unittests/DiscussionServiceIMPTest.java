package com.skillforge.backend.unittests;

import com.skillforge.backend.dto.DiscussionDTO;
import com.skillforge.backend.dto.DiscussionReplyDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.entity.Course;
import com.skillforge.backend.entity.DiscussionReply;
import com.skillforge.backend.entity.Discussions;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.exception.InternalServerException;
import com.skillforge.backend.exception.ResourceNotFoundException;
import com.skillforge.backend.repository.CourseRepository;
import com.skillforge.backend.repository.DiscussionReplyRepository;
import com.skillforge.backend.repository.DiscussionRepository;
import com.skillforge.backend.repository.EmployeeCourseRepository;
import com.skillforge.backend.service.impl.DiscussionServiceIMPL;
import com.skillforge.backend.utils.ROLES;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DiscussionServiceIMPTest {

    @Mock
    private DiscussionRepository discussionRepository;

    @Mock
    private DiscussionReplyRepository discussionReplyRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EmployeeCourseRepository employeeCourseRepository;

    @InjectMocks
    private DiscussionServiceIMPL discussionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCourseDiscussions_Success() {
        User user = new User();
        user.setRole("ADMIN");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        List<Discussions> discussionsList = new ArrayList<>();
        DiscussionReply discussionReply = new DiscussionReply();
        discussionReply.setId("1234");
        discussionReply.setRepliedBy("1234");
        discussionReply.setRepliedat(LocalDateTime.now());
        discussionReply.setReply("1234");
        Discussions discussions = new Discussions();
        discussions.setCreatedat(LocalDateTime.now());
        discussions.setId("12343");
        discussions.setDiscussionReplyList(Arrays.asList(discussionReply));
        discussions.setCreatedby("sample");
        discussions.setTitle("title");
        discussions.setDescription("12343");
        discussionsList.add(discussions);

        when(discussionRepository.findByCourseCourseid("course1")).thenReturn(discussionsList);

        List<DiscussionDTO> result = discussionService.getCourseDiscussions("course1", principal);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetCourseDiscussions_ResourceNotFoundException() {
        User user = new User();
        user.setRole(ROLES.EMPLOYEE.toString());
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        when(employeeCourseRepository.findByUserIdAndCourseId(anyString(), anyString())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> discussionService.getCourseDiscussions("course1", principal));
    }

    @Test
    void testGetCourseDiscussions_InternalServerException() {
        User user = new User();
        user.setUserId("123");
        user.setRole(ROLES.EMPLOYEE.toString());
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        when(employeeCourseRepository.findByUserIdAndCourseId("123", "course1")).thenThrow(new RuntimeException());

        assertThrows(InternalServerException.class, () -> discussionService.getCourseDiscussions("course1", principal));
    }

    @Test
    void testPostDiscussion_Success() {
        User user = new User();
        user.setUsername("testuser");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        Course course = new Course();
        when(courseRepository.findByCourseid("course1")).thenReturn(course);

        DiscussionDTO discussionDTO = new DiscussionDTO();

        GenericDTO result = discussionService.postDiscussion("course1", discussionDTO, principal);

        assertNotNull(result);
        assertEquals("Discussion Posted Successfully", result.getMessage());
    }

    @Test
    void testPostDiscussion_ResourceNotFoundException() {
        when(courseRepository.findByCourseid("course1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> discussionService.postDiscussion("course1", new DiscussionDTO(), mock(Principal.class)));
    }

    @Test
    void testPostDiscussion_InternalServerException() {
        when(courseRepository.findByCourseid("course1")).thenThrow(new RuntimeException());

        assertThrows(InternalServerException.class, () -> discussionService.postDiscussion("course1", new DiscussionDTO(), mock(Principal.class)));
    }

    @Test
    void testReplyToDiscussion_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setRole("ADMIN");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        Discussions discussions = new Discussions();
        Course course = new Course();
        course.setCourseid("course1");
        course.setCourseTags("tags");
        course.setCourseDescription("desc");
        course.setDays(12);
        course.setCourseName("testcourse");
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        discussions.setCourse(course);

        when(discussionRepository.findById("discussion1")).thenReturn(discussions);

        GenericDTO result = discussionService.replyToDiscussion(new DiscussionReplyDTO(), "discussion1", principal);

        assertNotNull(result);
        assertEquals("Reply To Discussion Posted Successfully", result.getMessage());
    }

    @Test
    void testReplyToDiscussion_ResourceNotFoundException() {
        when(discussionRepository.findById("discussion1")).thenReturn(null);
        User user = new User();
        user.setUsername("testuser");
        user.setRole("ADMIN");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        assertThrows(ResourceNotFoundException.class, () -> discussionService.replyToDiscussion(new DiscussionReplyDTO(), "discussion1", principal));
    }

    @Test
    void testReplyToDiscussion_InternalServerException() {
        when(discussionRepository.findById("discussion1")).thenThrow(new RuntimeException());
        User user = new User();
        user.setUsername("testuser");
        user.setRole("ADMIN");
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        assertThrows(InternalServerException.class, () -> discussionService.replyToDiscussion(new DiscussionReplyDTO(), "discussion1", principal));
    }

    @Test
    void testReplyToDiscussion_ResourceNotFoundException2() {
        User user = new User();
        user.setUserId("123");
        user.setUsername("testuser");
        user.setRole(ROLES.EMPLOYEE.toString());
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        Discussions discussions = new Discussions();
        Course course = new Course();
        course.setCourseid("course1");
        course.setCourseTags("tags");
        course.setCourseDescription("desc");
        course.setDays(12);
        course.setCourseName("testcourse");
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        discussions.setCourse(course);

        when(discussionRepository.findById("discussion1")).thenReturn(discussions);
        when(employeeCourseRepository.findByUserIdAndCourseId("123","course1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> discussionService.replyToDiscussion(new DiscussionReplyDTO(), "discussion1", principal));
    }

    @Test
    void testDeleteDiscussion_Success() {
        Discussions discussions = new Discussions();
        when(discussionRepository.findById("discussion1")).thenReturn(discussions);

        GenericDTO result = discussionService.deleteDiscussion("discussion1");

        assertNotNull(result);
        assertEquals("Discussion Delete Successfully", result.getMessage());
        verify(discussionRepository, times(1)).delete(discussions);
    }

    @Test
    void testDeleteDiscussion_ResourceNotFoundException() {
        when(discussionRepository.findById("discussion1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> discussionService.deleteDiscussion("discussion1"));
    }

    @Test
    void testDeleteDiscussion_InternalServerException() {
        when(discussionRepository.findById("discussion1")).thenThrow(RuntimeException.class);

        assertThrows(InternalServerException.class, () -> discussionService.deleteDiscussion("discussion1"));
    }
}
