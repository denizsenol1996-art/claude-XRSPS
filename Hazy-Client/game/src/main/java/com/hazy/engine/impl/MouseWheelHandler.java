package com.hazy.engine.impl;

import com.hazy.Client;
import com.hazy.cache.graphics.widget.Widget;
import com.hazy.cache.graphics.widget.impl.OptionTabWidget;
import com.hazy.util.ConfigUtility;

import java.awt.*;
import java.awt.event.*;


public class MouseWheelHandler implements MouseWheelListener {

    public static int mouseRotation;
    public static boolean mouseWheelDown;
    public static int mouseWheelX;
    public static int mouseWheelY;

    public synchronized int useRotation() {
        int rotation = this.mouseRotation;
        this.mouseRotation = 0;
        return rotation;
    }

    public void addTo(Component component) {
        component.addMouseWheelListener(this);
    }

    public void removeFrom(Component component) {
        component.removeMouseWheelListener(this);
    }

    public boolean canZoom = true;
    private boolean shiftTeleport = false;

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int rotation = e.getWheelRotation();
        handleInterfaceScrolling(e);
        if (MouseHandler.mouseX > 0 && MouseHandler.mouseX < 512 && MouseHandler.mouseY > Client.canvasHeight - 165 && MouseHandler.mouseX < Client.canvasHeight - 25) {
            int scrollPos = Client.chatScrollAmount;
            scrollPos -= rotation * 30;
       
            if (scrollPos < 0)
                scrollPos = 0;
            if (scrollPos > Client.chatScrollHeight - 110)
                scrollPos = Client.chatScrollHeight - 110;
            if (Client.chatScrollAmount != scrollPos) {
                Client.chatScrollAmount = scrollPos;
                Client.update_chat_producer = true;
            }
        } else if (Client.loggedIn) {
            // Admin shift scrollwheel height changing. Do not send packets from the client to the server here, send packets from the main do while loop inside run method instead.
            if ((Client.instance.getMyPrivilege() >= 2 && Client.instance.getMyPrivilege() <= 4) && Client.isShiftPressed) {
                this.mouseRotation = rotation;
                shiftTeleport = true;
            } else {
                shiftTeleport = false;
            }

            /** ZOOMING **/
            boolean zoom = !Client.instance.isResized() ? (MouseHandler.mouseX < 512) : (MouseHandler.mouseX < Client.canvasWidth - 200);
            if (zoom && Client.widget_overlay_id == -1 && Client.instance.settings[ConfigUtility.ZOOM_TOGGLE_ID] == 0) {
                int zoom_in = !Client.instance.isResized() ? 195 : 240;
                int zoom_out = !Client.instance.isResized() ? 1105 : 1220;

                if (e.getWheelRotation() != -1) {
                    if (Client.cameraZoom > zoom_in) {
                        Client.cameraZoom -= 45;
                    }
                } else {
                    if (Client.cameraZoom < zoom_out) {
                        Client.cameraZoom += 45;
                    }
                }

                Widget.cache[OptionTabWidget.ZOOM_SLIDER].slider.setValue(Client.cameraZoom);
                Client.instance.setting.save();
            }
            Client.update_chat_producer = true;
        }
    }

    public void handleInterfaceScrolling(MouseWheelEvent event) {
        int rotation = event.getWheelRotation();
        int tabInterfaceId = Client.tabInterfaceIDs[Client.instance.sidebarId];
        if (tabInterfaceId != -1) {
            handleScrolling(rotation, tabInterfaceId, !Client.instance.isResized() ? Client.canvasWidth - 218
                : (!Client.instance.isResized() ? 28
                : Client.canvasWidth - 197), !Client.instance.isResized() ? Client.canvasHeight - 298
                : (!Client.instance.isResized() ? 37
                : Client.canvasHeight
                - (Client.canvasWidth >= 1000 ? 37 : 74) - 267));
        }
        if (Client.instance.widget_overlay_id != -1) {
            handleScrolling(rotation, Client.instance.widget_overlay_id, !Client.instance.isResized() ? 4
                : (Client.canvasWidth / 2) - 356, !Client.instance.isResized() ? 4
                : (Client.canvasHeight / 2) - 230);
        }
    }

    private void handleScrolling(int rotation, int interfaceId, int offsetX, int offsetY) {
        try {
            //No widget found
            if(Widget.cache[interfaceId] == null) {
                return;
            }
            Widget widget = Widget.cache[interfaceId];
            for (int index = 0; index < widget.children.length; index++) {
                Widget child = Widget.cache[widget.children[index]];
                if (child != null && child.scrollMax > child.height) {
                    int positionX = widget.child_x[index] + child.x;
                    int positionY = widget.child_y[index] + child.y;
                    int width = child.width;
                    int height = child.height;
                    if (MouseHandler.mouseX >= offsetX + positionX && MouseHandler.mouseY >= offsetY + positionY
                        && MouseHandler.mouseX < offsetX + positionX + width
                        && MouseHandler.mouseY < offsetY + positionY + height) {
                        int newRotation = rotation * 30;
                        if (newRotation > child.scrollMax - child.height - child.scrollPosition) {
                            newRotation = child.scrollMax - child.height - child.scrollPosition;
                        } else if (newRotation < -child.scrollPosition) {
                            newRotation = -child.scrollPosition;
                        }
                        if (Client.instance.getActiveInterfaceType() != 0) {
                            Client.instance.setMouseDragY(Client.instance.getMouseDragY() - newRotation);
                        }
                        child.scrollPosition += newRotation;
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
