package hiiragi283.ragium.client.integration.emi.category

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.EmiRecipeSorting
import dev.emi.emi.api.render.EmiRenderable
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.api.text.HTHasText
import hiiragi283.ragium.client.integration.emi.toEmi
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike
import java.util.Comparator

open class HTEmiRecipeCategory(
    val bounds: HTBounds,
    private val hasText: HTHasText,
    val workStations: List<EmiStack>,
    id: ResourceLocation,
    icon: EmiRenderable,
    simplified: EmiRenderable = icon,
    sorter: Comparator<EmiRecipe> = EmiRecipeSorting.compareOutputThenInput(),
) : EmiRecipeCategory(id, icon, simplified, sorter) {
    companion object {
        @JvmStatic
        fun create(
            bounds: HTBounds,
            hasText: HTHasText,
            id: ResourceLocation,
            vararg workStations: ItemLike,
            sorter: Comparator<EmiRecipe> = EmiRecipeSorting.compareOutputThenInput(),
        ): HTEmiRecipeCategory = HTEmiRecipeCategory(
            bounds,
            hasText,
            workStations.map(ItemLike::toEmi),
            id,
            workStations[0].toEmi(),
            sorter = sorter,
        )

        @JvmStatic
        fun create(bounds: HTBounds, recipeType: HTDeferredRecipeType<*, *>, vararg workStations: ItemLike): HTEmiRecipeCategory =
            create(bounds, recipeType, recipeType.id, *workStations)
    }

    override fun getName(): Component = hasText.getText()
}
