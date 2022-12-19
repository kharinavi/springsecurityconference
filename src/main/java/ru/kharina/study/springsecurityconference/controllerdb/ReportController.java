package ru.kharina.study.springsecurityconference.controllerdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kharina.study.springsecurityconference.modeldto.ReportDto;
import ru.kharina.study.springsecurityconference.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('permission:min')")
    public List<ReportDto> getAllReports() {
        return reportService.getAllReportsDto();
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('permission:speaker')")
    public void createReport(@RequestBody ReportDto newReport) {
        if (reportService.isCurrentUserIsSpeakerOrAdmin()) {
            reportService.saveReportDto(newReport);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:min')")
    public ReportDto getReportDtoById( @PathVariable int id) {
        return reportService.getReportById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:speaker')")
    public ReportDto updateReport(@RequestBody ReportDto newReport, @PathVariable int id) {
        ReportDto result = null;
        if (reportService.ifCurrentUserCanEditReportOrAdmin(id))
             result = reportService.updateReport(newReport, id);
        return result;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:speaker')")
    public void deleteReport(@PathVariable int id) {
        if (!reportService.ifCurrentUserCanEditReportOrAdmin(id)) return;
        reportService.deleteReportById(id);
    }
}
