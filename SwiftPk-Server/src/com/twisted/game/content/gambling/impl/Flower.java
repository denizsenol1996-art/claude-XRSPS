package com.twisted.game.content.gambling.impl;

import java.security.SecureRandom;

public enum Flower {
    /**
     * The black flower
     */
    BLACK(2988, 99.7),
    /**
     * The blue flower
     */
    BLUE(2982, 42.6),
    /**
     * The orange flower
     */
    ORANGE(2985, 85.2),
    /**
     * The pastel flower. Purple, blue, cyan
     */
    PASTEL(2980, 0.00),
    /**
     * The purple flower
     */
    PURPLE(2984, 71.0),
    /**
     * The rainbow flower. Red, yellow, blue
     */
    RAINBOW(2986, 14.2),
    /**
     * The red flower
     */
    RED(2981, 28.4),
    /**
     * The white flower
     */
    WHITE(2987, 99.4),
    /**
     * The yellow
     */
    YELLOW(2983, 56.8);

    /**
     * The id
     */
    private int id;
    public final double chance;

    /**
     * The id
     *
     * @param id
     *            the id
     */
    Flower(int id, double chance) {
        this.setId(id);
        this.chance = chance;
    }

    /**
     * Gets the id
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id
     *
     * @param id
     *            the id
     */
    public void setId(int id) {
        this.id = id;
    }

    private static final SecureRandom random = new SecureRandom();
    
    public static Flower flower() {
        double chance = random.nextDouble() * 100; //Generate a random double to use as our 'chance'

        Flower flower = null;
        if (chance >= Flower.BLACK.chance) {
            flower = Flower.BLACK;
        } else if (chance >= Flower.WHITE.chance) {
            flower = Flower.WHITE;
        } else if (chance >= Flower.ORANGE.chance) {
            flower = Flower.ORANGE;
        } else if (chance >= Flower.PURPLE.chance) {
            flower = Flower.PURPLE;
        } else if (chance >= Flower.YELLOW.chance) {
            flower = Flower.YELLOW;
        } else if (chance >= Flower.BLUE.chance) {
            flower = Flower.BLUE;
        } else if (chance >= Flower.RED.chance) {
            flower = Flower.RED;
        } else if (chance >= Flower.RAINBOW.chance) {
            flower = Flower.RAINBOW;
        } else if (chance >= Flower.PASTEL.chance) {
            flower = Flower.PASTEL;
        }
        return flower;
    }
}
