package com.twisted.game.world.entity.combat.ranged;

public enum ThrownaxeDrawback {

    BRONZE(800, -1, -1),
    IRON(801, -1, -1),
    STEEL(802, -1, -1),
    MITHRIL(803, -1, -1),
    ADAMANT(804, -1, -1),
    RUNE(805, -1, -1),
    DRAGON(20849, 1320, 1319);

    public int arrow, gfx, projectile;

    ThrownaxeDrawback(int arrow, int gfx, int projectile) {
        this.arrow = arrow;
        this.gfx = gfx;
        this.projectile = projectile;
    }

    public static ThrownaxeDrawback find(int ammo) {
        for (ThrownaxeDrawback thrownaxeDrawback : ThrownaxeDrawback.values()) {
            if(thrownaxeDrawback.arrow == ammo) {
                return thrownaxeDrawback;
            }
        }
        return null;
    }
}
