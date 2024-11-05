package com.skillforge.backend.service.impl;

import com.skillforge.backend.dto.ConcernDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.ReplyDTO;
import com.skillforge.backend.entity.ConcernReply;
import com.skillforge.backend.entity.Concerns;
import com.skillforge.backend.entity.User;
import com.skillforge.backend.exception.InternalServerException;
import com.skillforge.backend.exception.ResourceNotFoundException;
import com.skillforge.backend.repository.ConcernRepository;
import com.skillforge.backend.repository.ReplyRepository;
import com.skillforge.backend.service.ConcernsService;
import com.skillforge.backend.utils.ConcernStatus;
import com.skillforge.backend.utils.ObjectMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConcernsServiceIMPL implements ConcernsService {

    @Autowired
    ConcernRepository concernRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Override
    public List<ConcernDTO> getEmployeeConcerns(Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            String userId = user.getUserId();
            List<Concerns> concerns = concernRepository.findByUserUserId(userId);
            List<ConcernDTO> concernDTOS = new ArrayList<>();
            if(concerns.isEmpty()) {
                return concernDTOS;
            } else {
                for(Concerns concern : concerns) {
                    concernDTOS.add(ObjectMappers.concernsToConcernDTO(concern));
                }
            }
            return concernDTOS;
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public GenericDTO raiseAConcern(ConcernDTO concernDTO, Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            Concerns concerns = new Concerns();
            concerns.setUser(user);
            concerns.setDescription(concernDTO.getDescription());
            concerns.setCreatedat(LocalDateTime.now());
            concerns.setStatus(ConcernStatus.NOT_READ.name());
            concerns.setSubject(concernDTO.getSubject());
            concernRepository.save(concerns);
            return GenericDTO.builder()
                    .message("Concern Raised Successfully")
                    .build();
        }catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @Override
    public GenericDTO replyToConcern(ReplyDTO replyDTO,String concernId, Principal connectedUser) {
        try {
            User user = ((User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal());
            Optional<Concerns> concern = concernRepository.findById(concernId);
            if(concern.isEmpty()) {
                throw new ResourceNotFoundException();
            }
            ConcernReply concernReply = new ConcernReply();
            Concerns validConcern = concern.get();
            validConcern.setStatus(ConcernStatus.READ.name());
            concernRepository.save(validConcern);
            concernReply.setConcerns(concern.get());
            concernReply.setReply(replyDTO.getReply());
            concernReply.setRepliedat(LocalDateTime.now());
            concernReply.setRepliedBy(user.getUsername());
            replyRepository.save(concernReply);
            return GenericDTO.builder()
                    .message("Reply Posted Successfully")
                    .build();
        } catch (Exception e) {
            if(e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException();
            }
            throw new InternalServerException();
        }
    }

    @Override
    public List<ConcernDTO> getAllConcerns() {
        try {
            List<Concerns> concerns = concernRepository.findAll();
            List<ConcernDTO> concernDTOS = new ArrayList<>();
            for(Concerns concern : concerns) {
                concernDTOS.add(ObjectMappers.concernsToConcernDTO(concern));
            }
            return concernDTOS;
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }
}
