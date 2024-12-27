package hiiragi283.ragium.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import hiiragi283.ragium.api.RagiumAPI;
import hiiragi283.ragium.api.RagiumPlugin;
import hiiragi283.ragium.api.material.HTMaterialKey;
import hiiragi283.ragium.api.material.HTMaterialRegistry;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

    @Shadow
    private Multimap<RecipeType<?>, RecipeEntry<?>> recipesByType;

    @Shadow
    private Map<Identifier, RecipeEntry<?>> recipesById;

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At("TAIL"))
    private void ragium$injectMaterialRecipe(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
        var map1 = HashMultimap.create(recipesByType);
        var map2 = new LinkedHashMap<>(recipesById);
        RecipeExporter exporter = new RecipeExporter() {
            @Override
            public void accept(Identifier recipeId, Recipe<?> recipe, @Nullable AdvancementEntry advancement) {
                // RagiumAPI.getLOGGER().info("Recipe: {} was registered!", recipeId);
                RecipeEntry<?> entry = new RecipeEntry<>(recipeId, recipe);
                map1.put(recipe.getType(), entry);
                if (map2.put(recipeId, entry) != null) {
                    RagiumAPI.getLOGGER().warn("Recipe: {} was replaced!", recipeId);
                }
            }

            @Override
            public Advancement.Builder getAdvancementBuilder() {
                return new Advancement.Builder();
            }
        };
        RagiumAPI.getPlugins().forEach(plugin -> {
            plugin.registerRuntimeRecipe(exporter);
            RagiumAPI.getInstance().getMaterialRegistry().getEntryMap().forEach((@NotNull HTMaterialKey key, HTMaterialRegistry.@NotNull Entry entry) -> plugin.registerRuntimeMaterialRecipes(exporter, key, entry, new RagiumPlugin.RecipeHelper()));
        });
        recipesByType = map1;
        recipesById = map2;
        RagiumAPI.getLOGGER().info("Registered runtime recipes!");
    }
}
