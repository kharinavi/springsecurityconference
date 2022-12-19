package ru.kharina.study.springsecurityconference.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.kharina.study.springsecurityconference.model.Role;
import ru.kharina.study.springsecurityconference.model.User;
import ru.kharina.study.springsecurityconference.modeldb.Report;
import ru.kharina.study.springsecurityconference.modeldb.Speaker;
import ru.kharina.study.springsecurityconference.modeldb.Visitor;
import ru.kharina.study.springsecurityconference.modeldto.ReportDto;
import ru.kharina.study.springsecurityconference.repository.UserRepository;
import ru.kharina.study.springsecurityconference.repositorydb.AuditoriumRepository;
import ru.kharina.study.springsecurityconference.repositorydb.ReportRepository;
import ru.kharina.study.springsecurityconference.repositorydb.SpeakerRepository;
import ru.kharina.study.springsecurityconference.repositorydb.VisitorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final SpeakerRepository speakerRepository;
    private final AuditoriumRepository auditoriumRepository;
    private final VisitorRepository visitorRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository,
                         SpeakerRepository speakerRepository,
                         AuditoriumRepository auditoriumRepository,
                         VisitorRepository visitorRepository,
                         UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.speakerRepository = speakerRepository;
        this.auditoriumRepository = auditoriumRepository;
        this.visitorRepository = visitorRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User result = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            result = userRepository.findByEmail(currentUserName).get();
        }
        return result;
    }

    private Speaker getSpeakerBySecurityUser(Long id) {
        List<Speaker> speakers = speakerRepository.findAll().stream()
                .filter(speaker -> speaker.getSecurityId() == id)
                .collect(Collectors.toList());
        return speakers.stream().findFirst().orElse(null);
    }

    public boolean isCurrentUserIsSpeakerOrAdmin() {
        User user = getCurrentUser();
        if (user.getRole() == Role.ADMIN) return true;
        return (!getSpeakerBySecurityUser(user.getId()).equals(null));
    }

    public boolean ifCurrentUserCanEditReportOrAdmin(int id) {
        User user = getCurrentUser();
        if (user.getRole() == Role.ADMIN) return true;
        Speaker speaker = getSpeakerBySecurityUser(user.getId());
        if (speaker.equals(null)) return false;
        return (!speaker.getReportList().stream()
                .filter(rep -> rep.getId()==id)
                .collect(Collectors.toList()).isEmpty());
    }

    //Для GET всех докладов
    public List<ReportDto> getAllReportsDto() {
        return reportRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }


    //Конвертация списка сущностей слушателей в список id
    private List<Integer> convertEntityListToDto(List<Visitor> visitorList){
        List<Integer> result = new ArrayList<>();
        for (Visitor visitor : visitorList) {
            result.add(visitor.getId());
        }
        return result;
    }

    //Конвертация списка id слушателей в список сущностей
    private List<Visitor> convertDtoListToEntity(List<Integer> dtoList){
        List<Visitor> result = new ArrayList<>();
        for (Integer id : dtoList) {
            result.add(visitorRepository.getOne(id));
        }
        return result;
    }

    //Конвертация списка сущностей докладов в список id
    public List<Integer> convertReportEntityListToDto(List<Report> reportList){
        List<Integer> result = new ArrayList<>();
        for (Report report : reportList) {
            result.add(report.getId());
        }
        return result;
    }

    //Конвертация списка id докладов в список сущностей
    public List<Report> convertReportDtoListToEntity(List<Integer> dtoList){
        List<Report> result = new ArrayList<>();
        for (Integer id : dtoList) {
            result.add(reportRepository.getOne(id));
        }
        return result;
    }

    //Конвертация сущности доклада в dto
    private ReportDto convertEntityToDto(Report report){
        ReportDto reportDTO = new ReportDto();
        reportDTO.setId(report.getId());
        reportDTO.setName(report.getName());
        reportDTO.setDescription(report.getDescription());
        reportDTO.setDate(report.getDate());
        reportDTO.setDuration(report.getDuration());
        reportDTO.setSpeaker(report.getSpeaker().getId());
        reportDTO.setAuditorium(report.getAuditorium().getId());
        reportDTO.setVisitorList(convertEntityListToDto(report.getVisitorList()));
        return reportDTO;
    }

    //Конвертация dto в сущность доклада
    public Report convertDtoToEntity(ReportDto dto){
        Report report = new Report();
        report.setId(dto.getId());
        report.setName(dto.getName());
        report.setDescription(dto.getDescription());
        report.setDate(dto.getDate());
        report.setDuration(dto.getDuration());
        report.setSpeaker(speakerRepository.getOne(dto.getSpeaker()));
        report.setAuditorium(auditoriumRepository.getOne(dto.getAuditorium()));
        report.setVisitorList(convertDtoListToEntity(dto.getVisitorList()));
        return report;
    }

    private boolean haveToAdd(List<Report> list, ReportDto dto) {
        boolean result = true;
        long newBegin = dto.getDate().getTime();
        long newEnd = dto.getDate().getTime() + dto.getDuration()*60000;

        for (Report report : list) {
            long currentBegin = report.getDate().getTime();
            long currentEnd = report.getDate().getTime() + report.getDuration()*60000;

            if (((currentBegin <= newBegin) && (currentEnd >= newBegin))
                    || (newEnd >= currentBegin) && (newEnd <= currentEnd)) {
                result = result&&false;
            }
            if (!result) break;
        }
        return result;
    }


    //POST сохранение в базу данных
    public Report saveReportDto(ReportDto dto){
        List<Report> auditoriumResult = reportRepository
                .findAll().stream()
                .filter(m -> m.getAuditorium().getId() == dto.getAuditorium())
                .collect(Collectors.toList());

        List<Report> speakerResult = reportRepository
                .findAll().stream()
                .filter(m -> m.getSpeaker().getId() == dto.getSpeaker())
                .collect(Collectors.toList());

        Report report = convertDtoToEntity(dto);
        if (haveToAdd(auditoriumResult, dto) && haveToAdd(speakerResult, dto)){
            reportRepository.save(report);
        }
        return report;
    }

    //PUT Обновление в бд по id
    public ReportDto updateReport(ReportDto newReport, int id) {
        ReportDto result = getReportById(id);
        result.setId(id);
        result.setName(newReport.getName());
        result.setDescription(newReport.getDescription());
        result.setDate(newReport.getDate());
        result.setDuration(newReport.getDuration());
        result.setSpeaker(newReport.getSpeaker());
        result.setAuditorium(newReport.getAuditorium());
        result.setVisitorList(newReport.getVisitorList());
        reportRepository.save(convertDtoToEntity(result));
        return result;
    }

    //GET по id
    public ReportDto getReportById(int id) {
        return convertEntityToDto(reportRepository.getOne(id));
    }

    //DELETE по id
    public void deleteReportById(int id) {
        reportRepository.deleteById(id);
    }
}
