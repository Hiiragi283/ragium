package hiiragi283.ragium.client.integration.emi.category

import dev.emi.emi.api.recipe.EmiRecipeSorting
import dev.emi.emi.api.render.EmiRenderable
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.recipe.HTRecipeType
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.client.integration.emi.toEmi
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.ItemLike

class HTRegistryEmiRecipeCategory<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(
    bounds: HTBounds,
    private val recipeType: HTDeferredRecipeType<INPUT, RECIPE>,
    workStations: List<EmiStack>,
    icon: EmiRenderable,
) : HTEmiRecipeCategory(bounds, recipeType, workStations, recipeType.id, icon, icon, EmiRecipeSorting.compareOutputThenInput()),
    HTRecipeType<INPUT, RECIPE> by recipeType {
    constructor(
        bounds: HTBounds,
        recipeType: HTDeferredRecipeType<INPUT, RECIPE>,
        vararg workStations: ItemLike,
    ) : this(bounds, recipeType, workStations.map(ItemLike::toEmi), workStations[0].toEmi())
}
