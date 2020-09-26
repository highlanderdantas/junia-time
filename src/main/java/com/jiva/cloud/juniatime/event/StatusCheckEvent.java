package com.jiva.cloud.juniatime.event;

import com.jiva.cloud.juniatime.enums.StatusEnum;
import com.jiva.cloud.juniatime.model.Check;
import lombok.AllArgsConstructor;
import lombok.Data;

public @Data @AllArgsConstructor class StatusCheckEvent {
    
    private Check check;
    private StatusEnum status;
}
