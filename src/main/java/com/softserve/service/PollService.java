package com.softserve.service;

import com.softserve.dto.PollDTO;
import com.softserve.model.Poll;
import com.softserve.model.PollAnswer;
import com.softserve.repository.PollAnswerRepository;
import com.softserve.repository.PollOptionRepository;
import com.softserve.repository.PollRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PollService {
    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollOptionService pollOptionService;

    @Autowired
    private PollOptionRepository pollOptionRepository;

    @Autowired
    private PollAnswerRepository pollAnswerRepository;

    ModelMapper mapper = new ModelMapper();

    public void save(PollDTO pollDTO) {
        Poll poll = mapper.map(pollDTO, Poll.class);
        Poll pollAfterSave = pollRepository.save(poll);
        pollDTO.getPollOptions().forEach( element -> element.setPoll(pollAfterSave));
        pollOptionService.save(pollDTO.getPollOptions());
    }

    public PollDTO getPoll(Integer id, String roleName) {
        PollDTO pollDTOResponse = null;
        Poll poll = pollRepository.findById(id).orElse(new Poll());

        if (roleName == null || roleName.isEmpty()) {
            return null;
        }

        if(roleName.equals("ROLE_ADMIN")) {
            pollDTOResponse = mapper.map(poll, PollDTO.class);
        }
        else if(roleName.equals("ROLE_USER")) {
            if(poll.getStatus() != null && poll.getStatus() < 3) {
                poll.setUserId(null);
                poll.setCreationDate(null);
                poll.setModificationDate(null);

                pollDTOResponse = mapper.map(poll, PollDTO.class);
            }
        }
        return pollDTOResponse;
    }

    public List<PollDTO> getAllPolls(String roleName) {
        List<Poll> pollList = pollRepository.findAll();

        if (roleName == null || roleName.isEmpty()) {
            return null;
        }

        if(roleName.equals("ROLE_ADMIN")) {
            return pollList.stream()
                    .filter(poll -> poll.getStatus() != null)
                    .filter(poll -> poll.getStatus() < 3)
                    .map(poll -> mapper.map(poll, PollDTO.class))
                    .collect(Collectors.toList());
        }
        else if(roleName.equals("ROLE_USER")) {
            return pollList.stream()
                    .filter(poll -> poll.getStatus() != null)
                    .filter(poll -> poll.getStatus() < 3)
                    .map(poll -> {
                            poll.setUserId(null);
                            poll.setStatus(null);
                            poll.setCreationDate(null);
                            poll.setModificationDate(null);
                        return mapper.map(poll, PollDTO.class);
                    })
                    .collect(Collectors.toList());
        } else{
            return null;
        }
    }

    public PollAnswer savePollAnswer(PollAnswer pollAnswer) {
        return pollAnswerRepository.save(pollAnswer);
    }
}
