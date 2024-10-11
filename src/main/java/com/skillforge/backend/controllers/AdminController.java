package com.skillforge.backend.controllers;

import com.skillforge.backend.dto.ConcernDTO;
import com.skillforge.backend.dto.GenericDTO;
import com.skillforge.backend.dto.ReplyDTO;
import com.skillforge.backend.service.ConcernsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    ConcernsService concernsService;

    @GetMapping("getAllConcerns")
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


}
