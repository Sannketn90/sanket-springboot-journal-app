package com.springboot.journalapp.Service;

import com.springboot.journalapp.Dto.JournalDTO;
import com.springboot.journalapp.Entity.Journal;
import com.springboot.journalapp.Entity.User;
import com.springboot.journalapp.Exception.JournalAccessDeniedException;
import com.springboot.journalapp.Exception.ResourceNotFoundException;
import com.springboot.journalapp.Mapper.JournalMapper;
import com.springboot.journalapp.Repository.JournalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalService {


    private final JournalRepository journalRepository;
    private final UserService userService;
    private final JournalMapper journalMapper;

    @Transactional
    public JournalDTO saveJournal(JournalDTO journalDTO, String username) {
        User user = userService.findByusername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        Journal journal = journalMapper.toEntity(journalDTO);
        journal.setUser(user);
        journal.setDate(LocalDate.now());

        Journal saved = journalRepository.save(journal);
        return journalMapper.toDto(saved);
    }

    public List<JournalDTO> getUserJournals(String username) {
        User user = userService.findByusername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        List<Journal> allJournals = journalRepository.findAll();
        List<JournalDTO> userJournalDTOs = new ArrayList<>();

        for (Journal journal : allJournals) {
            if (journal.getUser().getUsername().equals(username)) {
                userJournalDTOs.add(journalMapper.toDto(journal));
            }
        }

        return userJournalDTOs;
    }

    @Transactional
    public JournalDTO updateJournal(Long id, JournalDTO updatedJournalDTO, String username) {
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal not found with id: " + id));

        if (!journal.getUser().getUsername().equals(username)) {
            throw new JournalAccessDeniedException("Access denied for journal id: " + id);
        }

        if (updatedJournalDTO.getTitle() != null && !updatedJournalDTO.getTitle().isBlank()) {
            journal.setTitle(updatedJournalDTO.getTitle());
        }

        if (updatedJournalDTO.getContent() != null && !updatedJournalDTO.getContent().isBlank()) {
            journal.setContent(updatedJournalDTO.getContent());
        }

        journal.setDate(LocalDate.now());
        return journalMapper.toDto(journalRepository.save(journal));
    }

    public JournalDTO getJournalById(Long id, String username) {
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal not found with id: " + id));

        if (!journal.getUser().getUsername().equals(username)) {
            throw new JournalAccessDeniedException("Access denied for journal id: " + id);
        }

        return journalMapper.toDto(journal);
    }

    @Transactional
    public void deleteJournal(Long id, String username) {
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal not found with id: " + id));

        if (!journal.getUser().getUsername().equals(username)) {
            throw new JournalAccessDeniedException("Access denied for journal id: " + id);
        }

        journalRepository.delete(journal);
    }

}