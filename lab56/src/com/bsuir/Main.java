package com.bsuir;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        QueueingSystem queueingSystem = new QueueingSystem(0.5, 0.9, 0.5, 999999);
        List<QueueingSystem.Request> requests = queueingSystem.run();
        double denied1 = queueingSystem.getDeniedRequests1();
        double denied2 = queueingSystem.getDeniedRequests2();

        System.out.println("P отказа 1 = " + denied1 / requests.size());
        System.out.println("P отказа 2 = " + denied2 / requests.size());
        System.out.println("Q 1 = " + (1 - denied1 / requests.size()));
        System.out.println("Q 2 = " + (1 - denied2 / requests.size()));
    }
}
