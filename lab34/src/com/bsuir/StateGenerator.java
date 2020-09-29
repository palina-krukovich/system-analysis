package com.bsuir;

import java.util.Random;

public class StateGenerator {
    // генератор случайных чисел для расчета выдачи заявки источником
    private Random rand;
    // генератор случайных чисел для расчета обслуживания заявки каналом 1
    private Random rand1;
    // генератор случайных чисел для расчета обслуживания заявки каналом 2
    private Random rand2;
    // вероятность не выдачи заявки источником
    private double p;
    // вероятность не обслуживания заявки каналом 1
    private double p1;
    // вероятность не обслуживания заявки каналом 2
    private double p2;
    // {0, 1} – количество заявок в очереди
    private byte j;
    // {0, 1} – количество заявок в канале 1
    private byte t1;
    // {0, 1} – количество заявок в канале 2
    private byte t2;
    // количество отказов системы
    private int denies;
    // количество тактов, когда канал 1 занят
    private int busyTicks1;
    // количество тактов, когда канал 2 занят
    private int busyTicks2;
    // количество обработанных заявок
    private int proceedRequestsCount;
    // количество выданных источником заявок
    private int totalRequestsCount;

    public StateGenerator(double p, double p1, double p2) {
        j = 0;
        t1 = 0;
        t2 = 0;
        denies = 0;
        busyTicks1 = 0;
        busyTicks2 = 0;
        proceedRequestsCount = 0;
        totalRequestsCount = 0;
        rand = new Random();
        rand1 = new Random();
        rand2 = new Random();
        this.p = p;
        this.p1 = p1;
        this.p2 = p2;
    }

    public byte getJ() {
        return j;
    }

    public byte getT1() {
        return t1;
    }

    public byte getT2() {
        return t2;
    }

    public int getDenies() {
        return denies;
    }

    public int getBusyTicks1() {
        return busyTicks1;
    }

    public int getBusyTicks2() {
        return busyTicks2;
    }

    public int getProceedRequestsCount() {
        return proceedRequestsCount;
    }

    public int getTotalRequestsCount() {
        return totalRequestsCount;
    }

    /**
     * Показывает была ли выдана заявка.
     * @return true - если заявка была выдана,
     *         false - если новых заявок нет.
     */
    private boolean isNewRequest() {
        return rand.nextDouble() >= p;
    }

    /**
     * Показывает, завершил ли обработку заявки канал 1.
     * @return true - если канал 1 завершил обработку заявки,
     *         false - если канал 1 еще не завершил обработку заявки.
     */
    private boolean proceed1() {
        return rand1.nextDouble() >= p1;
    }

    /**
     * Показывает, завершил ли обработку заявки канал 2.
     * @return true - если канал 2 завершил обработку заявки,
     *         false - если канал 2 еще не завершил обработку заявки.
     */
    private boolean proceed2() {
        return rand2.nextDouble() < p2;
    }

    public void generateNextState() {
        if (t1 == 1) { // Если в канале 1 есть заявка
            if (proceed1()) { // Если обработка завершена
                t1 = 0;
                proceedRequestsCount++;
            } else {
                busyTicks1++;
            }
        }
        if (t2 == 1) { // Если в канале 2 есть заявка
            if (proceed2()) { // Если обработка завершена
                t2 = 0;
                proceedRequestsCount++;
            } else {
                busyTicks2++;
            }
        }
        if(j == 1) { // Если есть заявка в очереди
            if (t1 == 0) { // Если канал 1 свободен
                j = 0;
                t1 = 1;
                busyTicks1++;
            }
            else if (t2 == 0) // Если канал 2 свободен
            {
                j = 0;
                t2 = 1;
                busyTicks2++;
            }
        }
        if (isNewRequest()) { // Если источник выдал заявку
            totalRequestsCount++;
            if (j == 0) { // Если нет заявок в очереди
                if (t1 == 0) { // Если канал 1 свободен
                    t1 = 1;
                    busyTicks1++;
                } else if (t2 == 0) { // Если канал 2 свободен
                    t2 = 1;
                    busyTicks2++;
                } else { // Если оба канала заняты
                    j = 1; // Помещаем заявку в очередь
                }
            } else { // Если есть заявка в очереди
                denies++; // Увеличиваем количество отказов системы
            }
        }
    }
}

