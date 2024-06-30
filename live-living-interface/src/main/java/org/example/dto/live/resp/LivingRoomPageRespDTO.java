package org.example.dto.live.resp;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Setter
public class LivingRoomPageRespDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8513787306312054357L;
    private List<LivingRoomRespDTO> livingRoomRespVoList;

    private boolean hasNext;


}
