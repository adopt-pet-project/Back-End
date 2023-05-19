package com.adoptpet.server.user.service;

import com.adoptpet.server.adopt.dto.response.MyAdoptResponse;
import com.adoptpet.server.adopt.service.AdoptQueryService;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MyAdoptService {

    private final AdoptQueryService adoptQueryService;

    public List<MyAdoptResponse> myAdoptList(String status, SecurityUserDto userDto) {
        if (status.equals("interested")) {
            List<Integer> keys = adoptQueryService.myInterestedAdoptKeys(userDto);
            return adoptQueryService.myInterestedAdoptList(keys);
        }

        return adoptQueryService.myAdoptList(status, userDto);
    }
}
