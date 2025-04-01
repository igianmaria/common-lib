package iot.diagnostics.data;

public abstract class Thresholds {
    public record Range(double min, double max) {}
}
