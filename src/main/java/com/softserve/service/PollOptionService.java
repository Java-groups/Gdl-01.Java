package com.softserve.service;

import com.softserve.dto.PollOptionDTO;
import com.softserve.model.PollOption;
import com.softserve.repository.PollOptionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollOptionService {
    @Autowired
    private PollOptionRepository pollOptionRepository;

    private ModelMapper mapper = new ModelMapper();

    public void save(List<PollOptionDTO> pollOptionDTO) {
        pollOptionDTO.forEach(element -> pollOptionRepository.save(mapper.map(element, PollOption.class)));
    }
}
