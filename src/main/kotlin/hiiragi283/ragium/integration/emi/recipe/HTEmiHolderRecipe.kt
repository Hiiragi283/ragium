package hiiragi283.ragium.integration.emi.recipe

import dev.emi.emi.api.recipe.EmiRecipeCategory
import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.integration.emi.HTEmiRecipeCategory
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

/**
 * @see [mekanism.client.recipe_viewer.emi.recipe.MekanismEmiHolderRecipe]
 */
abstract class HTEmiHolderRecipe<RECIPE : Recipe<*>> : HTEmiRecipe<RECIPE> {
    private val holder: RecipeHolder<RECIPE>

    constructor(category: EmiRecipeCategory, holder: RecipeHolder<RECIPE>, bounds: HTBounds) : super(
        category,
        holder.id,
        holder.value,
        bounds,
    ) {
        this.holder = holder
    }

    constructor(
        category: HTEmiRecipeCategory,
        id: ResourceLocation,
        recipe: RECIPE,
    ) : super(category, id, recipe) {
        this.holder = RecipeHolder(id, recipe)
    }

    constructor(
        category: HTEmiRecipeCategory,
        holder: RecipeHolder<RECIPE>,
    ) : this(category, holder, category.bounds)

    override fun getBackingRecipe(): RecipeHolder<*> = holder
}
