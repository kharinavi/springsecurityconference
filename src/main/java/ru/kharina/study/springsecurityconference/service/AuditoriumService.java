package ru.kharina.study.springsecurityconference.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kharina.study.springsecurityconference.modeldb.Auditorium;
import ru.kharina.study.springsecurityconference.modeldto.AuditoriumDto;
import ru.kharina.study.springsecurityconference.repositorydb.AuditoriumRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditoriumService {
    private final AuditoriumRepository auditoriumRepository;
    private final ReportService reportService;

    @Autowired
    public AuditoriumService(AuditoriumRepository auditoriumRepository,
                             ReportService reportService) {
        this.auditoriumRepository = auditoriumRepository;
        this.reportService = reportService;
    }

    //Для GET всех аудиторий
    public List<AuditoriumDto> getAllAuditoriumsDto() {
        return auditoriumRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    //Конвертация сущности аудитории в dto
    public AuditoriumDto convertEntityToDto(Auditorium auditorium){
        AuditoriumDto auditoriumDTO = new AuditoriumDto();
        auditoriumDTO.setId(auditorium.getId());
        auditoriumDTO.setNumber(auditorium.getNumber());
        auditoriumDTO.setDescription(auditorium.getDescription());
        auditoriumDTO.setReportList(reportService.convertReportEntityListToDto(auditorium.getReportList()));
        return auditoriumDTO;
    }

    //Конвертация dto в сущность аудитории
    public Auditorium convertDtoToEntity(AuditoriumDto dto){
        Auditorium auditorium = new Auditorium();
        auditorium.setId(dto.getId());
        auditorium.setNumber(dto.getNumber());
        auditorium.setDescription(dto.getDescription());
        auditorium.setReportList(reportService.convertReportDtoListToEntity(dto.getReportList()));
        return auditorium;
    }

    //POST сохранение в базу данных
    public Auditorium saveAuditoriumDto(AuditoriumDto dto){
        Auditorium auditorium = convertDtoToEntity(dto);
        auditoriumRepository.save(auditorium);
        return auditorium;
    }

    //PUT Обновление в бд по id
    public AuditoriumDto updateAuditorium(AuditoriumDto newAuditorium, int id) {
        AuditoriumDto result = getAuditoriumById(id);
        result.setId(id);
        result.setNumber(newAuditorium.getNumber());
        result.setDescription(newAuditorium.getDescription());
        result.setReportList(newAuditorium.getReportList());
        auditoriumRepository.save(convertDtoToEntity(result));
        return result;
    }

    //GET по id
    public AuditoriumDto getAuditoriumById(int id) {
        return convertEntityToDto(auditoriumRepository.getOne(id));
    }

    //DELETE по id
    public void deleteAuditoriumById(int id) {
        auditoriumRepository.deleteById(id);
    }
}
