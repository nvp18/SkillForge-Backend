package com.skillforge.backend.dto;

import com.skillforge.backend.entity.Course;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnouncementDTO {

    private String id;

    private String title;

    private String description;

    private String createdat;

    private String updatedat;

    private String createdby;

}
