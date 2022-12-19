package ru.kharina.study.springsecurityconference.controllerdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kharina.study.springsecurityconference.modeldto.SpeakerDto;
import ru.kharina.study.springsecurityconference.service.SpeakerService;

import java.util.List;

@RestController
@RequestMapping("/speakers")
public class SpeakerController {

    private final SpeakerService speakerService;

    @Autowired
    public SpeakerController(SpeakerService speakerService) {
        this.speakerService = speakerService;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('permission:min')")
    public List<SpeakerDto> getAllSpeakerDto() {
        return speakerService.getAllSpeakersDto();
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('permission:admin')")
    public void createSpeaker(@RequestBody SpeakerDto newSpeaker) {
        speakerService.saveSpeakerDto(newSpeaker);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:min')")
    public SpeakerDto getSpeakerDtoById(@PathVariable int id) {
        return speakerService.getSpeakerById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:speaker')")
    public SpeakerDto updateSpeaker(@RequestBody SpeakerDto newSpeaker, @PathVariable int id) {
        SpeakerDto result = null;
        if (speakerService.isCurrentUserAuthorized(id))
            result = speakerService.updateSpeaker(newSpeaker, id);
        return result;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:admin')")
    public void deleteSpeaker(@PathVariable int id) {
        if (speakerService.isCurrentUserAuthorized(id))
            speakerService.deleteSpeakerById(id);
    }
}
