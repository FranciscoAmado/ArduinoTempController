package com.franciscoamado.tempcontroller.Core;

import java.util.ArrayList;

/**
 * Created by Pedro on 21/08/2015.
 */
public class Wrapper {

    private ArrayList sensores;
    private ArCondicionado arCondicionado;

    public Wrapper(){
        setArCondicionado(new ArCondicionado());
        setSensores(new ArrayList(2));
    }

    public Wrapper(Wrapper obj){
        setArCondicionado(obj.getArCondicionado());
    }

    public void setArCondicionado(ArCondicionado arCondicionado) {this.arCondicionado = arCondicionado;}

    public void setSensores(ArrayList sensores) {this.sensores = sensores;}

    public ArCondicionado getArCondicionado() {return arCondicionado;}

    public ArrayList getSensores() {return sensores;}

    public void addSensor(Sensor sensor){
        if(this.sensores != null){
            this.sensores.add(sensor);
        }
    }

    public boolean isEmpty(){
        boolean empty = this.sensores.isEmpty();
        return empty;
    }
}
