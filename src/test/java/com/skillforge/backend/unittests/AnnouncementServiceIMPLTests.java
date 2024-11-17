
package com.skillforge.backend.unittests;

import com.skillforge.backend.dto.AnnouncementDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.entity.Announcement;
import com.skillforge.backend.entity.Course;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.exception.InternalServerException;
import com.skillforge.backend.exception.ResourceNotFoundException;
import com.skillforge.backend.repository.AnnouncementRepository;
import com.skillforge.backend.repository.CourseRepository;
import com.skillforge.backend.service.impl.AnnouncementServiceIMPL;
import com.skillforge.backend.utils.ObjectMappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnouncementServiceIMPLTests {

    @Mock
    private AnnouncementRepository announcementRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private AnnouncementServiceIMPL announcementService;

    private User mockUser;
    private Principal mockPrincipal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setUserId("123");
        mockUser.setUsername("john_doe");
        mockPrincipal = new UsernamePasswordAuthenticationToken(mockUser, null);
    }

    @Test
    void testPostAnnouncementSuccess() {
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setTitle("Test Title");
        dto.setDescription("Test Description");

        Course course = new Course();
        course.setCourseid("C01");

        Announcement announcement = new Announcement();
        when(courseRepository.findByCourseid("C01")).thenReturn(course);
        when(announcementRepository.save(any(Announcement.class))).thenReturn(announcement);

        GenericDTO result = announcementService.postAnnouncement(dto, "C01", mockPrincipal);

        assertEquals("Announcement Posted Successfully", result.getMessage());
        verify(announcementRepository, times(1)).save(any(Announcement.class));
    }

    @Test
    void testPostAnnouncementCourseNotFound() {
        AnnouncementDTO dto = new AnnouncementDTO();
        when(courseRepository.findByCourseid("C01")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            announcementService.postAnnouncement(dto, "C01", mockPrincipal);
        });
    }

    @Test
    void testPostAnnouncementInternalError() {
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setTitle("Test Title");
        dto.setDescription("Test Description");

        Course course = new Course();
        when(courseRepository.findByCourseid("C01")).thenReturn(course);
        when(announcementRepository.save(any(Announcement.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(InternalServerException.class, () -> {
            announcementService.postAnnouncement(dto, "C01", mockPrincipal);
        });
    }

    @Test
    void testGetCourseAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();
        Announcement announcement = new Announcement();
        announcement.setTitle("Test Title");
        announcement.setDescription("Test Description");
        announcement.setCreatedat(LocalDateTime.now());
        announcement.setUpdatedat(LocalDateTime.now());
        announcement.setId("123");
        announcement.setCourse(new Course());
        announcement.setCreatedby("sivesh");
        announcements.add(announcement);

        when(announcementRepository.findByCourseCourseid("C01")).thenReturn(announcements);


        List<AnnouncementDTO> result = announcementService.getCourseAnnouncements("C01");

        assertFalse(result.isEmpty());
        verify(announcementRepository, times(1)).findByCourseCourseid("C01");
    }

    @Test
    void testGetCourseAnnouncementsEmpty() {
        when(announcementRepository.findByCourseCourseid("C01")).thenReturn(new ArrayList<>());

        List<AnnouncementDTO> result = announcementService.getCourseAnnouncements("C01");

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetCourseAnnouncementsInternalError() {
        when(announcementRepository.findByCourseCourseid("C01")).thenThrow(new RuntimeException("Database error"));

        assertThrows(InternalServerException.class, () -> {
            announcementService.getCourseAnnouncements("C01");
        });
    }

    @Test
    void testDeleteAnnouncementSuccess() {
        Announcement announcement = new Announcement();
        when(announcementRepository.findById("A01")).thenReturn(announcement);

        GenericDTO result = announcementService.deleteAnnouncement("A01");

        assertEquals("Announcement Deleted Successfully", result.getMessage());
        verify(announcementRepository, times(1)).deleteById("A01");
    }

    @Test
    void testDeleteAnnouncementNotFound() {
        when(announcementRepository.findById("A01")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            announcementService.deleteAnnouncement("A01");
        });
    }

    @Test
    void testDeleteAnnouncementInternalError() {
        when(announcementRepository.findById("A01")).thenThrow(new RuntimeException("Database error"));

        assertThrows(InternalServerException.class, () -> {
            announcementService.deleteAnnouncement("A01");
        });
    }

    @Test
    void testEditAnnouncementSuccess() {
        Announcement announcement = new Announcement();
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setTitle("Updated Title");
        dto.setDescription("Updated Description");

        when(announcementRepository.findById("A01")).thenReturn(announcement);
        GenericDTO result = announcementService.editAnnouncement("A01", dto, mockPrincipal);

        assertEquals("Announcement Updated Successfully", result.getMessage());
        verify(announcementRepository, times(1)).save(any(Announcement.class));
    }

    @Test
    void testEditAnnouncementNotFound() {
        when(announcementRepository.findById("InvalidId")).thenReturn(null);

        AnnouncementDTO dto = new AnnouncementDTO();
        assertThrows(ResourceNotFoundException.class, () -> {
            announcementService.editAnnouncement("InvalidId", dto, mockPrincipal);
        });
    }

    @Test
    void testEditAnnouncementInternalError() {
        when(announcementRepository.findById("A01")).thenReturn(new Announcement());
        when(announcementRepository.save(any(Announcement.class))).thenThrow(new RuntimeException("Database error"));

        AnnouncementDTO dto = new AnnouncementDTO();
        assertThrows(InternalServerException.class, () -> {
            announcementService.editAnnouncement("A01", dto, mockPrincipal);
        });
    }

    @Test
    void testGetAnnouncementSuccess() {

        Announcement announcement = new Announcement();
        announcement.setTitle("Test Title");
        announcement.setDescription("Test Description");
        announcement.setCreatedat(LocalDateTime.now());
        announcement.setUpdatedat(LocalDateTime.now());
        announcement.setId("123");
        announcement.setCourse(new Course());
        announcement.setCreatedby("sivesh");
        when(announcementRepository.findById("A01")).thenReturn(announcement);


        AnnouncementDTO result = announcementService.getAnnouncement("A01");

        assertNotNull(result);
        verify(announcementRepository, times(1)).findById("A01");
    }

    @Test
    void testGetAnnouncementNotFound() {
        when(announcementRepository.findById("InvalidId")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            announcementService.getAnnouncement("InvalidId");
        });
    }

    @Test
    void testGetAnnouncementInternalError() {
        when(announcementRepository.findById("A01")).thenThrow(new RuntimeException("Database error"));

        assertThrows(InternalServerException.class, () -> {
            announcementService.getAnnouncement("A01");
        });
    }
}

