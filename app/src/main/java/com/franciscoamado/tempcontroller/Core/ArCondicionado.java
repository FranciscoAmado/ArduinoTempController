package com.franciscoamado.tempcontroller.Core;


/**
 * Created by Pedro on 17/08/2015.
 */
public class ArCondicionado {

    private String nome;
    private String estado;   // "Ligado" ou "Desligado"
    private String accao;    // "Arrefecer" ou "Aquecer"
    private int velocidadeVentoinha; // Valores 0, 1 ou 2

    public ArCondicionado() {
        setNome("");
        setEstado("");
        setAccao("");
        setVelocidadeVentoinha(0);
    }
    public ArCondicionado(String nome, String estado, String accao) {
        setNome(nome);
        setEstado(estado);
        setAccao(accao);
        setVelocidadeVentoinha(0);
    }

    public ArCondicionado(ArCondicionado obj){
        setNome(obj.getNome());
        setEstado(obj.getEstado());
        setAccao(obj.getAccao());
        setVelocidadeVentoinha(obj.getVelocidadeVentoinha());
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setAccao(String accao) {
        this.accao = accao;
    }

    public void setVelocidadeVentoinha(int velocidadeVentoinha) {this.velocidadeVentoinha = velocidadeVentoinha;}

    public int getVelocidadeVentoinha() {return velocidadeVentoinha;}

    public String getAccao() {return accao;}

    public String getEstado() {return estado;}

    public String getNome() {return nome;}
}
