package hiiragi283.ragium.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import hiiragi283.ragium.event.RagiumRuntimeRecipeProvider;
import java.util.Collection;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {
    @Shadow
    @Final
    private HolderLookup.Provider registries;

    @WrapOperation(
            method =
                    "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/world/item/crafting/RecipeMap;",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/item/crafting/RecipeMap;create(Ljava/lang/Iterable;)Lnet/minecraft/world/item/crafting/RecipeMap;"))
    private RecipeMap ragium$prepare(Iterable<RecipeHolder<?>> recipes, Operation<RecipeMap> original) {
        Collection<RecipeHolder<?>> recipes1 = (Collection<RecipeHolder<?>>) recipes;
        RagiumRuntimeRecipeProvider.addRecipes(
                (id, recipe, _) -> recipes1.add(new RecipeHolder<>(id, recipe)), registries);
        return original.call(recipes1);
    }
}
