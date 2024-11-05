package com.skillforge.backend.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscussionDTO {

    private String id;
    private String title;
    private String description;
    private String createdat;
    private String createdby;
    private List<DiscussionReplyDTO> discussionReplyList;
}
