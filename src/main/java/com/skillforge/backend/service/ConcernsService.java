package com.skillforge.backend.service;

import com.skillforge.backend.dto.ConcernDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.ReplyDTO;

import java.security.Principal;
import java.util.List;

public interface ConcernsService {

    List<ConcernDTO>  getEmployeeConcerns(Principal connectedUser);

    GenericDTO raiseAConcern(ConcernDTO concernDTO, Principal connectedUser);

    GenericDTO replyToConcern(ReplyDTO replyDTO, String concernId, Principal connectedUser);

    List<ConcernDTO> getAllConcerns();

}
