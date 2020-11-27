package ru.mos.eirc.log.searcher.controller;

import com.example.logsearch.entities.FileExtension;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import com.example.logsearch.entities.SignificantDateInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.mos.eirc.log.searcher.service.LogSearchService;
import ru.mos.eirc.log.searcher.util.ApplicationProperties;

import java.util.Arrays;
import java.util.List;

@Controller
public class LogSearchController {

    private final LogSearchService logSearchService;

    @Autowired
    public LogSearchController(LogSearchService logSearchService) {
	this.logSearchService = logSearchService;
    }

    @ModelAttribute("allExtensions")
    public List<FileExtension> populateTypes() {
	return Arrays.asList(FileExtension.values());
    }

    @GetMapping(value = "/")
    public String logSearch(Model model) {
	SearchInfo searchInfo = new SearchInfo();
	searchInfo.getDateIntervals().add(new SignificantDateInterval());
	model.addAttribute(searchInfo);
	String path = ApplicationProperties.getDomainPath().toString();
	model.addAttribute("path", path);
	return "index";
    }

    @RequestMapping(value = "/logSearch", params = { "addRow" })
    public String addRow(final SearchInfo searchInfo) {
	searchInfo.getDateIntervals().add(new SignificantDateInterval());
	return "index";
    }

    @RequestMapping(value = "/logSearch", params = { "removeRow" })
    public String removeRow(final SearchInfo searchInfo, @RequestParam("removeRow") int removeRow) {
	if (searchInfo.getDateIntervals().size() > 1) {
	    searchInfo.getDateIntervals().remove(removeRow);
	}
	return "index";
    }

    @PostMapping(value = "/logSearch")
    public ModelAndView logSearch(final SearchInfo searchInfo) {
	ModelAndView modelAndView = new ModelAndView("result");
	SearchInfoResult searchInfoResult = logSearchService.logSearch(searchInfo);
	modelAndView.addObject("searchInfoResult", searchInfoResult);
	return modelAndView;
    }
}
