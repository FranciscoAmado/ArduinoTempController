package com.franciscoamado.tempcontroller.Core;

/**
 * Created by Pedro on 17/08/2015.
 */
public class Sensor {
    private String nome;
    private String localizacao;  // "Interior" ou "Exterior"
    private double temperatura;
    private double temperaturaDesejada;
    private double humidade;

    public Sensor () {
        setNome("John");
        setLocalizacao("NO");
        setHumidade(0);
        setTemperatura(0);
        setTemperaturaDesejada(0);
    }
    public Sensor(String nome, String localizacao, double temperatura, double humidade){
        setNome(nome);
        setLocalizacao(localizacao);
        setTemperatura(temperatura);
        setTemperaturaDesejada(temperatura);
        setHumidade(humidade);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public void setHumidade(double humidade) {
        this.humidade = humidade;
    }

    public void setTemperaturaDesejada(double temperaturaDesejada) {
        this.temperaturaDesejada = temperaturaDesejada;
    }

    public double getHumidade() { return humidade; }

    public double getTemperatura() { return temperatura; }

    public double getTemperaturaDesejada() { return temperaturaDesejada; }

    public String getLocalizacao() { return localizacao; }

    public String getNome() { return nome; }
}
