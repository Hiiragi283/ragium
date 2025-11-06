package hiiragi283.ragium.client.integration.emi.type

import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.recipe.manager.HTRecipeType
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.ItemLike

/**
 * @see mekanism.client.recipe_viewer.type.RVRecipeTypeWrapper
 */
class HTRegistryRecipeViewerType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(
    private val id: ResourceLocation,
    private val recipeType: HTRecipeType<INPUT, RECIPE>,
    override val iconStack: ItemStack,
    override val icon: ResourceLocation?,
    private val bounds: HTBounds,
    override val workStations: List<ItemLike>,
) : HTRecipeViewerType<RECIPE>,
    HTRecipeType<INPUT, RECIPE> by recipeType {
    constructor(
        id: ResourceLocation,
        recipeType: HTRecipeType<INPUT, RECIPE>,
        iconStack: ItemStack,
        icon: ResourceLocation?,
        bounds: HTBounds,
        vararg workStations: ItemLike,
    ) : this(id, recipeType, iconStack, icon, bounds, listOf(*workStations))

    constructor(
        recipeType: HTDeferredRecipeType<INPUT, RECIPE>,
        iconStack: ItemStack,
        icon: ResourceLocation?,
        bounds: HTBounds,
        vararg workStations: ItemLike,
    ) : this(recipeType.id, recipeType, iconStack, icon, bounds, *workStations)

    override fun getBounds(): HTBounds = bounds

    override fun getId(): ResourceLocation = id
}
