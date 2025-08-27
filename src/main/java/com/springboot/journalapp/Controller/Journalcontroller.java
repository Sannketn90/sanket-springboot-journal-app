package com.springboot.journalapp.Controller;

import com.springboot.journalapp.Dto.JournalDTO;
import com.springboot.journalapp.Service.JournalService;
import com.springboot.journalapp.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/journals")
@RequiredArgsConstructor
@Tag(name = "Journal APIs", description = "Read, Insert, Update, Delete and ReadById")
public class Journalcontroller {


    private final JournalService journalService;

    @Operation(
            summary = "Get all journals for the current user",
            description = "Fetches all journal entries created by the authenticated user.")
    @GetMapping("/getall")
    public ResponseEntity<ApiResponse<List<JournalDTO>>> getUserJournals() {
        String username = getCurrentUsername();
        List<JournalDTO> journals = journalService.getUserJournals(username);
        return ResponseEntity.ok(ApiResponse.success(journals, "Journals fetched successfully"));
    }

    @Operation(
            summary = "Create a new journal entry",
            description = "Saves a new journal entry for the authenticated user.")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<JournalDTO>> saveJournal(@Valid @RequestBody JournalDTO journalDTO) {
        String username = getCurrentUsername();
        JournalDTO saveJournal = journalService.saveJournal(journalDTO, username);
        return ResponseEntity.ok(ApiResponse.success(saveJournal, "Journal entry saved successfully"));
    }

    @Operation(
            summary = "Get a journal entry by ID",
            description = "Fetches a specific journal entry by its ID for the authenticated user.")
    @GetMapping("/id/{id}")
    public ResponseEntity<ApiResponse<JournalDTO>> getJournalById(@PathVariable Long id) {
        String username = getCurrentUsername();
        JournalDTO journal = journalService.getJournalById(id, username);
        return ResponseEntity.ok(ApiResponse.success(journal, "Journal fetched successfully"));
    }

    @Operation(
            summary = "Update a journal entry",
            description = "Updates an existing journal entry by its ID for the authenticated user.")
    @PutMapping("/id/{id}")
    public ResponseEntity<ApiResponse<JournalDTO>> updateJournal(@Valid @PathVariable Long id, @RequestBody JournalDTO updatedJournal) {
        String username = getCurrentUsername();
        JournalDTO journal = journalService.updateJournal(id, updatedJournal, username);
        return ResponseEntity.ok(ApiResponse.success(journal, "Journal updated successfully"));
    }

    @Operation(
            summary = "Delete a journal entry",
            description = "Deletes a specific journal entry by its ID for the authenticated user.")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<ApiResponse<String>> deleteJournal(@PathVariable Long id) {
        String username = getCurrentUsername();
        journalService.deleteJournal(id, username);
        return ResponseEntity.ok(ApiResponse.success(null, "Journal deleted successfully"));
    }

    @Operation(
            summary = "Get all journals",
            description = "Fetches all journal entries in the system.")
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}