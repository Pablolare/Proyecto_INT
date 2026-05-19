package com.lrvoley.api.dto;

public class EstadisticasDTO {
    private int totalEntrenos;
    private int totalAciertos;
    private int totalFallos;
    private double promedioAciertos;
    private double promedioFallos;

    public EstadisticasDTO(int totalEntrenos, int totalAciertos, int totalFallos,
                           double promedioAciertos, double promedioFallos) {
        this.totalEntrenos = totalEntrenos;
        this.totalAciertos = totalAciertos;
        this.totalFallos = totalFallos;
        this.promedioAciertos = promedioAciertos;
        this.promedioFallos = promedioFallos;
    }

    public int getTotalEntrenos() { return totalEntrenos; }
    public int getTotalAciertos() { return totalAciertos; }
    public int getTotalFallos() { return totalFallos; }
    public double getPromedioAciertos() { return promedioAciertos; }
    public double getPromedioFallos() { return promedioFallos; }

    public double getPorcentajeAciertos() {
        int total = totalAciertos + totalFallos;
        return total > 0 ? (totalAciertos * 100.0 / total) : 0;
    }
}
