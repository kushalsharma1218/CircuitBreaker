package com.example.demo;

import java.util.List;
import java.util.ArrayList;

public class DummyService implements Service {
    private boolean isHealthy;
    private List<Service> dependentServices;

    public DummyService(boolean isHealthy) {
        this.isHealthy = isHealthy;
        this.dependentServices = new ArrayList<>();
    }

    @Override
    public String sendRequest(String request) {
        if (isHealthy) {
            return "Service: request served successfully for " +request;
        } else {
            throw new RuntimeException("Service: "+ request +" failed due to Service Health");
        }
    }
    
    public void registerService(Service service){
        dependentServices.add(service);
    }

    public boolean getState(){
        return isHealthy;
    }

    public void setState(boolean isHealthy){
        this.isHealthy = isHealthy;
        notifyCusumer();
    }

    private void notifyCusumer() {
        if(getState()){
            for (Service service : dependentServices) {
                service.receiveUpdate();
            }
        }
    }

    @Override
    public void receiveUpdate() {
        // TODO Auto-generated method stub
        
    }

    
}
