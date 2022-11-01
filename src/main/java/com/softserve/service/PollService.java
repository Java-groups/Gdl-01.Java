package com.softserve.service;

import com.softserve.dto.PollDTO;
import com.softserve.mapper.PollMapper;
import com.softserve.model.Poll;
import com.softserve.repository.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollService {
    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollMapper pollMapper;

    public void save(PollDTO pollDTO) {
        pollRepository.save(pollMapper.sourceToDestination(pollDTO));
    }

    public List<Poll> ListALL() {
        return pollRepository.findAll();
    }
}
