package be15fintomatokatchupbe.common.domain;

import lombok.Getter;

@Getter
public enum GenderType {
    M("남성"),
    F("여성"),
    O("기타");

    private final String label;

    GenderType(String label) {
        this.label = label;
    }

}


