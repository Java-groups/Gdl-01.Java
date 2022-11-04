package com.softserve.service;

import com.softserve.dto.PollDTO;
import com.softserve.model.Poll;
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

    ModelMapper mapper = new ModelMapper();

    public void save(PollDTO pollDTO) {
        Poll poll = mapper.map(pollDTO, Poll.class);
        pollDTO.getPollOptions().forEach( element -> element.setPoll(poll));

        pollRepository.save(poll);
        pollOptionService.save(pollDTO.getPollOptions());
    }

    public PollDTO getPoll(Integer id) {
        Poll poll = pollRepository.findById(id).orElse(null);
        return mapper.map(poll, PollDTO.class);
    }

    public List<PollDTO> getAllPolls() {
        List<Poll> pollList = pollRepository.findAll();

        return pollList.stream()
                .filter(poll -> poll.getStatus() != null)
                .filter(poll -> poll.getStatus() < 3)
                .map(poll -> mapper.map(poll, PollDTO.class))
                .collect(Collectors.toList());
    }
}
