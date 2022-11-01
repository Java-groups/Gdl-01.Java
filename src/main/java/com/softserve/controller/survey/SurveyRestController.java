package com.softserve.controller.survey;

import com.softserve.dto.PollDTO;
import com.softserve.dto.SurveyDTO;
import com.softserve.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/poll")
public class SurveyRestController {

    @Autowired
    PollService pollService;

    @PostMapping(value = "api/surveys", consumes = "application/json")
    public void createSurvey(@RequestBody SurveyDTO surveyDTO){
        pollService.save(surveyDTO.getPoll());
    }

    @PutMapping(value = "api/surveys", consumes = "application/json")
    public void updateSurvey(@RequestBody SurveyDTO surveyDTO){
        pollService.save(surveyDTO.getPoll());
    }

    @GetMapping()
    public List<PollDTO> getpolls () {
        return pollService.getAllPolls();
    }
    @GetMapping("/{id}")
    public PollDTO getPollById (@PathVariable Integer id) {
        return pollService.getPoll(id);
    }
}
