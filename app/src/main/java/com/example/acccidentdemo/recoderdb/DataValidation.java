package com.example.acccidentdemo.recoderdb;

import java.util.List;

/**
 * Creted by User on 20-Dec-17
 */

public class DataValidation {

    public static boolean isGrivanceIndicatorAvailable(int jOccupation) {
        return jOccupation != 1;
    }

    public static double getMaxValue(List<Double> numbers) {
        if (numbers == null || numbers.size() == 0) {
            return 0;
        }
        double maxValue = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            if (numbers.get(i) > maxValue) {
                maxValue = numbers.get(i);
            }
        }
        return maxValue;
    }

    public static double getMinValue(List<Double> numbers) {
        if (numbers == null || numbers.size() == 0) {
            return 0;
        }
        double minValue = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            if (numbers.get(i) < minValue) {
                minValue = numbers.get(i);
            }
        }
        return minValue;

    }

    public static double getAverage(List<Double> numbers) {
        if (numbers == null || numbers.size() == 0) {
            return 0;
        }
        double sum = 0;
        for (int i = 1; i < numbers.size(); i++) {
            sum += numbers.get(i);
        }
        return sum / numbers.size();
    }

    public static class Manditory {
        private static final int QUESTION_MANDATORY = 1;
        private static final int SHOW_PHOTO = 2;
        private static final int QUESTION_MANDATORY_SHOW_PHOTO = 3;

        public static int getQuestionMandatoryBit() {
            return QUESTION_MANDATORY;
        }

        public static boolean isQuestionMandatory(int jFlag) {
//            return (jFlag&1)==1;
            return false;
        }

        public static boolean showPhoto(int jFlag) {
//            return (jFlag &(1<<2))==1;
                        return jFlag == 2;
        }

        public static boolean questionMandatoryAndShowPhoto(int jFlag) {
            return jFlag == 3;
        }

    }
}
