package com.softserve.service;

import com.softserve.dto.PollDTO;
import com.softserve.dto.PollOptionDTO;
import com.softserve.mapper.PollMapper;
import com.softserve.mapper.PollOptionsMapper;
import com.softserve.model.Poll;
import com.softserve.model.PollOption;
import com.softserve.repository.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PollService {
    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollMapper pollMapper;

    @Autowired
    private PollOptionsMapper pollOptionsMapper;

    public void save(PollDTO pollDTO) {
        Poll poll = pollMapper.toPoll(pollDTO);

        for (PollOptionDTO pollOptionDTO : pollDTO.getPollOptions()) {
            PollOption pollOption = pollOptionsMapper.toPollOption(pollOptionDTO);
            pollOption.setPoll(poll);
            poll.getPollOptions().add(pollOption);
        }

        pollRepository.save(poll);
    }
}
