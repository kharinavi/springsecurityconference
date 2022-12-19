package ru.kharina.study.springsecurityconference.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.kharina.study.springsecurityconference.model.User;
import ru.kharina.study.springsecurityconference.modeldb.Visitor;
import ru.kharina.study.springsecurityconference.modeldto.VisitorDto;
import ru.kharina.study.springsecurityconference.repository.UserRepository;
import ru.kharina.study.springsecurityconference.repositorydb.VisitorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitorService {
    private final VisitorRepository visitorRepository;
    private final ReportService reportService;
    private final UserRepository userRepository;


    public VisitorService(VisitorRepository visitorRepository,
                          ReportService reportService, UserRepository userRepository) {
        this.visitorRepository = visitorRepository;
        this.reportService= reportService;
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
        return (securityUserId == visitorRepository.getOne(id).getSecurityId());
    }

    public List<VisitorDto> getAllVisitorsDto() {
        return visitorRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    //Конвертация сущности слушателя в dto
    public VisitorDto convertEntityToDto(Visitor visitor){
        VisitorDto visitorDTO = new VisitorDto();
        visitorDTO.setId(visitor.getId());
        visitorDTO.setName(visitor.getName());
        visitorDTO.setPhone(visitor.getPhone());
        visitorDTO.setEmail(visitor.getEmail());
        visitorDTO.setReportList(reportService.convertReportEntityListToDto(visitor.getReportList()));
        return visitorDTO;
    }

    //Конвертация dto в сущность слушателя
    private Visitor convertDtoToEntity(VisitorDto dto){
        Visitor visitor = new Visitor();
        visitor.setId(dto.getId());
        visitor.setName(dto.getName());
        visitor.setPhone(dto.getPhone());
        visitor.setEmail(dto.getEmail());
        visitor.setReportList(reportService.convertReportDtoListToEntity(dto.getReportList()));
        return visitor;
    }

    //POST сохранение в базу данных
    public void saveVisitorDto(VisitorDto dto){
        Visitor visitor = convertDtoToEntity(dto);
        visitorRepository.save(visitor);
    }

    //PUT Обновление в бд по id
    public VisitorDto updateVisitor(VisitorDto newVisitor, int id) {
        VisitorDto result = getVisitorById(id);
        result.setId(id);
        result.setName(newVisitor.getName());
        result.setPhone(newVisitor.getPhone());
        result.setEmail(newVisitor.getEmail());
        result.setReportList(newVisitor.getReportList());
        visitorRepository.save(convertDtoToEntity(result));
        return result;
    }

    //GET по id
    public VisitorDto getVisitorById(int id) {
        return convertEntityToDto(visitorRepository.getOne(id));
    }

    //DELETE по id
    public void deleteVisitorById(int id) {
        visitorRepository.deleteById(id);
    }
}
