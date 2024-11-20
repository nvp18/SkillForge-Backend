package com.skillforge.backend.unittests;

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
import com.skillforge.backend.service.impl.ConcernsServiceIMPL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ConcernServiceIMPLTests {

	@Mock
	private ConcernRepository concernRepository;

	@Mock
	private ReplyRepository replyRepository;

	@InjectMocks
	private ConcernsServiceIMPL concernsService;

	private User mockUser;
	private Principal mockPrincipal;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockUser = new User();
		mockUser.setUserId("123");
		mockUser.setUsername("john_doe");
		mockPrincipal = new UsernamePasswordAuthenticationToken(mockUser, null);
	}

	@Test
	void testGetEmployeeConcerns() {
		// Arrange
		Concerns mockConcern = new Concerns();
		mockConcern.setUser(mockUser);
		mockConcern.setCreatedat(LocalDateTime.now());
		ConcernReply concernReply = new ConcernReply();
		concernReply.setRepliedat(LocalDateTime.now());
		concernReply.setRepliedBy("abdc");
		mockConcern.setConcernReplies(List.of(concernReply));
		when(concernRepository.findByUserUserId("123")).thenReturn(List.of(mockConcern));

		// Act
		List<ConcernDTO> result = concernsService.getEmployeeConcerns(mockPrincipal);

		// Assert
		assertNotNull(result);
		assertEquals(1, result.size());
		Mockito.verify(concernRepository, times(1)).findByUserUserId("123");
	}

	@Test
	void testGetEmployeeConcernsFailure() {
		// Arrange
		Concerns mockConcern = new Concerns();
		mockConcern.setUser(mockUser);
		mockConcern.setCreatedat(LocalDateTime.now());
		ConcernReply concernReply = new ConcernReply();
		concernReply.setRepliedat(LocalDateTime.now());
		concernReply.setRepliedBy("abdc");
		mockConcern.setConcernReplies(List.of(concernReply));
		when(concernRepository.findByUserUserId("123")).thenThrow(new RuntimeException());

		// Act
		assertThrows(InternalServerException.class, () -> concernsService.getEmployeeConcerns(mockPrincipal));

		Mockito.verify(concernRepository, times(1)).findByUserUserId("123");
	}

	@Test
	void testRaiseAConcern() {
		// Arrange
		ConcernDTO concernDTO = new ConcernDTO();
		concernDTO.setDescription("Test Concern");
		concernDTO.setSubject("Test Subject");

		Concerns mockConcern = new Concerns();
		when(concernRepository.save(any(Concerns.class))).thenReturn(mockConcern);

		// Act
		GenericDTO result = concernsService.raiseAConcern(concernDTO, mockPrincipal);

		// Assert
		assertEquals("Concern Raised Successfully", result.getMessage());
		verify(concernRepository, times(1)).save(any(Concerns.class));
	}

	@Test
	void testRaiseAConcernFailure() {
		// Arrange
		ConcernDTO concernDTO = new ConcernDTO();
		concernDTO.setDescription("Test Concern");
		concernDTO.setSubject("Test Subject");

		when(concernRepository.save(any(Concerns.class))).thenThrow(new RuntimeException());

		// Act
		assertThrows(InternalServerException.class, () -> concernsService.raiseAConcern(concernDTO,mockPrincipal));

		// Assert
		verify(concernRepository, times(1)).save(any(Concerns.class));
	}

	@Test
	void testReplyToConcern_Success() {
		// Arrange
		ReplyDTO replyDTO = new ReplyDTO();
		replyDTO.setReply("Test Reply");

		Concerns mockConcern = new Concerns();
		when(concernRepository.findById("1")).thenReturn(Optional.of(mockConcern));
		when(replyRepository.save(any(ConcernReply.class))).thenReturn(new ConcernReply());

		// Act
		GenericDTO result = concernsService.replyToConcern(replyDTO, "1", mockPrincipal);

		// Assert
		assertEquals("Reply Posted Successfully", result.getMessage());
		verify(replyRepository, times(1)).save(any(ConcernReply.class));
	}

	@Test
	void testReplyToConcern_ConcernNotFound() {
		// Arrange
		when(concernRepository.findById("1")).thenReturn(Optional.empty());

		ReplyDTO replyDTO = new ReplyDTO();

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> {
			concernsService.replyToConcern(replyDTO, "1", mockPrincipal);
		});
		verify(replyRepository, never()).save(any());
	}

	@Test
	void testReplyToConcern_InternalServerException() {
		// Arrange
		ReplyDTO replyDTO = new ReplyDTO();
		replyDTO.setReply("Test Reply");

		Concerns mockConcern = new Concerns();
		when(concernRepository.findById("1")).thenReturn(Optional.of(mockConcern));
		when(replyRepository.save(any(ConcernReply.class))).thenThrow(new RuntimeException());

		// Act
		assertThrows(InternalServerException.class, () -> {
			concernsService.replyToConcern(replyDTO, "1", mockPrincipal);
		});

		// Assert
		verify(replyRepository, times(1)).save(any(ConcernReply.class));
	}

	@Test
	void testGetAllConcerns() {
		// Arrange
		Concerns mockConcern = new Concerns();
		User user = new User();
		user.setUsername("sample");
		mockConcern.setUser(user);
		mockConcern.setCreatedat(LocalDateTime.now());
		when(concernRepository.findAll()).thenReturn(List.of(mockConcern));

		// Act
		List<ConcernDTO> result = concernsService.getAllConcerns();

		// Assert
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(concernRepository, times(1)).findAll();
	}

	@Test
	void testGetAllConcernsFailure() {
		// Arrange
		Concerns mockConcern = new Concerns();
		mockConcern.setCreatedat(LocalDateTime.now());
		when(concernRepository.findAll()).thenThrow(new RuntimeException());

		// Act
		assertThrows(InternalServerException.class, () -> {
			concernsService.getAllConcerns();
		});

		// Assert
		verify(concernRepository, times(1)).findAll();
	}

	@Test
	void testGetEmployeeConcerns_EmptyList() {
		// Arrange
		when(concernRepository.findByUserUserId("123")).thenReturn(new ArrayList<>());

		// Act
		List<ConcernDTO> result = concernsService.getEmployeeConcerns(mockPrincipal);

		// Assert
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}
}
