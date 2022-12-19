package ru.kharina.study.springsecurityconference.controllerdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kharina.study.springsecurityconference.modeldb.Auditorium;
import ru.kharina.study.springsecurityconference.modeldto.AuditoriumDto;
import ru.kharina.study.springsecurityconference.service.AuditoriumService;

import java.util.List;

@RestController
@RequestMapping("/auditoriums")
public class AuditoriumController {

    private final AuditoriumService auditoriumService;

    @Autowired
    public AuditoriumController(AuditoriumService auditoriumService) {
        this.auditoriumService = auditoriumService;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('permission:min')")
    public List<AuditoriumDto> getAllAuditorium() {
        return auditoriumService.getAllAuditoriumsDto();
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('permission:admin')")
    public Auditorium createAuditorium(@RequestBody AuditoriumDto newAuditorium) {
        return auditoriumService.saveAuditoriumDto(newAuditorium);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:min')")
    public AuditoriumDto getAuditoriumDtoById(@PathVariable int id) {
        return auditoriumService.getAuditoriumById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:admin')")
    public AuditoriumDto updateAuditorium(@RequestBody AuditoriumDto newAuditorium, @PathVariable int id) {
       return auditoriumService.updateAuditorium(newAuditorium, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:admin')")
    public void deleteAuditorium(@PathVariable int id) {
        auditoriumService.deleteAuditoriumById(id);
    }
}


