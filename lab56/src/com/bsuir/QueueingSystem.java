package com.bsuir;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static java.lang.Math.log;

public class QueueingSystem {
    private final Random randRequest;
    private final Random randTime;

    private final double p;
    private final double l;
    private final double m;
    private final int simulationTime;
    private final double averageTimeBetweenRequests;
    private final double averageProcessingTime;
    private int deniedRequests1 = 0;
    private int deniedRequests2 = 0;
    private int totalRequests1 = 0;
    private int totalRequests2 = 0;
    private int proceedRequests1 = 0;
    private int proceedRequests2 = 0;

    private static final int FIRST_PRIORITY_REQUEST = 1;
    private static final int SECOND_PRIORITY_REQUEST = 2;
    private static final int NO_REQUEST = 0;

    public QueueingSystem(double m, double l, double p, int simulationTime) {
        this.p = p;
        this.m = m;
        this.l = l;
        this.simulationTime = simulationTime;
        this.averageTimeBetweenRequests = 1 / l;
        this.averageProcessingTime = 1 / m;
        this.randRequest = new Random(100000);
        this.randTime = new Random(100000);
    }

    public int getDeniedRequests1() {
        return deniedRequests1;
    }

    public int getDeniedRequests2() {
        return deniedRequests2;
    }

    private int getAppearedRequestPriority() {
        return randRequest.nextDouble() < p
                ? FIRST_PRIORITY_REQUEST
                : SECOND_PRIORITY_REQUEST;
    }

    private double getTimeBetweenRequests(double averageTimeBetweenRequests) {
        return -averageTimeBetweenRequests * log(randRequest.nextDouble());
    }

    private double getProcessingTime(double averageProcessingTime) {
        return -averageProcessingTime * log(randTime.nextDouble());
    }

    public List<Request> run() {
        double currentTime = 0;
        List<Request> requests = new ArrayList<>();
        Channel channel1 = new Channel();
        Channel channel2 = new Channel();
        Queue queue = new Queue();
        while (currentTime < simulationTime) {
            double timePassed = getTimeBetweenRequests(averageTimeBetweenRequests);
            currentTime += timePassed;
            if (currentTime > simulationTime) {
                break;
            }
            Request request = new Request(
                    getAppearedRequestPriority(),
                    currentTime,
                    getProcessingTime(averageProcessingTime));
            requests.add(request);

            double timeLeft1 = 0;
            double timeLeft2 = 0;
            if (channel1.isBusy()) {
                channel1.getRequest().updateProcessingTime(timePassed);
                if (channel1.getRequest().isCompleted()) {
                    timeLeft1 = channel1.getRequest().getOverTime();
                    channel1.stop();
                }
            }
            if (channel2.isBusy()) {
                channel2.getRequest().updateProcessingTime(timePassed);
                if (channel2.getRequest().isCompleted()) {
                    timeLeft2 = channel2.getRequest().getOverTime();
                    channel2.stop();
                }
            }
            if (queue.hasRequest()) {
                if (channel1.isFree()) {
                    channel1.setRequest(queue.getRequest());
                    channel1.getRequest().updateProcessingTime(timeLeft1);
                    if (channel1.getRequest().isCompleted()) {
                        channel1.stop();
                    }
                    queue.clear();
                } else if (channel2.isFree()) {
                    channel2.setRequest(queue.getRequest());
                    channel2.getRequest().updateProcessingTime(timeLeft2);
                    if (channel2.getRequest().isCompleted()) {
                        channel2.stop();
                    }
                    queue.clear();
                }
            }
            if (request.getPriority() == FIRST_PRIORITY_REQUEST) {
                if (channel1.isFree()) {
                    channel1.setRequest(request);
                } else if (channel2.isFree()) {
                    channel2.setRequest(request);
                } else if (channel1.getRequest().getPriority() == SECOND_PRIORITY_REQUEST) {
                    if (queue.isEmpty()) {
                        queue.setRequest(channel1.getRequest());
                    } else {
                        deniedRequests2++;
                        channel1.getRequest().deny();
                    }
                    channel1.setRequest(request);
                } else if (channel2.getRequest().getPriority() == SECOND_PRIORITY_REQUEST) {
                    if (queue.isEmpty()) {
                        queue.setRequest(channel2.getRequest());
                    } else {
                        deniedRequests2++;
                        channel2.getRequest().deny();
                    }
                    channel2.setRequest(request);
                } else if (queue.isEmpty()) {
                    queue.setRequest(request);
                } else if (queue.getRequest().getPriority() == SECOND_PRIORITY_REQUEST) {
                    deniedRequests2++;
                    queue.getRequest().deny();
                    queue.setRequest(request);
                } else {
                    deniedRequests1++;
                    request.deny();
                }
            }
            if (request.getPriority() == SECOND_PRIORITY_REQUEST) {
                if (channel1.isFree()) {
                    channel1.setRequest(request);
                } else if (channel2.isFree()) {
                    channel2.setRequest(request);
                } else if (queue.isEmpty()) {
                    queue.setRequest(request);
                } else {
                    deniedRequests2++;
                    request.deny();
                }
            }
        }
        return requests;
    }

    public class Request {
        private final int priority;
        private final double appearedAt;
        private final double processingTime;
        private double currentProcessingTime = 0;
        private boolean completed = false;
        private boolean denied = false;

        public Request(int priority, double appearedAt, double processingTime) {
            this.priority = priority;
            this.appearedAt = appearedAt;
            this.processingTime = processingTime;
        }

        public int getPriority() {
            return priority;
        }

        public boolean isDenied() {
            return denied;
        }

        public void deny() {
            this.denied = true;
        }

        public void updateProcessingTime(double dt) {
            currentProcessingTime += dt;
            if (currentProcessingTime >= processingTime) {
                completed = true;
            }
        }

        public boolean isCompleted() {
            return completed;
        }

        public double getOverTime() {
            return currentProcessingTime - processingTime;
        }
    }

    private class Channel {
        private Request request = null;

        public boolean isFree() {
            return request == null;
        }

        public boolean isBusy() {
            return request != null;
        }

        public Request getRequest() {
            return request;
        }

        public void setRequest(Request request) {
            this.request = request;
        }

        public void stop() {
            request = null;
        }
    }

    private class Queue {
        private Request request = null;

        public boolean isEmpty() {
            return request == null;
        }

        public boolean hasRequest() {
            return request != null;
        }

        public Request getRequest() {
            return request;
        }

        public void setRequest(Request request) {
            this.request = request;
        }

        public void clear() {
            request = null;
        }
    }

}
