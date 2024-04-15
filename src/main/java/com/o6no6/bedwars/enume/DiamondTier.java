package com.o6no6.bedwars.enume;

public enum DiamondTier {
    DIAMOND_I((short)1,"Diamante I", (short) 300, (short) 120),
    DIAMOND_II((short)2,"Diamante II", (short) 300,(short) 60),
    DIAMOND_III((short)3,"Diamante III", (short) 300,(short) 30);

    private final String name;
    private final short timetoreach;
    private final short reloadTime;
    private final short position;

    DiamondTier(short position, String name, short timetoreach, short reloadTime) {
        this.name = name;
        this.timetoreach = timetoreach;
        this.reloadTime = reloadTime;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public short getTimeToReach() {
        return timetoreach;
    }

    public short getReloadTime() {
        return reloadTime;
    }

    public short getPosition() {
        return position;
    }
}
