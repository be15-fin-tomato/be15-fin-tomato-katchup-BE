package be15fintomatokatchupbe.common.domain;

public enum GenderType {
    M("남성"),
    F("여성");

    private final String label;

    // 생성자
    GenderType(String label) {
        this.label = label;
    }

    // Getter
    public String getLabel() {
        return label;
    }
}


