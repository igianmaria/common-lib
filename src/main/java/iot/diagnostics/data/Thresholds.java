package iot.diagnostics.data;

public abstract class Thresholds {
    public record Range(double min, double max) {
        public boolean contains(double value) {
            return value >= min && value <= max;
        }
    }
}
