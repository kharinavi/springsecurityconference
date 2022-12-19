package ru.kharina.study.springsecurityconference.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.kharina.study.springsecurityconference.model.User;
import ru.kharina.study.springsecurityconference.modeldb.Speaker;
import ru.kharina.study.springsecurityconference.modeldto.SpeakerDto;
import ru.kharina.study.springsecurityconference.repository.UserRepository;
import ru.kharina.study.springsecurityconference.repositorydb.SpeakerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpeakerService {
    private final SpeakerRepository speakerRepository;
    private final ReportService reportService;
    private final UserRepository userRepository;

    @Autowired
    public SpeakerService(SpeakerRepository speakerRepository,
                          ReportService reportService,
                          UserRepository userRepository) {
        this.speakerRepository = speakerRepository;
        this.reportService = reportService;
        this.userRepository = userRepository;
    }

    public boolean isCurrentUserAuthorized(int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int securityUserId = 0;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            User currentUser = userRepository.findByEmail(currentUserName).get();
            securityUserId = currentUser.getId().intValue();
        }
        return (securityUserId == speakerRepository.getOne(id).getSecurityId());
    }

    public List<SpeakerDto> getAllSpeakersDto() {
        return speakerRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    //Конвертация сущности доклада в dto
    public SpeakerDto convertEntityToDto(Speaker speaker){
        SpeakerDto speakerDTO = new SpeakerDto();
        speakerDTO.setId(speaker.getId());
        speakerDTO.setAge(speaker.getAge());
        speakerDTO.setDescription(speaker.getDescription());
        speakerDTO.setName(speaker.getName());
        speakerDTO.setPhone(speaker.getPhone());
        speakerDTO.setEmail(speaker.getEmail());
        speakerDTO.setPhoto(speaker.getPhoto());
        speakerDTO.setReportList(reportService.convertReportEntityListToDto(speaker.getReportList()));
        return speakerDTO;
    }

    //Конвертация dto в сущность докладчика
    public Speaker convertDtoToEntity(SpeakerDto dto){
        Speaker speaker = new Speaker();
        speaker.setId(dto.getId());
        speaker.setAge(dto.getAge());
        speaker.setDescription(dto.getDescription());
        speaker.setName(dto.getName());
        speaker.setPhone(dto.getPhone());
        speaker.setEmail(dto.getEmail());
        speaker.setPhoto(dto.getPhoto());
        speaker.setReportList(reportService.convertReportDtoListToEntity(dto.getReportList()));
        return speaker;
    }

    //POST сохранение в базу данных
    public void saveSpeakerDto(SpeakerDto dto){
        Speaker speaker = convertDtoToEntity(dto);
        speakerRepository.save(speaker);
    }

    //PUT Обновление в бд по id
    public SpeakerDto updateSpeaker(SpeakerDto newSpeaker, int id) {
        SpeakerDto result = getSpeakerById(id);
        result.setId(id);
        result.setName(newSpeaker.getName());
        result.setAge(newSpeaker.getAge());
        result.setDescription(newSpeaker.getDescription());
        result.setPhone(newSpeaker.getPhone());
        result.setEmail(newSpeaker.getEmail());
        result.setPhoto(newSpeaker.getPhoto());
        result.setReportList(newSpeaker.getReportList());
        speakerRepository.save(convertDtoToEntity(result));
        return result;
    }

    //GET по id
    public SpeakerDto getSpeakerById(int id) {
        return convertEntityToDto(speakerRepository.getOne(id));
    }

    //DELETE по id
    public void deleteSpeakerById(int id) {
        speakerRepository.deleteById(id);
    }
}
