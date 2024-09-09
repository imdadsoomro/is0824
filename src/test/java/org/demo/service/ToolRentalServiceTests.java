package org.demo.service;

import org.demo.model.RentalAgreement;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ToolRentalServiceTests {

    @Test
    void testInvalidDiscount() {
        ToolRentalService service = new ToolRentalService();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.checkout("JAKR", 5, 101, "09/03/15");
        });
        assertEquals("Discount percent must be between 0 and 100.", exception.getMessage());
    }

    @Test
    void testLADWRentalJuly2020() {
        ToolRentalService service = new ToolRentalService();
        RentalAgreement agreement = service.checkout("LADW", 3, 10, "07/02/20");

        assertEquals("07/05/20", agreement.getDueDate());
        assertEquals(2, agreement.getChargeDays());
        assertEquals(3.98, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(10, agreement.getDiscountPercent()); // 10% discount
        assertEquals(0.40, agreement.getDiscountAmount(), 0.01);
        assertEquals(3.58, agreement.getFinalCharge(), 0.01);
    }

    @Test
    void testCHNSRentalJuly2015() {
        ToolRentalService service = new ToolRentalService();
        RentalAgreement agreement = service.checkout("CHNS", 5, 25, "07/02/15");

        assertEquals("07/07/15", agreement.getDueDate());
        assertEquals(4, agreement.getChargeDays());
        assertEquals(5.96, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(25, agreement.getDiscountPercent());
        assertEquals(1.49, agreement.getDiscountAmount(), 0.01);
        assertEquals(4.47, agreement.getFinalCharge(), 0.01);
    }

    @Test
    void testJAKDRentalSeptember2015() {
        ToolRentalService service = new ToolRentalService();
        RentalAgreement agreement = service.checkout("JAKD", 6, 0, "09/03/15");

        assertEquals("09/09/15", agreement.getDueDate());
        assertEquals(3, agreement.getChargeDays());
        assertEquals(8.97, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0, agreement.getDiscountPercent());
        assertEquals(0.00, agreement.getDiscountAmount(), 0.01);
        assertEquals(8.97, agreement.getFinalCharge(), 0.01);
    }

    @Test
    void testJAKRRentalJuly2015() {
        ToolRentalService service = new ToolRentalService();
        RentalAgreement agreement = service.checkout("JAKR", 9, 0, "07/02/15");

        assertEquals("07/11/15", agreement.getDueDate());
        assertEquals(5, agreement.getChargeDays());
        assertEquals(14.95, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0, agreement.getDiscountPercent());
        assertEquals(0.00, agreement.getDiscountAmount(), 0.01);
        assertEquals(14.95, agreement.getFinalCharge(), 0.01);
    }

    @Test
    public void testJAKRRentalJuly2020() {
        ToolRentalService service = new ToolRentalService();
        RentalAgreement agreement = service.checkout("JAKR", 4, 50, "07/02/20");


        assertEquals(1, agreement.getChargeDays());
        assertEquals(2.99, agreement.getPreDiscountCharge());
        assertEquals(1.5, agreement.getDiscountAmount());
        assertEquals(1.5, agreement.getFinalCharge());
    }

    @Test
    void testCheckoutWithInvalidRentalDays() {
        ToolRentalService app = new ToolRentalService();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            app.checkout("JAKR", 0, 10, "09/03/15");
        });
        assertEquals("Rental day count must be 1 or greater.", exception.getMessage());
    }

    @Test
    void testCheckoutWithInvalidDiscountPercent() {
        ToolRentalService app = new ToolRentalService();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            app.checkout("LADW", 3, 101, "07/02/20");
        });
        assertEquals("Discount percent must be between 0 and 100.", exception.getMessage());
    }

    @Test
    void testCheckoutWithValidParameters() {
        ToolRentalService app = new ToolRentalService();
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
        ToolRentalService app = new ToolRentalService();
        RentalAgreement agreement = app.checkout("JAKR", 6, 0, "09/03/15");

        assertEquals("09/09/15", agreement.getDueDate());
        assertEquals(3, agreement.getChargeDays());
        assertEquals(8.97, agreement.getPreDiscountCharge());
        assertEquals(0, agreement.getDiscountPercent());
        assertEquals(8.97, agreement.getFinalCharge());
    }

    @Test
    void testIndependenceDayOnWeekday() {
        ToolRentalService app = new ToolRentalService();
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
        ToolRentalService app = new ToolRentalService();
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
        ToolRentalService app = new ToolRentalService();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            app.checkout("INVALID", 3, 10, "07/02/20");
        });
        assertEquals("Invalid tool code.", exception.getMessage());
    }

    @Test
    void testLaborDayWeekend() {
        ToolRentalService app = new ToolRentalService();
        RentalAgreement agreement = app.checkout("LADW", 4, 0, "09/04/21");

        assertEquals("09/08/21", agreement.getDueDate());
        assertEquals(3, agreement.getChargeDays());
        assertEquals(5.97, agreement.getPreDiscountCharge());
        assertEquals(0, agreement.getDiscountPercent());
        assertEquals(5.97, agreement.getFinalCharge());
    }

    @Test
    void testIndependenceDayObservedOnFriday() {
        ToolRentalService app = new ToolRentalService();
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
        ToolRentalService app = new ToolRentalService();
        RentalAgreement agreement = app.checkout("CHNS", 7, 50, "06/10/22");

        assertEquals("06/17/22", agreement.getDueDate());
        assertEquals(5, agreement.getChargeDays());
        assertEquals(7.45, agreement.getPreDiscountCharge());
        assertEquals(50, agreement.getDiscountPercent());
        assertEquals(3.73, agreement.getDiscountAmount(), 0.01);
        assertEquals(3.72, agreement.getFinalCharge(), 0.01);
    }
}
