package com.skillforge.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyDTO {

    private String id;

    private String reply;

    private String repliedat;

    private String repliedBy;

}
