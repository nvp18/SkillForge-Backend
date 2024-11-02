package com.skillforge.backend.service;


import com.skillforge.backend.dto.AnnouncementDTO;
import com.skillforge.backend.dto.GenericDTO;

import java.security.Principal;
import java.util.List;

public interface AnnouncementService {

    GenericDTO postAnnouncement(AnnouncementDTO announcementDTO, String courseId,Principal connectedUser);

    List<AnnouncementDTO> getCourseAnnouncements(String courseId);

    GenericDTO deleteAnnouncement(String announcementId);

    GenericDTO editAnnouncement(String announcementId, AnnouncementDTO announcementDTO, Principal connectedUser);

    AnnouncementDTO getAnnouncement(String announcementId);

}
