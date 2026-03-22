package com.hazy.cache.graphics.widget.impl;

import com.hazy.cache.graphics.font.AdvancedFont;
import com.hazy.cache.graphics.widget.Widget;

public class TournamentWidget extends Widget {

    public static void unpack(AdvancedFont[] font) {
        tournament(font);
        tournamentWalk(font);
    }

    private static void tournamentWalk(AdvancedFont[] font) {
        Widget widget = addInterface(21100);
        widget.totalChildren(2);

        addOutlinedColorBox(21101, 0x534a40, 100, 40, 100);
        addText(21102, "<col=ffffff>", font, 1, 16750623, true);

        setBounds(21101, 200, 20, 0, widget);
        setBounds(21102, 250, 33, 1, widget);
    }

    private static void tournament(AdvancedFont[] font) {
        Widget widget = addInterface(19999);
        addSprite(20000, 1420);
        addText(20002, "Next world tournament: <col=ffff00>Dharok PK Tournament", font, 2, 16750623, true);
        addText(20003, "Tournament time:", font, 0, 16750623, true);
        addText(20004, "This tournament's prize will be..", font, 0, 16750623, false);
        addText(20005, "<col=ffff00>Previous Tournament Winners", font, 1, 16750623, true);
        addItem(20007, true, false);
        cache[20007].width = 4;
        cache[20007].inventoryMarginX = 11;
        cache[20007].inventoryMarginY = 10;
        cache[20007].inventoryItemId[0] = 13191;
        cache[20007].inventoryAmounts[0] = 1;
        Widget scroll = addTabInterface(20008);
        scroll.scrollMax = 475;
        scroll.width = 255;
        scroll.height = 123;
        scroll.hoverType = 87;
        scroll.totalChildren(35);
        int tick = 0;
        for (int index = 0; index < 35; index++) {
            if (tick == 2) {
                addText(20009 + index, "", font, 0, 16750623, true);
                tick = 0;
            } else if (tick == 0) {
                addText(20009 + index, "Patrick won <col=ffff00>x1 $10.00 bond", font, 0, 16750623, true);
                tick++;
            } else {
                addText(20009 + index, "from <col=ff7000>Dharok PK Tournament <col=ffff00>" + (index + 1) * 6 + " hours ago", font, 0, 16750623, true);
                tick++;
            }
            scroll.child(index, 20009 + index, 115, 1 + index * 13);
        }
        addCustomClickableText(20047, "Enter Tournament", "Enter Tournament", font, 0, 16750623, true, true, 100, 10);
        addCustomClickableText(20052, "Spectate", "Spectate", font, 0, 16750623, true, true, 100, 10);
        closeButton(20058, 142, 143, false);
        widget.totalChildren(10);
        int x_offset = 12;
        int y_offset = 10;
        widget.child(0, 20000, x_offset + 7, y_offset + 15);
        widget.child(1, 20002, x_offset + 240, y_offset + 25);
        widget.child(2, 20003, x_offset + 133, y_offset + 77);
        widget.child(3, 20004, x_offset + 47, y_offset + 92);
        widget.child(4, 20005, x_offset + 175, y_offset + 125);
        widget.child(5, 20007, x_offset + 287, y_offset + 73);
        widget.child(6, 20008, x_offset + 50, y_offset + 150);
        widget.child(7, 20058, x_offset + 450, y_offset + 22);
        widget.child(8, 20047, x_offset + 355, y_offset + 73);
        widget.child(9, 20052, x_offset + 353, y_offset + 117);
    }
}

