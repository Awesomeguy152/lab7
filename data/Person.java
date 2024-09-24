package org.example.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Scanner;

public class Person implements Serializable {
    private String name;
    private Date birthday;
    private int weight;
    private Color eyeColor;

    public Person() {
    }

    public Person(String frontManName, LocalDate frontManBirthday, int frontManWeight, String frontManEyeColor) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                ", weight=" + weight +
                ", eyeColor=" + eyeColor +
                '}';
    }
    public static Person createFrontMan() {
        Scanner scanner = new Scanner(System.in);
        Person frontMan = new Person();

        System.out.print("Введите имя фронтмена: ");
        String name = scanner.nextLine().trim();
        frontMan.setName(name);

        while (true) {
            try {
                System.out.print("Введите дату рождения фронтмена (формат: yyyy-MM-dd или оставьте пустым): ");
                String birthdayInput = scanner.nextLine().trim();
                if (!birthdayInput.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate birthday = LocalDate.parse(birthdayInput, formatter);
                    frontMan.setBirthday(Date.from(birthday.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Ошибка при вводе даты. Попробуйте снова.");
            }
        }

        while (true) {
            try {
                System.out.print("Введите вес фронтмена: ");
                int weight = Integer.parseInt(scanner.nextLine().trim());
                if (weight > 0) {
                    frontMan.setWeight(weight);
                } else {
                    System.out.println("Вес должен быть положительным числом.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка при вводе веса. Попробуйте снова.");
            }
        }

        System.out.print("Выберите цвет глаз фронтмена (BLUE, YELLOW, BROWN): ");
        while (true) {
            try {
                String colorInput = scanner.nextLine().trim().toUpperCase();
                if (!colorInput.isEmpty()) {
                    Color eyeColor = Color.valueOf(colorInput);
                    frontMan.setEyeColor(eyeColor);
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Некорректный цвет. Пожалуйста, выберите один из BLUE, YELLOW, BROWN.");
            }
        }

        return frontMan;
    }

    public String toXML() {
        return "<frontMan>\n" +
                "<name>" + name + "</name>\n"+
                "<birthday>" + birthday + "</birthday>\n" +
                "<weight>" + weight + "</weight>\n" +
                "<eyeColor>" + eyeColor + "</eyeColor>\n" +
                "</frontMan>";
    }
}
