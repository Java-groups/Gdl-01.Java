package com.softserve.mapper;

import com.softserve.dto.PollOptionDTO;
import com.softserve.model.PollOption;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PollOptionsMapper {
    PollOption sourceToDestination(PollOptionDTO source);

    @InheritInverseConfiguration
    PollOptionDTO destinationToSource(PollOption destination);
}
