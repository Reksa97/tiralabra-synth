package main;

import gui.Frame;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // erase terminal
        System.out.print("\033[H\033[2J");

        System.out.println("Kuinka monta oskillaattoria halutaan? (1-8)");
        Scanner scanner = new Scanner(System.in);
        int oscillators = 4;

        while (true) {
            try {
                String read = scanner.nextLine();
                int parsed = Integer.parseInt(read);

                if (parsed >= 1 && parsed <= 8) {
                    oscillators = parsed;
                    break;
                }

            } catch (Exception e) {}
            System.out.println("Valitse luku väliltä 1-8");
        }
        new Frame(oscillators);

        System.out.println("Pianon koskettimia vastaa näppäimistön ylin (Q - Å) ja toisiksi ylin (A - Ä) kirjainrivi.");
        System.out.println("Oktaavin saa vaihdettua ylös merkillä 'm' ja alas merkillä 'n'.");
        System.out.println("\n \n \n \n");
    }
}
