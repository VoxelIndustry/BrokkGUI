package fr.ourten.brokkgui.skin;

import fr.ourten.brokkgui.behavior.GuiBehaviorProgressBar;
import fr.ourten.brokkgui.data.EAlignment;
import fr.ourten.brokkgui.element.GuiProgressBar;
import fr.ourten.brokkgui.internal.IGuiRenderer;
import fr.ourten.brokkgui.paint.EGuiRenderPass;
import fr.ourten.brokkgui.shape.Rectangle;

public class GuiProgressBarSkin<C extends GuiProgressBar, B extends GuiBehaviorProgressBar<C>> extends GuiLabeledSkinBase<C, B>
{
    private final Rectangle progressBar;
    private final C model;
    
    public GuiProgressBarSkin(C model, B behaviour)
    {
        super(model, behaviour);
        this.model = model;
        
        this.progressBar = new Rectangle(model.getxPos(), model.getyPos(), model.getWidth(), model.getHeight());
        
        this.progressBar.getxPosProperty().bind(model.getxPosProperty());
        this.progressBar.getyPosProperty().bind(model.getyPosProperty());
        this.progressBar.getWidthProperty().bind(model.getProgressProperty());
        this.progressBar.getColorProperty().bind(model.getColorProperty());
        this.getText().getxPosProperty().bind(model.getxPosProperty());
        this.getText().getyPosProperty().bind(model.getyPosProperty());
        this.getText().getTextAlignmentProperty().setValue(EAlignment.MIDDLE_DOWN);
        
        // this.getText().setzLevel(this.progressBar.getzLevel() + 10);
        
    }
    
    @Override
    public void render(final EGuiRenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);
        this.model.getTextProperty().setValue(model.getProgressRatio() + "");
        this.progressBar.renderNode(renderer, pass, mouseX, mouseY);
        this.getText().renderNode(renderer, pass, mouseX, mouseY);
    }
    
    public Rectangle getProgressBar()
    {
        return this.progressBar;
    }
    
}
