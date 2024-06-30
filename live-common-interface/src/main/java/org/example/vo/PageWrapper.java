package org.example.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class PageWrapper<T> {

    private List<T> list;

    private boolean hasNext;

}
