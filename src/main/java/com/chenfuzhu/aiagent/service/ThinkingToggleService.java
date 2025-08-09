package com.chenfuzhu.aiagent.service;

import com.chenfuzhu.aiagent.dto.ThinkingToggleDTO;
import org.springframework.stereotype.Service;

@Service
public class ThinkingToggleService {

    private final ThinkingToggleDTO thinkingToggleDTO;

    public ThinkingToggleService(ThinkingToggleDTO thinkingToggleDTO) {
        this.thinkingToggleDTO = thinkingToggleDTO;
    }

    public void updateStatus(boolean isEnabled) {

        thinkingToggleDTO.setToggle(isEnabled);
    }

}
