package hiiragi283.ragium.mixin;

import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RecipeManager.class)
public interface RecipeManagerAccessor {
    @Accessor
    Multimap<RecipeType<?>, RecipeHolder<?>> getByType();

    @Accessor
    void setByType(Multimap<RecipeType<?>, RecipeHolder<?>> byType);

    @Accessor
    Map<ResourceLocation, RecipeHolder<?>> getByName();

    @Accessor
    void setByName(Map<ResourceLocation, RecipeHolder<?>> byName);
}
