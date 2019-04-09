package com.aaronzadev.util;

import com.aaronzadev.model.pojo.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CBoxManager {

    private static final ObservableList<Month> months = FXCollections.observableArrayList(
            new Month(1, "Enero"),
            new Month(2, "Febrero"),
            new Month(3, "Marzo"),
            new Month(4, "Abril"),
            new Month(5, "Mayo"),
            new Month(6, "Junio"),
            new Month(7, "Julio"),
            new Month(8, "Agosto"),
            new Month(9, "Septiembre"),
            new Month(10, "Octubre"),
            new Month(11, "Noviembre"),
            new Month(12, "Diciembre")
    );

    private static final ObservableList<Integer> years = FXCollections.observableArrayList(
            2019, 2020, 2021, 2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030,
            2031, 2032, 2033, 2034, 2035
    );

    private static final ObservableList<String> fastMethods = FXCollections.observableArrayList(
            "Ventas del Día",
            "Ventas de la Semana",
            "Ventas del Mes"
    );

    private static final ObservableList<Integer> nums = FXCollections.observableArrayList(
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20

    );

    private static  final ObservableList<Character> letters = FXCollections.observableArrayList(
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','Ñ','O','P','Q','R','S','T','U',
            'V','W','X','Y','Z'
    );

    private static final ObservableList<Role> roles = FXCollections.observableArrayList(
            new Role((short)1, "Administrador"),
            new Role((short)2, "Mostrador"),
            new Role((short)3, "Técnico"),
            new Role((short)4, "Bloqueado")
    );

    private static final ObservableList<RevStatus> revStatus = FXCollections.observableArrayList(
            new RevStatus((short)0, "Sin Revisar"),
            new RevStatus((short)1, "En Revision"),
            new RevStatus((short)2, "Revisado")
    );

    private static final ObservableList<FixStatus> fstatus = FXCollections.observableArrayList(
            new FixStatus((short)0, "No Reparado"),
            new FixStatus((short)1, "Reparado")
    );

    private static final ObservableList<CashOperationType> opsType = FXCollections.observableArrayList(
            new CashOperationType((short) 1, "Ingreso Caja Chica", "ICC"),
            new CashOperationType((short) 2, "Ingreso Venta", "VTA"),
            new CashOperationType((short) 3, "Ingreso Servicio Tec", "IST"),
            new CashOperationType((short) 4, "Egreso Gastos", "GTO"),
            new CashOperationType((short) 5, "Egreso Corte de Caja", "ECC")
    );

    private static final ObservableList<PriorityService> priorities = FXCollections.observableArrayList(
            new PriorityService((short) 0, "Prioridad Baja", "Mas de 5 Dias"),
            new PriorityService((short) 1, "Prioridad Media", "Entre 3 y 5 Dias"),
            new PriorityService((short) 2, "Prioridad Alta", "1 - 2 Dias")
    );

    public static ObservableList<Month> getMonths(){
        return months;
    }

    public static ObservableList<Integer> getYears(){
        return years;
    }

    public static ObservableList<String> getMethods(){
        return fastMethods;
    }

    public static ObservableList<Integer> getNums(){
        return nums;
    }

    public static ObservableList<Character> getAlphabet(){
        return letters;
    }

    public static ObservableList<Role> getRoles(){
        return roles;
    }

    public static ObservableList<RevStatus> getRevStatus(){
        return revStatus;
    }

    public static ObservableList<FixStatus> getFixstatus() {
        return fstatus;
    }

    public static ObservableList<CashOperationType> getOpsType(){
        return opsType;
    }

    public static ObservableList<PriorityService> getPriorityServices(){
        return priorities;
    }
}
