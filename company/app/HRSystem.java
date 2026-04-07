package company.app;

import company.core.Employee;

/**
 * Задание 1.2 — Модификаторы доступа (тестовый класс)
 *
 * Задача: запустите этот файл. Он НЕ скомпилируется полностью — это нормально.
 *
 * Для каждой строки (A–H) определите:
 *   1. Скомпилируется ли она?
 *   2. Если нет — какой модификатор доступа и почему он не виден из company.app?
 *
 * Заполните таблицу в answers/task1_2_access_table.md.
 * Подсказка: закомментируйте строки, которые не компилируются,
 * чтобы запустить остальные.
 */
public class HRSystem {
    public static void main(String[] args) {
        Employee emp = new Employee("Иван", 30, 80000, "secret");

        System.out.println(emp.name);            // Строка A
        //System.out.println(emp.age);             // Строка B
        //System.out.println(emp.salary);          // Строка C
        // System.out.println(emp.password);        // Строка D
        System.out.println(emp.getRole());       // Строка E
        // emp.promote(5000);                       // Строка F
        // emp.printSummary();                      // Строка G
        // emp.validatePassword("secret");          // Строка H

        /*
        Строка | Компилируется? | Причина
        A      | Да             | name — public, поэтому доступен из любого пакета без ограничений
        B      | Нет            | age — protected. В другом пакете доступен только наследникам, а HRSystem не наследует Employee
        C      | Нет            | salary — package-private. Виден только внутри пакета company.core, а HRSystem в company.app
        D      | Нет            | password — private. Доступен исключительно внутри класса Employee
        E      | Да             | getRole() — public, поэтому виден отовсюду, включая другие пакеты
        F      | Нет            | promote() — protected. Требует либо тот же пакет, либо наследование, чего нет у HRSystem
        G      | Нет            | printSummary() — package-private. Ограничен пакетом company.core, а HRSystem в company.app
        H      | Нет            | validatePassword() — private. Доступ есть только у методов самого класса Employee

        ИТОГ: из восьми строк компилируются только A и E.
        Причина — только public-члены доступны из другого пакета при отсутствии наследования.
        */
    }
}