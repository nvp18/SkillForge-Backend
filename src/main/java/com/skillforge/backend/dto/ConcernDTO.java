package com.skillforge.backend.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConcernDTO {

    private String id;

    private String createdat;

    private String subject;

    private String description;

    private String status;

    private List<ReplyDTO> concernReplies;

    private String createdBy;

}
