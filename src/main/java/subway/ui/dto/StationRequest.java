package subway.ui.dto;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public class StationRequest {

    @NotBlank
    @Length(min = 1, max = 10, message = "노선의 이름은 {min}자 ~ {max}자여야 합니다")
    private final String lineName;
    @NotBlank
    @Length(min = 1, max = 10, message = "역의 이름은 {min}자 ~ {max}자여야 합니다")
    private final String baseStationName;

    @NotBlank
    @Length(min = 1, max = 10, message = "역의 이름은 {min}자 ~ {max}자여야 합니다")
    private final String newStationName;

    @NotBlank
    private final String directionOfBaseStation;

    @NotBlank
    @Range(min = 1, max = 100000, message = "역 사이의 거리는 양의 정수여야 합니다")
    private final Integer distance;

    public StationRequest(String lineName, String baseStationName, String newStationName, String directionOfBaseStation,
            Integer distance) {
        this.lineName = lineName;
        this.baseStationName = baseStationName;
        this.newStationName = newStationName;
        this.directionOfBaseStation = directionOfBaseStation;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public String getBaseStationName() {
        return baseStationName;
    }

    public String getNewStationName() {
        return newStationName;
    }

    public String getDirectionOfBaseStation() {
        return directionOfBaseStation;
    }

    public Integer getDistance() {
        return distance;
    }

}
