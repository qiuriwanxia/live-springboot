package org.example.dto.live.req;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Data
@Getter
@Setter
public class LivingRoomReqDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3798016843386010685L;
    private Integer type;

    private int page;

    private int pageSize;


}
