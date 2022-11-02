package com.softserve.mapper;

import com.softserve.dto.PollDTO;
import com.softserve.model.Poll;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PollOptionsMapper.class})
public interface PollMapper {
    Poll sourceToDestination(PollDTO source);
    @InheritInverseConfiguration
    PollDTO destinationToSource(Poll destination);
}
