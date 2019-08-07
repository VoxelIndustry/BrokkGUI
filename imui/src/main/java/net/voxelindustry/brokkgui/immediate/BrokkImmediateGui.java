package net.voxelindustry.brokkgui.immediate;

import net.voxelindustry.brokkgui.immediate.element.ElementsDelegate;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.RenderTarget;

public abstract class BrokkImmediateGui extends GuiWindowAdapter implements ElementsDelegate {

    private boolean[] clickedKey = new boolean[3];
    public abstract void render();
    private IBrokkGuiImpl wrapper;

    @Override
    public void setWrapper(IBrokkGuiImpl wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public IBrokkGuiImpl wrapper() {
        return this.wrapper;
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose() {

    }


    public void render(int mouseX, int mouseY, RenderTarget target, RenderPass... passes) {
        if(target.equals(RenderTarget.MAIN)) {
            render();
        }
    }



    @Override
    public void tick() {

    }

    @Override
    public void initGui() {

    }

    @Override
    public boolean isMouseClick(int key) {
        if(key < 0 || key > clickedKey.length)
            return false;
        return clickedKey[key];
    }

    @Override
    public void onClick(int mouseX, int mouseY, int key) {
        if(key >= 0 && key < clickedKey.length)
            clickedKey[key] = true;
    }

    @Override
    public void onClickDrag(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

    }

    @Override
    public void onClickStop(int mouseX, int mouseY, int state) {
        if(state >= 0 && state < clickedKey.length)
            clickedKey[state] = true;
    }



    @Override
    public void open() {

    }

}
