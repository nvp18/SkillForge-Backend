package com.skillforge.backend.service;


import com.skillforge.backend.dto.DiscussionDTO;
import com.skillforge.backend.dto.DiscussionReplyDTO;
import com.skillforge.backend.dto.GenericDTO;

import java.security.Principal;
import java.util.List;

public interface DiscussionService {

    List<DiscussionDTO> getCourseDiscussions(String courseId, Principal connectedUser);

    GenericDTO postDiscussion(String courseId,DiscussionDTO discussionDTO, Principal connectedUser);

    GenericDTO replyToDiscussion(DiscussionReplyDTO replyDTO, String discussionId, Principal connectedUser);

    GenericDTO deleteDiscussion(String discussionId);


}
