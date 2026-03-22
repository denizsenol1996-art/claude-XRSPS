package com.twisted.game.world.entity.combat.ranged;

public enum ArrowDrawBack {

    BRONZE_ARROW(882, 19, 10),
    IRON_ARROW(884, 18, 9),
    STEEL_ARROW(886, 20, 11),
    MITHRIL_ARROW(888, 21, 12),
    ADAMANT_ARROW(890, 22, 13),
    RUNITE_ARROW(892, 24, 15),
    AMETHYST_ARROW(21326, 1385, 15),
    DRAGON_ARROW(11212, 1116, 1120),
    DRAGON_ARROW_20389(20389,1116, 1120),
    DRAGON_ARROW_P(11227,1116, 1120),
    DRAGON_ARROW_P_PLUS(11228,1116, 1120),
    DRAGON_ARROW_P_PLUS_PLUS(11229,1116, 1120),
    FIRE_ARROW(4160, 20, 11),
    ICE_ARROW(78, 1116, 1120),
    EARTH_ARROW(2866, 243, 242),
    CRAWS_BOW(-1,1611, 1574),
    WEB_WEAVER_BOW(-1, 2283, 2282),
    ;

    public final int arrow;
    public final int gfx;
    public final int projectile;

    ArrowDrawBack(int arrow, int gfx, int projectile) {
        this.arrow = arrow;
        this.gfx = gfx;
        this.projectile = projectile;
    }

    public static ArrowDrawBack find(int ammo) {
        for (ArrowDrawBack arrowDrawBack : ArrowDrawBack.values()) {
            if(arrowDrawBack.arrow == ammo) {
                return arrowDrawBack;
            }
        }
        return null;
    }
}
