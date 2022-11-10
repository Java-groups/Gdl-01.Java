package com.softserve.controller.survey;

import com.softserve.dto.PollDTO;
import com.softserve.model.PollAnswer;
import com.softserve.service.PollService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/poll")
public class SurveyRestController {

    @Autowired
    PollService pollService;

    @PostMapping(value = "/create", consumes = "application/json")
    public void createSurvey(@RequestBody PollDTO pollDTO){
        pollService.save(pollDTO);
    }

    @ApiOperation(
            value = "Update the poll from the respective id",
            response = PollDTO.class,
            notes = "Respective Poll must exist"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = PollDTO.class, responseContainer = "List"),
            @ApiResponse(code = 204, message = "No records found in Poll list"),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)})
    @PutMapping(value = "/update", consumes = "application/json")
    public void updateSurvey(@RequestBody PollDTO pollDTO){
        pollService.save(pollDTO);
    }

    @ApiOperation(
            value = "Get the list of all polls depending on the rol type",
            response = PollDTO.class, responseContainer = "List",
            notes = "List must have polls"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = PollDTO.class, responseContainer = "List"),
            @ApiResponse(code = 204, message = "No records found in Poll list"),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)})
    @GetMapping()
    public ResponseEntity<?> getPolls (@RequestParam("roleName") String roleName) {
        try {
            List<PollDTO> pollDTOList = pollService.getAllPolls(roleName);

            if (pollDTOList == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(pollDTOList);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @ApiOperation(
            value = "Get content of poll depending on the rol type",
            response = PollDTO.class,
            notes = "Poll must have content"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = PollDTO.class),
            @ApiResponse(code = 204, message = "No records found in Poll list"),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)})
    @GetMapping("/{id}")
    public ResponseEntity<?> getPollById (@PathVariable Integer id, @RequestParam("roleName") String roleName) {
        try {
            PollDTO pollDTOId = pollService.getPoll(id, roleName);

            if (pollDTOId == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(pollDTOId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @ApiOperation(
            value = "Create a answer from user to a respective poll",
            response = PollDTO.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = PollAnswer.class),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)})
    @PostMapping("/answer")
    public ResponseEntity<?> savePollAnswer(@RequestBody PollAnswer pollAnswer) {
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(pollService.savePollAnswer(pollAnswer));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
