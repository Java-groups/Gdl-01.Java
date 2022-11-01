package com.softserve.controller.survey;

import com.softserve.model.Poll;
import com.softserve.model.PollOption;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Timestamp;
import java.util.*;

@Controller
public class SurveyController {
    private final static List<Poll> pollsList;

    public SurveyController() {
        generatePolls();
    }

    static {
        pollsList = new ArrayList<Poll>();
        Poll poll1 = new Poll();
        poll1.setQuestion("Which is your favourite sport of the list");
        poll1.setClosedDate(new Timestamp(2022,10,27, 9,41,12,0));
        poll1.setPollOption(new HashSet<PollOption>());
        PollOption pollOption11 = new PollOption();
        pollOption11.setPoll(poll1);
        pollOption11.setValue("Baseball");
        PollOption pollOption12 = new PollOption();
        pollOption12.setPoll(poll1);
        pollOption12.setValue("BasketBall");
        poll1.getPollOption().add(pollOption11);
        poll1.getPollOption().add(pollOption12);


        Poll poll2 = new Poll();
        poll2.setQuestion("Who is your favourite soccer player");
        poll2.setClosedDate(new Timestamp(2022,10,27, 9,42,30,0));
        poll2.setPollOption(new HashSet<PollOption>());
        PollOption pollOption21 = new PollOption();
        pollOption21.setPoll(poll2);
        pollOption21.setValue("CR7");
        PollOption pollOption22 = new PollOption();
        pollOption22.setPoll(poll2);
        pollOption22.setValue("Messi");
        poll2.getPollOption().add(pollOption21);
        poll2.getPollOption().add(pollOption22);

        pollsList.add(poll1);
        pollsList.add(poll2);
    }

    private void generatePolls () {
//        pollsList = new ArrayList<Poll>();
        Poll poll1 = new Poll();
        poll1.setQuestion("Which is your favourite sport of the list");
        poll1.setClosedDate(new Timestamp(2022,10,27, 9,41,12,0));
        poll1.setPollOption(new HashSet<PollOption>());
        PollOption pollOption11 = new PollOption();
        pollOption11.setPoll(poll1);
        pollOption11.setValue("Baseball");
        PollOption pollOption12 = new PollOption();
        pollOption12.setPoll(poll1);
        pollOption12.setValue("BasketBall");
        poll1.getPollOption().add(pollOption11);
        poll1.getPollOption().add(pollOption12);


        Poll poll2 = new Poll();
        poll2.setQuestion("Who is your favourite soccer player");
        poll2.setClosedDate(new Timestamp(2022,10,27, 9,42,30,0));
        poll2.setPollOption(new HashSet<PollOption>());
        PollOption pollOption21 = new PollOption();
        pollOption21.setPoll(poll2);
        pollOption21.setValue("CR7");
        PollOption pollOption22 = new PollOption();
        pollOption22.setPoll(poll2);
        pollOption22.setValue("Messi");
        poll2.getPollOption().add(pollOption21);
        poll2.getPollOption().add(pollOption22);
/*
        Poll poll3 = new Poll();
        poll3.setQuestion("Best year for NFL");
        poll3.setClosedDate(new Timestamp(2022,10,27, 9,44,34,0));

        Poll poll4 = new Poll();
        poll4.setQuestion("Favourite team");
        poll4.setClosedDate(new Timestamp(2022,10,27, 9,45,23,0));

        Poll poll5 = new Poll();
        poll5.setQuestion("Do you watch your favorite sport on TV?");
        poll5.setClosedDate(new Timestamp(2022,10,27, 9,46,42,0));*/

        /*pollsList.add(poll1);
        pollsList.add(poll2);*/
        /*pollsList.add(poll3);
        pollsList.add(poll4);
        pollsList.add(poll5);*/
    }

    @GetMapping("/surveys")
    public String showSurveys(Model model){
        return "admin/surveys";
    }

    @GetMapping("/polls")
    public String showPolls(Model model) {
        model.addAttribute("pollsList", pollsList);
        return "users/polls";
    }

}
