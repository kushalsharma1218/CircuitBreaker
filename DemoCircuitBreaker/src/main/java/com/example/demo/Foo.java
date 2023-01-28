package com.example.demo;

import java.time.Instant;

import com.example.demo.enums.State;

public class Foo implements Service {

    private static final int retries = 3;

    private final Service service;
    private State state;
    private int threshold;
    private int failureCount;
    private int successCount;
    private Instant lastFailureTime;

    public Foo(Service service, int threshold) {
        this.service = service;
        this.state = State.CLOSED;
        this.threshold = threshold;
        this.failureCount = 0;
        this.successCount = 0;
        this.lastFailureTime = null;
    }

    @Override
    public String sendRequest(String request) {
        if (state == State.CLOSED) {
            String response = callService(request);
            if(response != null){
                failureCount = 0;
            }
            return response;
        } else if (state == State.PARTIALLY_OPEN) {
            if(allowRequest()){
                return callService(request);
            }
        }
        System.out.println("Foo: "+ request + " failed due to Service is currently unavailable.");
        return null;
    }

    private boolean allowRequest() {
        return this.failureCount < threshold;
    }

    public String callService (String request){
        try {
            String response = service.sendRequest(request);
            successCount++;
            return response;
        } catch (Exception e) {
            failureCount++;
            lastFailureTime = Instant.now();
            if (failureCount >= retries) {
                setState(State.OPEN);
                System.out.println("Foo: Service has been marked as unavailable.");
                System.out.println("Foo: "+ request + " failed due to Service is currently unavailable.");
            }
            else{
                System.out.println(e.getMessage());
            }
            return null;
        }
    }

    public void setState(State state){
        this.state = state;
    }

    public State getState(){
        return state;
    }

    @Override
    public void receiveUpdate(){
        this.setState(State.PARTIALLY_OPEN);
        System.out.println("Foo: service state update to partially open");
        failureCount = 0;
    }

}