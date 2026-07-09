package br.com.jrssoftware.observastack.dto;

public record DependencyStatus(
        String name,
        String status,
        String details
) {
}
