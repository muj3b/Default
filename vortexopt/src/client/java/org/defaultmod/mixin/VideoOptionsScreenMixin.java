package org.defaultmod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.defaultmod.client.ui.DefaultSettingsScreen;

@Mixin(VideoOptionsScreen.class)
public abstract class VideoOptionsScreenMixin extends Screen {
    protected VideoOptionsScreenMixin(Text title) { super(title); }

    @Inject(method = "init()V", at = @At("TAIL"), require = 0)
    private void vortexopt$addSettingsButton(CallbackInfo ci) {
        int w = 150, h = 20;
        int x = this.width - w - 10;
        int y = 10;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Default Settings"), b -> {
            MinecraftClient.getInstance().setScreen(new DefaultSettingsScreen(this));
        }).dimensions(x, y, w, h).build());
    }
}
