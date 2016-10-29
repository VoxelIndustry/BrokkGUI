package fr.ourten.brokkgui.skin;

import fr.ourten.brokkgui.behavior.GuiBehaviorBase;
import fr.ourten.brokkgui.data.EAlignment;
import fr.ourten.brokkgui.element.GuiProgressBar;
import fr.ourten.brokkgui.internal.IGuiRenderer;
import fr.ourten.brokkgui.paint.EGuiRenderPass;
import fr.ourten.brokkgui.shape.Rectangle;
import fr.ourten.teabeans.binding.BaseBinding;

public class GuiProgressBarSkin<C extends GuiProgressBar, B extends GuiBehaviorBase<C>> extends GuiLabeledSkinBase<C, B>
{
    private final Rectangle progressBar;
    
    public GuiProgressBarSkin(C model, B behaviour)
    {
        super(model, behaviour);
        
        this.progressBar = new Rectangle(model.getxPos(), model.getyPos(), model.getWidth(), model.getHeight());
        
        this.progressBar.getxPosProperty().bind(model.getxPosProperty());
        this.progressBar.getyPosProperty().bind(model.getyPosProperty());
        
        this.progressBar.getWidthProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(model.getProgressProperty());
            }
            
            @Override
            public Float computeValue()
            {
                return model.getWidth() * model.getProgress();
            }
        });
        this.progressBar.getColorProperty().bind(model.getColorProperty());
        this.getText().getxPosProperty().bind(model.getxPosProperty());
        this.getText().getyPosProperty().bind(model.getyPosProperty());
        this.getText().getTextAlignmentProperty().setValue(EAlignment.MIDDLE_DOWN);
        
        this.getText().getTextProperty().bind(new BaseBinding<String>()
        {
            {
                super.bind(model.getProgressProperty());
            }
            
            @Override
            public String computeValue()
            {
                return model.getProgress() + "";
            }
        });
        
        this.getText().getzLevelProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(model.getzLevelProperty());
            }
            
            @Override
            public Float computeValue()
            {
                return model.getzLevel() + 10;
            }
        });
        
    }
    
    @Override
    public void render(final EGuiRenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);
        this.progressBar.renderNode(renderer, pass, mouseX, mouseY);
        // XXX temp : zLevel not working
        this.getText().renderNode(renderer, pass, mouseX, mouseY);
    }
    
    public Rectangle getProgressBar()
    {
        return this.progressBar;
    }
    
}
