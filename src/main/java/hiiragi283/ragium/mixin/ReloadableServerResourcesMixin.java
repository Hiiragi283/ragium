package hiiragi283.ragium.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import hiiragi283.ragium.api.recipe.HTRegisterRuntimeRecipeEvent;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(ReloadableServerResources.class)
public abstract class ReloadableServerResourcesMixin {
    @Unique
    private ReloadableServerResources ragium$self() {
        return (ReloadableServerResources) (Object) this;
    }
    
    @Inject(method = "updateRegistryTags()V", at = @At("TAIL"))
    private void ragium$addRuntimeRecipes(CallbackInfo ci) {
        RegistryAccess.Frozen registryAccess = ragium$self().fullRegistries().get();
        RecipeManager recipes = ragium$self().getRecipeManager();
        RecipeManagerAccessor accessor = (RecipeManagerAccessor) recipes;
        
        Multimap<RecipeType<?>, RecipeHolder<?>> byType = HashMultimap.create(accessor.getByType());
        Map<ResourceLocation, RecipeHolder<?>> byName = new HashMap<>(accessor.getByName());
        
        var event = new HTRegisterRuntimeRecipeEvent(registryAccess, recipes, (@NotNull RecipeHolder<?> holder) -> {
            byType.put(holder.value().getType(), holder);
            byName.put(holder.id(), holder);
        });
        NeoForge.EVENT_BUS.post(event);

        accessor.setByType(byType);
        accessor.setByName(byName);
    }
}
