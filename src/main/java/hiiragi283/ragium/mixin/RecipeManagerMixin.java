package hiiragi283.ragium.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.mojang.logging.LogUtils;
import hiiragi283.ragium.api.event.HTRecipeLoadingEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

    @Unique
    private static final Logger ragium$logger = LogUtils.getLogger();

    @Inject(method = "lambda$apply$0", at = @At("HEAD"), cancellable = true)
    private static void ragium$apply(
            ResourceLocation resourcelocation,
            ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> builder,
            ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> builder1,
            WithConditions<Recipe<?>> r,
            CallbackInfo ci
    ) {
        var event = new HTRecipeLoadingEvent(resourcelocation, r);
        NeoForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            ragium$logger.warn("The recipe {} was disabled!", resourcelocation);
            ci.cancel();
        }
    }
}
