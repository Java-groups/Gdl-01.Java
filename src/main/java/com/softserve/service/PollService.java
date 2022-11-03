package com.softserve.service;

import com.softserve.dto.PollDTO;
import com.softserve.dto.PollOptionDTO;
import com.softserve.mapper.PollMapper;
import com.softserve.mapper.PollOptionsMapper;
import com.softserve.model.Poll;
import com.softserve.model.PollOption;
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
    private PollOptionRepository pollOptionRepository;

    @Autowired
    private PollMapper pollMapper;

    @Autowired
    private PollOptionsMapper pollOptionsMapper;

    //@Autowired
    //private PollOption pollOption;

    ModelMapper mapper = new ModelMapper();

    public void save(PollDTO pollDTO) {
        Poll poll = pollMapper.toPoll(pollDTO);

        for (PollOptionDTO pollOptionDTO : pollDTO.getPollOptions()) {
            PollOption pollOption = pollOptionsMapper.toPollOption(pollOptionDTO);
            pollOption.setPoll(poll);
            poll.getPollOptions().add(pollOption);
        }

        pollRepository.save(poll);
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
