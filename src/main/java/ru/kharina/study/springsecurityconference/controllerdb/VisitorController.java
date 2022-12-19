package ru.kharina.study.springsecurityconference.controllerdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kharina.study.springsecurityconference.modeldto.VisitorDto;
import ru.kharina.study.springsecurityconference.service.VisitorService;

import java.util.List;

@RestController
@RequestMapping("/visitors")
public class VisitorController {

    private final VisitorService visitorService;

    @Autowired
    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('permission:min')")
    public List<VisitorDto> getAllVisitor() {
        return visitorService.getAllVisitorsDto();
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('permission:admin')")
    public void createVisitor(@RequestBody VisitorDto newVisitor) {
        visitorService.saveVisitorDto(newVisitor);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:visitor')")
    public VisitorDto getVisitorDtoById(@PathVariable int id) {
        VisitorDto result = null;
        if (visitorService.isCurrentUserAuthorized(id))
            result = visitorService.getVisitorById(id);
        return result;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:visitor')")
    public VisitorDto updateVisitor(@RequestBody VisitorDto newVisitor, @PathVariable int id) {
        VisitorDto result = null;
        if (visitorService.isCurrentUserAuthorized(id))
            result = visitorService.updateVisitor(newVisitor, id);
        return result;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:admin')")
    public void deleteVisitor(@PathVariable int id) {
        if (visitorService.isCurrentUserAuthorized(id))
            visitorService.deleteVisitorById(id);
    }
}
