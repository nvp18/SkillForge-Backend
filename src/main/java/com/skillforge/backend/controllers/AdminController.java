package com.skillforge.backend.controllers;

import com.skillforge.backend.dto.*;
import com.skillforge.backend.service.AnnouncementService;
import com.skillforge.backend.service.ConcernsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private ConcernsService concernsService;

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/getAllConcerns")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ConcernDTO>> getAllConcerns() {
        List<ConcernDTO> concernDTOS = concernsService.getAllConcerns();
        return ResponseEntity.ok().body(concernDTOS);
    }

    @PostMapping("/replyToConcern/{concernId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenericDTO> replyToConcern(@PathVariable("concernId") String concernId, @RequestBody ReplyDTO replyDTO, Principal connectedUser) {
        GenericDTO genericDTO = concernsService.replyToConcern(replyDTO,concernId,connectedUser);
        return ResponseEntity.ok().body(genericDTO);
    }

    @PostMapping("/postAnnouncement/{courseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenericDTO> postAnnouncement(@PathVariable("courseId") String courseId, @RequestBody AnnouncementDTO announcementDTO, Principal connectedUser) {
        GenericDTO genericDTO = announcementService.postAnnouncement(announcementDTO,courseId,connectedUser);
        return ResponseEntity.ok().body(genericDTO);
    }

    @GetMapping("/getAllAnnouncements/{courseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<AnnouncementDTO>> getCourseAnnouncements(@PathVariable("courseId") String courseId) {
        List<AnnouncementDTO> announcementDTOS = announcementService.getCourseAnnouncements(courseId);
        return ResponseEntity.ok().body(announcementDTOS);
    }

    @PutMapping("/editAnnouncement/{announcementId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenericDTO> editAnnouncement(@PathVariable("announcementId") String announcementId, @RequestBody AnnouncementDTO announcementDTO, Principal connectedUser) {
        GenericDTO genericDTO = announcementService.editAnnouncement(announcementId,announcementDTO,connectedUser);
        return ResponseEntity.ok().body(genericDTO);
    }

    @DeleteMapping("/deleteAnnouncement/{announcementId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GenericDTO> deleteAnnouncement(@PathVariable("announcementId") String announcementId) {
        GenericDTO genericDTO = announcementService.deleteAnnouncement(announcementId);
        return ResponseEntity.ok().body(genericDTO);
    }

    @GetMapping("/getAnnouncement/{announcementId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AnnouncementDTO> getAnnouncement(@PathVariable("announcementId") String announcementId) {
        AnnouncementDTO announcementDTO = announcementService.getAnnouncement(announcementId);
        return ResponseEntity.ok().body(announcementDTO);
    }
}
