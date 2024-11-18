package com.skillforge.backend.unittests;

import com.skillforge.backend.controllers.AdminController;
import com.skillforge.backend.dto.AnnouncementDTO;
import com.skillforge.backend.dto.ConcernDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.ReplyDTO;
import com.skillforge.backend.service.AnnouncementService;
import com.skillforge.backend.service.ConcernsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {
    @Mock
    private ConcernsService concernsService;

    @Mock
    private AnnouncementService announcementService;

    @InjectMocks
    private AdminController adminController;

    public AdminControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllConcerns() {
        List<ConcernDTO> mockConcerns = Arrays.asList(new ConcernDTO(), new ConcernDTO());
        when(concernsService.getAllConcerns()).thenReturn(mockConcerns);

        ResponseEntity<List<ConcernDTO>> response = adminController.getAllConcerns();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockConcerns, response.getBody());
        verify(concernsService, times(1)).getAllConcerns();
    }

    @Test
    void testReplyToConcern() {
        String concernId = "1";
        ReplyDTO replyDTO = new ReplyDTO();
        Principal mockPrincipal = mock(Principal.class);
        GenericDTO mockResponse = new GenericDTO();

        when(concernsService.replyToConcern(replyDTO, concernId, mockPrincipal)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = adminController.replyToConcern(concernId, replyDTO, mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(concernsService, times(1)).replyToConcern(replyDTO, concernId, mockPrincipal);
    }

    @Test
    void testPostAnnouncement() {
        String courseId = "101";
        AnnouncementDTO announcementDTO = new AnnouncementDTO();
        Principal mockPrincipal = mock(Principal.class);
        GenericDTO mockResponse = new GenericDTO();

        when(announcementService.postAnnouncement(announcementDTO, courseId, mockPrincipal)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = adminController.postAnnouncement(courseId, announcementDTO, mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(announcementService, times(1)).postAnnouncement(announcementDTO, courseId, mockPrincipal);
    }

    @Test
    void testGetCourseAnnouncements() {
        String courseId = "101";
        List<AnnouncementDTO> mockAnnouncements = Arrays.asList(new AnnouncementDTO(), new AnnouncementDTO());

        when(announcementService.getCourseAnnouncements(courseId)).thenReturn(mockAnnouncements);

        ResponseEntity<List<AnnouncementDTO>> response = adminController.getCourseAnnouncements(courseId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockAnnouncements, response.getBody());
        verify(announcementService, times(1)).getCourseAnnouncements(courseId);
    }

    @Test
    void testEditAnnouncement() {
        String announcementId = "1";
        AnnouncementDTO announcementDTO = new AnnouncementDTO();
        Principal mockPrincipal = mock(Principal.class);
        GenericDTO mockResponse = new GenericDTO();

        when(announcementService.editAnnouncement(announcementId, announcementDTO, mockPrincipal)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = adminController.editAnnouncement(announcementId, announcementDTO, mockPrincipal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(announcementService, times(1)).editAnnouncement(announcementId, announcementDTO, mockPrincipal);
    }

    @Test
    void testDeleteAnnouncement() {
        String announcementId = "1";
        GenericDTO mockResponse = new GenericDTO();

        when(announcementService.deleteAnnouncement(announcementId)).thenReturn(mockResponse);

        ResponseEntity<GenericDTO> response = adminController.deleteAnnouncement(announcementId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(announcementService, times(1)).deleteAnnouncement(announcementId);
    }

    @Test
    void testGetAnnouncement() {
        String announcementId = "1";
        AnnouncementDTO mockAnnouncement = new AnnouncementDTO();

        when(announcementService.getAnnouncement(announcementId)).thenReturn(mockAnnouncement);

        ResponseEntity<AnnouncementDTO> response = adminController.getAnnouncement(announcementId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockAnnouncement, response.getBody());
        verify(announcementService, times(1)).getAnnouncement(announcementId);
    }
}
