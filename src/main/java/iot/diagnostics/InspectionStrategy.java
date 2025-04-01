package iot.diagnostics;

import java.util.List;

public interface InspectionStrategy<T> {
    InspectionResult inspect(List<T> batch);
}