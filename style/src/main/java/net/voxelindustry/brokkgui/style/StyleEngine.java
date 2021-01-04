package net.voxelindustry.brokkgui.style;

import net.voxelindustry.brokkgui.component.ComponentEngine;
import net.voxelindustry.brokkgui.component.impl.Paint;
import net.voxelindustry.brokkgui.component.impl.PaintStyle;
import net.voxelindustry.brokkgui.component.impl.TextComponentStyle;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.Resource;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.sprite.RandomSpriteRotation;
import net.voxelindustry.brokkgui.sprite.SpriteRepeat;
import net.voxelindustry.brokkgui.sprite.Texture;
import net.voxelindustry.brokkgui.style.adapter.StyleTranslator;
import net.voxelindustry.brokkgui.style.adapter.translator.BackgroundRepeatStyleTranslator;
import net.voxelindustry.brokkgui.style.adapter.translator.ColorStyleTranslator;
import net.voxelindustry.brokkgui.style.adapter.translator.RandomSpriteRotationStyleTranslator;
import net.voxelindustry.brokkgui.style.adapter.translator.RectAlignmentStyleTranslator;
import net.voxelindustry.brokkgui.style.adapter.translator.RectBoxTranslator;
import net.voxelindustry.brokkgui.style.adapter.translator.ResourceStyleTranslator;
import net.voxelindustry.brokkgui.style.adapter.translator.TextureStyleTranslator;
import net.voxelindustry.brokkgui.style.tree.StyleList;
import net.voxelindustry.brokkgui.text.TextComponent;

import java.util.function.Supplier;

public class StyleEngine
{
    private static StyleEngine instance;

    public static StyleEngine getInstance()
    {
        if (instance == null)
            instance = new StyleEngine();
        return instance;
    }

    private StyleEngine()
    {

    }

    public static void refreshHierarchy(Transform transform)
    {
        transform.element().ifHas(StyleComponent.class, StyleComponent::refresh);
    }

    public static void setStyleSupplierHierarchy(Transform transform, Supplier<StyleList> styleSupplier)
    {
        transform.element().ifHas(StyleComponent.class, style -> style.setStyleSupplier(styleSupplier));
    }

    public void start()
    {
        ComponentEngine.instance().addComponentInject(StyleComponent.class);

        ComponentEngine.instance().addComponentOverride(Paint.class, PaintStyle.class);
        ComponentEngine.instance().addComponentOverride(TextComponent.class, TextComponentStyle.class);

        StyleTranslator.getInstance().registerTranslator(Color.class, new ColorStyleTranslator());
        StyleTranslator.getInstance().registerTranslator(Texture.class, new TextureStyleTranslator());

        StyleTranslator.getInstance().registerTranslator(RectBox.class, new RectBoxTranslator());

        StyleTranslator.getInstance().registerTranslator(SpriteRepeat.class, new BackgroundRepeatStyleTranslator());

        StyleTranslator.getInstance().registerTranslator(Resource.class, new ResourceStyleTranslator());

        StyleTranslator.getInstance().registerTranslator(RandomSpriteRotation.class, new RandomSpriteRotationStyleTranslator());

        StyleTranslator.getInstance().registerTranslator(RectAlignment.class, new RectAlignmentStyleTranslator());
    }
}
