package hiiragi283.ragium.integration.emi.type

import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.manager.HTRecipeGetter
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.api.text.HTHasTranslationKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.ItemLike

/**
 * @see [mekanism.client.recipe_viewer.type.RVRecipeTypeWrapper]
 */
class HTRegistryRecipeViewerType<INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>>(
    private val id: ResourceLocation,
    private val getter: HTRecipeGetter<INPUT, RECIPE>,
    translationKey: HTHasTranslationKey,
    override val iconStack: ItemStack,
    override val icon: ResourceLocation?,
    private val bounds: HTBounds,
    override val workStations: List<ItemLike>,
) : HTRecipeViewerType<RECIPE>,
    HTRecipeGetter<INPUT, RECIPE> by getter,
    HTHasTranslationKey by translationKey {
    constructor(
        recipeType: HTDeferredRecipeType<INPUT, RECIPE>,
        iconStack: ItemStack,
        icon: ResourceLocation?,
        bounds: HTBounds,
        vararg workStations: ItemLike,
    ) : this(recipeType.id, recipeType, recipeType, iconStack, icon, bounds, listOf(*workStations))

    override fun getBounds(): HTBounds = bounds

    override fun getId(): ResourceLocation = id
}
