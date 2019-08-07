package net.voxelindustry.brokkgui.immediate;

import net.voxelindustry.brokkgui.gui.IGuiWindow;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.RenderTarget;
import net.voxelindustry.brokkgui.panel.PaneBase;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class GuiWindowAdapter implements IGuiWindow {

    @Override
    public void onOpen() {
        throw new NotImplementedException();
    }

    @Override
    public void onClose() {
        throw new NotImplementedException();
    }

    @Override
    public void setWrapper(IBrokkGuiImpl wrapper) {
        throw new NotImplementedException();
    }

    @Override
    public void screenWidth(int width) {
    }

    @Override
    public void screenHeight(int height) {
    }

    @Override
    public fr.ourten.teabeans.value.IProperty<Integer> screenWidthProperty() {
        throw new NotImplementedException();
    }

    @Override
    public fr.ourten.teabeans.value.IProperty<Integer> screenHeightProperty() {
        throw new NotImplementedException();
    }

    @Override
    public void render(int mouseX, int mouseY, RenderTarget target, RenderPass... pass) {
    }

    @Override
    public void renderLast(int mouseX, int mouseY) {
    }

    @Override
    public void tick() {
    }

    @Override
    public void initGui() {
    }

    @Override
    public IBrokkGuiImpl wrapper() {
        throw new NotImplementedException();
    }

    @Override
    public PaneBase getMainPanel() {
        throw new NotImplementedException();
    }

    @Override
    public boolean doesOccludePoint(int mouseX, int mouseY) {
        return false;
    }

    @Override
    public void onKeyPressed(int key) {
    }

    @Override
    public void onKeyTyped(char c, int key) {
    }

    @Override
    public void onKeyReleased(int key) {
    }

    @Override
    public void onClick(int mouseX, int mouseY, int key) {
    }

    @Override
    public void onClickDrag(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    }

    @Override
    public void onClickStop(int mouseX, int mouseY, int state) {

    }

    @Override
    public void handleMouseScroll(double scrolled) {
    }

    @Override
    public void open() {

    }

    @Override
    public void close() {
        throw new NotImplementedException();
    }

    @Override
    public float xRelativePos() {
        throw new NotImplementedException();
    }

    @Override
    public float yRelativePos() {
        throw new NotImplementedException();
    }

    @Override
    public fr.ourten.teabeans.value.BaseProperty<Float> xRelativePosProperty() {
        throw new NotImplementedException();
    }

    @Override
    public fr.ourten.teabeans.value.BaseProperty<Float> yRelativePosProperty() {
        throw new NotImplementedException();
    }

    @Override
    public float width() {
        return 0;
    }

    @Override
    public float height() {
        throw new NotImplementedException();
    }

    @Override
    public <T extends HermodEvent> void addEventHandler(EventType<T> type, EventHandler<? super T> handler) {
        throw new NotImplementedException();
    }

    @Override
    public <T extends HermodEvent> void removeEventHandler(EventType<T> type, EventHandler<T> handler) {
        throw new NotImplementedException();
    }

    @Override
    public void dispatchEventRedirect(EventType<? extends HermodEvent> type, HermodEvent event) {
        throw new NotImplementedException();
    }

    @Override
    public void dispatchEvent(EventType<? extends HermodEvent> type, HermodEvent event) {
        throw new NotImplementedException();
    }
}
