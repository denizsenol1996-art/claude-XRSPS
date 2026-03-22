package com.hazy.engine.impl;

import com.hazy.Client;
import com.hazy.model.content.Keybinding;
import com.hazy.util.ConfigUtility;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static com.hazy.engine.GameEngine.addSyncTask;


public final class KeyHandler implements KeyListener, FocusListener {

    public static KeyHandler instance;

    static  {
        instance = new KeyHandler();
    }

    public final int[] keyArray = new int[128];
    private final int[] charQueue = new int[128];
    private int readIndex;
    private int writeIndex;
    int keyPressed = 0;
    public static volatile int idleCycles = 0;

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public final void focusLost(FocusEvent var1) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
        Client.instance.getCallbacks().keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent event) {
        Client.instance.getCallbacks().keyPressed(event);
        idleCycles = 0;
        int i = event.getKeyCode();


        int keyChar = event.getKeyChar();
        int keycode = event.getKeyCode();

        if (Keybinding.isBound(keycode)) {
            return;
        }

        if (keycode == KeyEvent.VK_ESCAPE && Client.instance.settings[ConfigUtility.ESC_CLOSE_ID] == 1) {
            //Close any open interfaces.
            if (Client.loggedIn && Client.widget_overlay_id != -1) {
                if(Client.widget_overlay_id == 48700) {
                    Client.tabInterfaceIDs[3] = 3213;
                }
                if (Client.widget_overlay_id == 16200) {
                    //Queue the task to run on the main Client thread to prevent a race condition.
                    //We use addSyncTask to send the packet from the main Client thread to prevent racing.
                    addSyncTask(() -> {
                        Client.instance.packetSender.sendButtonClick(16202);
                    });
                }
                Client.instance.clearTopInterfaces();
                return;
            }

            //Close the Client settings menu if it is open.
            if (Client.loggedIn && Client.tabInterfaceIDs[Client.sidebarId] == 50290) {
                //Queue the task to run on the main Client thread to prevent a race condition.
                //We use addSyncTask to send the packet from the main Client thread to prevent racing.
                addSyncTask(() -> {
                    Client.instance.packetSender.sendButtonClick(50293);
                });
                return;
            }
        }

        if (keycode == KeyEvent.VK_SHIFT) {
            Client.isShiftPressed = true;
        }

        if (keycode == KeyEvent.VK_CONTROL) {
            Client.isCtrlPressed = true;
        }

        if (keycode == KeyEvent.VK_B && Client.isCtrlPressed) {
            Client.instance.packetSender.sendCommand("bank");
        }

        if (keycode == KeyEvent.VK_T && Client.isCtrlPressed) {
            Client.instance.packetSender.sendCommand("ctrlt");
        }

        if (keyChar < 30)
            keyChar = 0;
        if (i == 37)
            keyChar = 1;
        if (i == 39)
            keyChar = 2;
        if (i == 38)
            keyChar = 3;
        if (i == 40)
            keyChar = 4;
        if (i == 17)
            keyChar = 5;
        if (i == 8)
            keyChar = 8;
        if (i == 127)
            keyChar = 8;
        if (i == 9)
            keyChar = 9;
        if (i == 10)
            keyChar = 10;
        if (i >= 112 && i <= 123)
            keyChar = (1008 + i) - 112;
        if (i == 36)
            keyChar = 1000;
        if (i == 35)
            keyChar = 1001;
        if (i == 33)
            keyChar = 1002;
        if (i == 34)
            keyChar = 1003;
        if (keyChar > 0 && keyChar < 128)
            keyArray[keyChar] = 1;
        if (keyChar > 4) {
            charQueue[writeIndex] = keyChar;
            writeIndex = writeIndex + 1 & 0x7f;
        }
    }

    public int readChar() {
        int k = -1;
        if (writeIndex != readIndex) {
            k = charQueue[readIndex];
            readIndex = readIndex + 1 & 0x7f;
        }
        return k;
    }


    @Override
    public void keyReleased(KeyEvent event) {
        Client.instance.getCallbacks().keyReleased(event);
        idleCycles = 0;
        int i = event.getKeyCode();
        char c = event.getKeyChar();

        if (i == KeyEvent.VK_SHIFT) {
            Client.isShiftPressed = false;
        }

        if (i == KeyEvent.VK_CONTROL) {
            Client.isCtrlPressed = false;
            Client.instance.prayerGrabbed = null;
        }

        if (c < '\036')
            c = '\0';
        if (i == 37)
            c = '\001';
        if (i == 39)
            c = '\002';
        if (i == 38)
            c = '\003';
        if (i == 40)
            c = '\004';
        if (i == 17)
            c = '\005';
        if (i == 8)
            c = '\b';
        if (i == 127)
            c = '\b';
        if (i == 9)
            c = '\t';
        if (i == 10)
            c = '\n';
        if (c > 0 && c < '\200')
            keyArray[c] = 0;
    }


}
