package com.hazy.cache.graphics.widget.impl;

import com.hazy.cache.graphics.font.AdvancedFont;
import com.hazy.cache.graphics.widget.Widget;

/**
 *
 * The class which represents functionality for the price checker interface.
 *
 * @author Patrick van Elderen
 * @see <a href="https://www.rune-server.ee/members/_Patrick_/">Rune-Server profile</a>
 *
 */
public class PriceChecker extends Widget {

    public static void unpack(AdvancedFont[] font) {

        Widget widget = addInterface(49500);
        addSprite(49501, 180);
        addContainer(49542, 7, 4, 40, 28, true, "Take 1", "Take 5", "Take 10", "Take All", "Take X");
        closeButton(49502, 107, 108, true);
        addHoverButton(49505, 181, 36, 36, "Add all", -1, 49506, 1);
        addHoveredButton(49506, 182, 36, 36, 49507);
        addHoverButton(49508, 183, 36, 36, "Search for item", -1, 49509, 1);
        addHoveredButton(49509, 184, 36, 36, 49510);
        addText(49511, "Grand Exchange guide prices", font, 2, 0xFF981F, true, true);
        addText(49512, "Total guide price:", font, 1, 0xFF981F, true, true);
        addText(49513, "115,424,152", font, 0, 0xffffff, true, true);
        addText(49550, "", font, 0, 0xffffff, true, true);
        addText(49551, "", font, 0, 0xffffff, true, true);
        addText(49552, "", font, 0, 0xffffff, true, true);
        addText(49553, "", font, 0, 0xffffff, true, true);
        addText(49554, "", font, 0, 0xffffff, true, true);
        addText(49555, "", font, 0, 0xffffff, true, true);
        addText(49556, "", font, 0, 0xffffff, true, true);
        addText(49557, "", font, 0, 0xffffff, true, true);
        addText(49558, "", font, 0, 0xffffff, true, true);
        addText(49559, "", font, 0, 0xffffff, true, true);
        addText(49560, "", font, 0, 0xffffff, true, true);
        addText(49561, "", font, 0, 0xffffff, true, true);
        addText(49562, "", font, 0, 0xffffff, true, true);
        addText(49563, "", font, 0, 0xffffff, true, true);
        addText(49564, "", font, 0, 0xffffff, true, true);
        addText(49565, "", font, 0, 0xffffff, true, true);
        addText(49566, "", font, 0, 0xffffff, true, true);
        addText(49567, "", font, 0, 0xffffff, true, true);
        addText(49568, "", font, 0, 0xffffff, true, true);
        addText(49569, "", font, 0, 0xffffff, true, true);
        addText(49570, "", font, 0, 0xffffff, true, true);
        addText(49571, "", font, 0, 0xffffff, true, true);
        addText(49572, "", font, 0, 0xffffff, true, true);
        addText(49573, "", font, 0, 0xffffff, true, true);
        addText(49574, "", font, 0, 0xffffff, true, true);
        addText(49575, "", font, 0, 0xffffff, true, true);
        addText(49576, "", font, 0, 0xffffff, true, true);
        addText(49577, "", font, 0, 0xffffff, true, true);
        addContainer(49581, 7, 4, 40, 28, true, new String[] { null, null, null, null, null });
        addText(49582, "", font, 0, 0xffffff, false, true);
        addText(49583, "", font, 0, 0xffffff, false, true);
        widget.totalChildren(39);
        widget.child(0, 49501, 15, 15);
        widget.child(1, 49502, 467, 22);
        widget.child(2, 49505, 451, 285);
        widget.child(3, 49508, 25, 285);
        widget.child(4, 49511, 260, 22);
        widget.child(5, 49512, 255, 286);
        widget.child(6, 49513, 255, 306);
        widget.child(7, 49542, 24, 57);
        widget.child(8, 49550, 38, 93);
        widget.child(9, 49551, 111, 93);
        widget.child(10, 49552, 183, 93);
        widget.child(11, 49553, 254, 93);
        widget.child(12, 49554, 326, 93);
        widget.child(13, 49555, 400, 93);
        widget.child(14, 49556, 470, 93); //line 1 end
        widget.child(15, 49557, 39, 153);
        widget.child(16, 49558, 111, 153);
        widget.child(17, 49559, 183, 153);
        widget.child(18, 49560, 254, 153);
        widget.child(19, 49561, 326, 153);
        widget.child(20, 49562, 400, 153);
        widget.child(21, 49563, 471, 153); //line 2 end
        widget.child(22, 49564, 39, 210);
        widget.child(23, 49565, 111, 213);
        widget.child(24, 49566, 183, 213);
        widget.child(25, 49567, 254, 213);
        widget.child(26, 49568, 326, 213);
        widget.child(27, 49569, 400, 213);
        widget.child(28, 49570, 471, 213); //line 3 end
        widget.child(29, 49571, 39, 256);
        widget.child(30, 49572, 111, 256);
        widget.child(31, 49573, 183, 256);
        widget.child(32, 49574, 254, 256);
        widget.child(33, 49575, 326, 256);
        widget.child(34, 49576, 400, 256);
        widget.child(35, 49577, 471, 256);
        widget.child(36, 49581, 12, 291);
        widget.child(37, 49582, 51, 296);
        widget.child(38, 49583, 51, 312);
    }
}

