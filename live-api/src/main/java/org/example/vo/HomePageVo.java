package org.example.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class HomePageVo {

    private String avatar;

    private Long userId;

    private boolean showStartLivingBtn;

    private String nickName;

    private boolean loginStatus;

}
