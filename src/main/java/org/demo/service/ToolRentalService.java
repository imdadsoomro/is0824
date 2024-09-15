package org.demo.service;
import org.demo.model.RentalAgreement;
import org.demo.model.Tool;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ToolRentalService {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yy");
    private static final Map<String, Tool> tools = new HashMap<>();

    static {
        tools.put("CHNS", new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true));
        tools.put("LADW", new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false));
        tools.put("JAKD", new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false));
        tools.put("JAKR", new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false));
    }

    public RentalAgreement checkout(String toolCode, int rentalDays, int discountPercent, String checkoutDate) {
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental day count must be 1 or greater.");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }

        Tool tool = tools.get(toolCode);
        if (tool == null) {
            throw new IllegalArgumentException("Invalid tool code.");
        }

        LocalDate checkoutLocalDate = LocalDate.parse(checkoutDate, DATE_FORMAT);
        LocalDate dueDate = checkoutLocalDate.plusDays(rentalDays);

        int chargeDays = calculateChargeDays(tool, checkoutLocalDate, dueDate);
        double preDiscountCharge = chargeDays * tool.getDailyCharge();
        double discountAmount = preDiscountCharge * (discountPercent / 100.0);
        double finalCharge = preDiscountCharge - discountAmount;
        String formattedFinalCharge = String.format("%.2f", finalCharge);
        String formattedDiscountAmount = String.format("%.2f", discountAmount);
        RentalAgreement rentalAgreement = new RentalAgreement(
                tool,
                rentalDays,
                checkoutLocalDate.format(DATE_FORMAT),
                dueDate.format(DATE_FORMAT),
                tool.getDailyCharge(),
                chargeDays,
                preDiscountCharge,
                discountPercent,
                Double.parseDouble(formattedDiscountAmount),
                Double.parseDouble(formattedFinalCharge)
        );
        System.out.println("======================================");
        rentalAgreement.printAgreement();
        System.out.println("======================================");
        return rentalAgreement;
    }

    private int calculateChargeDays(Tool tool, LocalDate checkoutDate, LocalDate dueDate) {
        int chargeDays = 0;
        LocalDate currentDate = checkoutDate.plusDays(1);

        while (!currentDate.isAfter(dueDate)) {
            boolean isHoliday = isHoliday(currentDate);
            boolean isWeekend = currentDate.getDayOfWeek().getValue() == 6 || currentDate.getDayOfWeek().getValue() == 7;

            if ((tool.isWeekdayCharge() && !isWeekend && !isHoliday) ||
                    (tool.isWeekendCharge() && isWeekend) ||
                    (tool.isHolidayCharge() && isHoliday)) {
                chargeDays++;
            }

            currentDate = currentDate.plusDays(1);
        }

        return chargeDays;
    }

    private boolean isHoliday(LocalDate date) {
        if (date.getMonthValue() == 7) {
            if (date.getDayOfMonth() == 4) {
                return true;
            }
            if ((date.getDayOfMonth() == 3 && date.getDayOfWeek().getValue() == 5) ||
                    (date.getDayOfMonth() == 5 && date.getDayOfWeek().getValue() == 1)) {
                return true;
            }
        }

        if (date.getMonthValue() == 9 && date.getDayOfWeek() == DayOfWeek.MONDAY) {
            if (date.getDayOfMonth() <= 7) {
                return true;
            }
        }

        return false;
    }
}