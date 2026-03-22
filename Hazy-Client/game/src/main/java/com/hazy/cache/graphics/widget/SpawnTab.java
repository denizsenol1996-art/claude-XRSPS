package com.hazy.cache.graphics.widget;

import com.hazy.cache.graphics.font.AdvancedFont;

import static com.hazy.ClientConstants.PVP_ALLOWED_SPAWNS;

/**
 * @author Patrick van Elderen | May, 29, 2021, 02:51
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class SpawnTab extends Widget {

    public static String searchSyntax = "";
    public static int lastLength = 0;
    public static int[] searchResults = new int[1000];
    public static boolean fetchSearchResults;
    public static boolean searchingSpawnTab;
    public static SpawnTabType spawnType = SpawnTabType.INVENTORY;

    public static void processSpawnTab() {
        //Draw checks..
        switch(spawnType) {
         /*   case INVENTORY:
                //Inventory ticks
               // Widget.cache[72007].disabledSprite = Client.spriteCache.get(332);
             //   Widget.cache[72007].enabledSprite = Client.spriteCache.get(332);
//                Widget.cache[72009].enabledSprite = Client.spriteCache.get(333); //Hover

                //Bank ticks
                Widget.cache[72011].disabledSprite = Client.spriteCache.get(335);
                Widget.cache[72011].enabledSprite = Client.spriteCache.get(334); //Hover
                Widget.cache[72013].enabledSprite = Client.spriteCache.get(335);
                break;
            case BANK:
                //Bank ticks
                Widget.cache[72011].disabledSprite = Client.spriteCache.get(332);
                Widget.cache[72011].enabledSprite = Client.spriteCache.get(332);
                Widget.cache[72013].enabledSprite = Client.spriteCache.get(333); //Hover

                //Inventory ticks
                Widget.cache[72007].disabledSprite = Client.spriteCache.get(335);
                Widget.cache[72007].enabledSprite = Client.spriteCache.get(334); //Hover
                Widget.cache[72009].enabledSprite = Client.spriteCache.get(335);
                break;*/
        }




//        Widget.cache[72003].defaultText = textInput;
    }

    public static int[] getResultsArray() {
        return searchSyntax.length() >= 2 ? searchResults : PVP_ALLOWED_SPAWNS;
    }

    static void unpack(AdvancedFont[] tda) {
        com.hazy.cache.graphics.widget.Widget tab = addTabInterface(72000);

        addText(72002, "Preset Tab", tda, 2, 0xFFFFFF, true, true);
        //   addText(72003, "", tda, 1, 0xff8000, false, true);

        //   addHoverButton(72004, 330, 172, 20, "Search", -1, 72005, 1);
        // addHoveredButton(72005, 331, 172, 20, 72006);

        //Inventory spawn
        //    addText(72010, "", tda, 0, 0xFFFFFF, false, true);
        //  addHoverButton(72007, 332, 14, 15, "Select", -1, 72008, 1);
        // addHoveredButton(72008, 333, 14, 15, 72009);

        //Bank spawn
        // addText(72014, "", tda, 0, 0xFFFFFF, false, true);
        ////  addHoverButton(72011, 332, 14, 15, "Select", -1, 72012, 1);
        //  addHoveredButton(72012, 333, 14, 15, 72013);

        addHoverButton(72015, 1830, 79, 30, "Presets", -1, 61016, 1);
        addHoveredButton(72016, 1831, 79, 30, 61017);

        cache[72015].optionType = 0;
        cache[72015].actions = new String[]{"Open Presets", "Load Last Preset"};

        addSpriteLoader(72001, 196);
        tab.totalChildren(5);

        tab.child(0, 72001, 0, 89);
        tab.child(1, 72030, 0, 91);
        tab.child(2, 72002, 95, 1);
        //  tab.child(3, 72004, 10, 25);
        //  tab.child(4, 72005, 10, 25);
        tab.child(3, 72015, 55, 52);
        tab.child(4, 72016, 103, 52);

        //Text area

    }
}
