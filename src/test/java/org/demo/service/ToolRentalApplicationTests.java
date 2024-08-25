package org.demo.service;

import org.demo.model.RentalAgreement;
import org.demo.model.ToolRentalApplication;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ToolRentalApplicationTests {

    @Test
    void testCheckoutWithInvalidRentalDays() {
        ToolRentalApplication app = new ToolRentalApplication();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            app.checkout("JAKR", 0, 10, "09/03/15");
        });
        assertEquals("Rental day count must be 1 or greater.", exception.getMessage());
    }

    @Test
    void testCheckoutWithInvalidDiscountPercent() {
        ToolRentalApplication app = new ToolRentalApplication();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            app.checkout("LADW", 3, 101, "07/02/20");
        });
        assertEquals("Discount percent must be between 0 and 100.", exception.getMessage());
    }

    @Test
    void testCheckoutWithValidParameters() {
        ToolRentalApplication app = new ToolRentalApplication();
        RentalAgreement agreement = app.checkout("LADW", 3, 10, "07/02/20");

        assertEquals("LADW", agreement.getTool().getToolCode());
        assertEquals("Ladder", agreement.getTool().getToolType());
        assertEquals("Werner", agreement.getTool().getBrand());
        assertEquals(3, agreement.getRentalDays());
        assertEquals("07/02/20", agreement.getCheckoutDate());
        assertEquals("07/05/20", agreement.getDueDate());
        assertEquals(1.99, agreement.getDailyCharge());
        assertEquals(2, agreement.getChargeDays());
        assertEquals(3.98, agreement.getPreDiscountCharge());
        assertEquals(10, agreement.getDiscountPercent());
        assertEquals(0.40, agreement.getDiscountAmount());
        assertEquals(3.58, agreement.getFinalCharge());
    }

    @Test
    void testLaborDayCharge() {
        ToolRentalApplication app = new ToolRentalApplication();
        RentalAgreement agreement = app.checkout("JAKR", 6, 0, "09/03/15");

        assertEquals("09/09/15", agreement.getDueDate());
        assertEquals(3, agreement.getChargeDays());
        assertEquals(8.97, agreement.getPreDiscountCharge());
        assertEquals(0, agreement.getDiscountPercent());
        assertEquals(8.97, agreement.getFinalCharge());
    }

    @Test
    void testIndependenceDayOnWeekday() {
        ToolRentalApplication app = new ToolRentalApplication();
        RentalAgreement agreement = app.checkout("CHNS", 5, 20, "07/02/19");

        assertEquals("07/07/19", agreement.getDueDate());
        assertEquals(3, agreement.getChargeDays()); // Independence Day should be free
        assertEquals(4.47, agreement.getPreDiscountCharge());
        assertEquals(20, agreement.getDiscountPercent());
        assertEquals(0.89, agreement.getDiscountAmount());
        assertEquals(3.58, agreement.getFinalCharge());
    }

    @Test
    void testMaxDiscountPercent() {
        ToolRentalApplication app = new ToolRentalApplication();
        RentalAgreement agreement = app.checkout("JAKD", 4, 100, "08/01/22");

        assertEquals("08/05/22", agreement.getDueDate());
        assertEquals(4, agreement.getChargeDays());
        assertEquals(11.96, agreement.getPreDiscountCharge());
        assertEquals(100, agreement.getDiscountPercent());
        assertEquals(11.96, agreement.getDiscountAmount());
        assertEquals(0.00, agreement.getFinalCharge());
    }

    @Test
    void testInvalidToolCode() {
        ToolRentalApplication app = new ToolRentalApplication();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            app.checkout("INVALID", 3, 10, "07/02/20");
        });
        assertEquals("Invalid tool code.", exception.getMessage());
    }

    @Test
    void testLaborDayWeekend() {
        ToolRentalApplication app = new ToolRentalApplication();
        RentalAgreement agreement = app.checkout("LADW", 4, 0, "09/04/21");

        assertEquals("09/08/21", agreement.getDueDate());
        assertEquals(3, agreement.getChargeDays()); // Labor Day is free
        assertEquals(5.97, agreement.getPreDiscountCharge());
        assertEquals(0, agreement.getDiscountPercent());
        assertEquals(5.97, agreement.getFinalCharge());
    }

    @Test
    void testIndependenceDayObservedOnFriday() {
        ToolRentalApplication app = new ToolRentalApplication();
        RentalAgreement agreement = app.checkout("CHNS", 5, 0, "07/01/22");

        assertEquals("07/06/22", agreement.getDueDate());
        assertEquals(3, agreement.getChargeDays());
        assertEquals(4.47, agreement.getPreDiscountCharge());
        assertEquals(0, agreement.getDiscountPercent());
        assertEquals(0.00, agreement.getDiscountAmount());
        assertEquals(4.47, agreement.getFinalCharge());
    }


    @Test
    void testChainsawWithNoWeekendCharges() {
        ToolRentalApplication app = new ToolRentalApplication();
        RentalAgreement agreement = app.checkout("CHNS", 7, 50, "06/10/22");

        assertEquals("06/17/22", agreement.getDueDate());
        assertEquals(5, agreement.getChargeDays()); // No charge for weekends
        assertEquals(7.45, agreement.getPreDiscountCharge());
        assertEquals(50, agreement.getDiscountPercent());
        assertEquals(3.73, agreement.getDiscountAmount(), 0.01);
        assertEquals(3.72, agreement.getFinalCharge(), 0.01);
    }


}
