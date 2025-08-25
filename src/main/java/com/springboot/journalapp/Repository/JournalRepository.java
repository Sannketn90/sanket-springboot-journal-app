package com.springboot.journalapp.Repository;

import com.springboot.journalapp.Entity.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
   List<Journal> findByUserId(Long userId);
}
