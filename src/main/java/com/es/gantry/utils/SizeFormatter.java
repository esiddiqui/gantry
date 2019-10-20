package com.es.gantry.utils;

public class SizeFormatter {


    public long convertSizeToBytes(final String sizeString) {

        if (sizeString==null || sizeString.trim().length()==0)
            return 0;

        String str =sizeString.trim();

        int indexForUnit = str.length()-2;
        for (int i=0; i<str.length(); i++) {
            if (Character.isAlphabetic(str.charAt(i))) {
                indexForUnit = i;
                break;
            }
        }
        Long value = Long.parseLong(str.substring(0,indexForUnit));
        String strUnit = str.substring(indexForUnit).trim();
        if (strUnit.equalsIgnoreCase(SizeUnit.B.unit))
            return value * SizeUnit.B.multiplier;
        if (strUnit.equalsIgnoreCase(SizeUnit.KB.unit))
            return value * SizeUnit.KB.multiplier;
        if (strUnit.equalsIgnoreCase(SizeUnit.MB.unit))
            return value * SizeUnit.MB.multiplier;
        if (strUnit.equalsIgnoreCase(SizeUnit.GB.unit))
            return value * SizeUnit.GB.multiplier;
        if (strUnit.equalsIgnoreCase(SizeUnit.TB.unit))
            return value * SizeUnit.TB.multiplier;
        return value;
    }



    public static enum SizeUnit {
        B("B",1),
        KB("KB",1024),
        MB("MB",1024*1024),
        GB("GB",1024*1024*1024),
        TB("TB",1024*1024*1024*1024);

        private String unit;

        private long multiplier;

        SizeUnit(String unit, long multiplier) {
            this.unit = unit;
            this.multiplier = multiplier;
        }

        public String getUnit() {
            return unit;
        }

        public long getMultiplier() {
            return multiplier;
        }
    }
}
