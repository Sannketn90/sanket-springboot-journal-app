package com.springboot.journalapp.Mapper;

import com.springboot.journalapp.Dto.JournalDTO;
import com.springboot.journalapp.Entity.Journal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JournalMapper {

    @Mapping(source = "user.id", target = "userId")
    JournalDTO toDto(Journal journal);

    @Mapping(source = "userId", target = "user.id")
    Journal toEntity(JournalDTO journalDTO);
}
