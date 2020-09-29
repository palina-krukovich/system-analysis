package com.bsuir;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите p, p1 and p2:");
        double p = scanner.nextDouble();
        double p1 = scanner.nextDouble();
        double p2 = scanner.nextDouble();
        System.out.println("Введите количество тактов:");
        int ticks = scanner.nextInt();

        StateGenerator stateGenerator = new StateGenerator(p, p1, p2);
        for (int i = 0; i < ticks; i++) {
            stateGenerator.generateNextState();
        }

        System.out.println();
        System.out.println("Количество тактов: " + ticks);
        System.out.println();
        System.out.println("Количество выданных источником заявок: " + stateGenerator.getTotalRequestsCount());
        System.out.println("Количество обработанных заявок: " + stateGenerator.getProceedRequestsCount());
        System.out.println("Количество отказов: " + stateGenerator.getDenies());
        System.out.println();
        System.out.println("Количество тактов с занятым каналом 1: " + stateGenerator.getBusyTicks1());
        System.out.println("Количество тактов с занятым каналом 2: " + stateGenerator.getBusyTicks2());
        System.out.println();
        double pDeny = (double) stateGenerator.getDenies() / stateGenerator.getTotalRequestsCount();
        System.out.println("Вероятность отказаза (P отк): " +
                toPercents(pDeny) + "%");
        System.out.println("Относительная пропускная способность (Q): " +
                toPercents(1 - pDeny) + "%");
        System.out.println("Абсолютная пропускная способность (А): " +
                (double) stateGenerator.getProceedRequestsCount() / ticks);
        System.out.println("Коэффициент загрузки канала 1 (K кан): " +
                toPercents((double) stateGenerator.getBusyTicks1() / ticks) + "%");
        System.out.println("Коэффициент загрузки канала 2 (K кан): " +
                toPercents((double) stateGenerator.getBusyTicks2() / ticks) + "%");
    }

    private static double toPercents(double num) {
        return (double) Math.round(num * 100000) / 1000;
    }

}

