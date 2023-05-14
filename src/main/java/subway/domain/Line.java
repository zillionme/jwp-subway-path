package subway.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Line {

    private final String name;
    private final String color;
    private final List<Section> sections;

    public Line(String name, String color, List<Section> sections) {
        this.name = name;
        this.color = color;
        this.sections = new LinkedList<>(sections);
    }

    public void addInitialStations(Station baseStation, Station newStation, Direction directionOfBase,
            Distance newDistance) {
        validateInitialSectionToAdd(baseStation, newStation);
        if (directionOfBase == Direction.LEFT) {
            sections.add(new Section(baseStation, newStation, newDistance));
        }
        if (directionOfBase == Direction.RIGHT) {
            sections.add(new Section(newStation, baseStation, newDistance));
        }
    }

    private void validateInitialSectionToAdd(Station baseStation, Station newStation) {
        if (!sections.isEmpty()) {
            throw new IllegalArgumentException("이미 등록된 역이 있습니다.");
        }
        if (baseStation.equals(newStation)) {
            throw new IllegalArgumentException("구간은 서로 다른 두 역으로 이뤄져 있어야 합니다.");
        }
    }

    public void addStation(Station baseStation, Station newStation, Direction directionOfBase, Distance newDistance) {
        validateSectionToAdd(baseStation, newStation, directionOfBase, newDistance);
        if (directionOfBase == Direction.LEFT) {
            addNewStationToRight(baseStation, newStation, newDistance, directionOfBase);
        }
        if (directionOfBase == Direction.RIGHT) {
            addNewStationToLeft(baseStation, newStation, newDistance, directionOfBase);
        }
    }

    private void validateSectionToAdd(Station baseStation, Station newStation, Direction directionOfBase,
            Distance newDistance) {
        if (!isExistInLine(baseStation)) {
            throw new IllegalArgumentException("기준역이 존재하지 않습니다");
        }
        if (isExistInLine(newStation)) {
            throw new IllegalArgumentException("추가하려는 역이 이미 존재합니다");
        }
        if (isExistingSectionIsLongerThanNewSection(baseStation, directionOfBase, newDistance)) {
            throw new IllegalArgumentException("새로운 구간의 거리는 기존 구간의 거리보다 짧아야 합니다.");
        }
    }

    private boolean isExistInLine(Station station) {
        return sections.stream()
                .anyMatch(section -> section.hasStationInSection(station));
    }

    private boolean isExistingSectionIsLongerThanNewSection(Station baseStation, Direction directionOfBase,
            Distance newDistance) {
        Optional<Section> sectionToRevise = findSectionIncludingStationOnDirection(baseStation, directionOfBase);
        return sectionToRevise
                .map(section -> section.isLongerThan(newDistance))
                .orElse(true);
    }

    private Optional<Section> findSectionIncludingStationOnDirection(Station baseStation, Direction direction) {
        return sections.stream()
                .filter(section -> section.isStationOnDirection(baseStation, direction))
                .findFirst();
    }

    private void addNewStationToRight(Station baseStation, Station newStation, Distance newDistance,
            Direction directionOfBase) {
        Optional<Section> sectionToRevise = findSectionIncludingStationOnDirection(baseStation, directionOfBase);
        if (sectionToRevise.isPresent()) {
            Section origin = sectionToRevise.get();
            sections.remove(origin);
            sections.add(new Section(newStation, origin.getDownStation(),
                    origin.getDistance().subtract(newDistance)));
            sections.add(new Section(baseStation, newStation, newDistance));
            return;
        }
        sections.add(new Section(baseStation, newStation, newDistance));
    }

    private void addNewStationToLeft(Station baseStation, Station newStation, Distance newDistance,
            Direction directionOfBase) {
        Optional<Section> sectionToRevise = findSectionIncludingStationOnDirection(baseStation, directionOfBase);
        if (sectionToRevise.isPresent()) {
            Section origin = sectionToRevise.get();
            sections.remove(origin);
            sections.add(new Section(origin.getUpStation(), newStation, origin.getDistance().subtract(newDistance)));
            sections.add(new Section(newStation, baseStation, newDistance));
            return;
        }
        sections.add(new Section(newStation, baseStation, newDistance));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(name, line.name) || Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
