package com.skillforge.backend.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscussionReplyDTO {

    private String id;
    private String repliedBy;
    private String reply;
    private String repliedat;
}
