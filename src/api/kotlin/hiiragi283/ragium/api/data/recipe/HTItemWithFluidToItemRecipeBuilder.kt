package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTItemWithFluidToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTInfusingRecipe
import hiiragi283.ragium.api.recipe.base.HTSolidifyingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import java.util.*

class HTItemWithFluidToItemRecipeBuilder<R : HTItemWithFluidToItemRecipe>(
    prefix: String,
    private val factory: Factory<R>,
    private val itemIngredient: Optional<HTItemIngredient>,
    private val fluidIngredient: Optional<HTFluidIngredient>,
    private val result: HTItemResult,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun solidifying(
            itemIngredient: HTItemIngredient?,
            fluidIngredient: HTFluidIngredient,
            result: HTItemResult,
        ): HTItemWithFluidToItemRecipeBuilder<HTSolidifyingRecipe> = HTItemWithFluidToItemRecipeBuilder(
            RagiumConst.SOLIDIFYING,
            ::HTSolidifyingRecipe,
            Optional.ofNullable(itemIngredient),
            Optional.of(fluidIngredient),
            result,
        )

        @JvmStatic
        fun infusing(
            itemIngredient: HTItemIngredient,
            fluidIngredient: HTFluidIngredient,
            result: HTItemResult,
        ): HTItemWithFluidToItemRecipeBuilder<HTInfusingRecipe> = HTItemWithFluidToItemRecipeBuilder(
            RagiumConst.INFUSING,
            ::HTInfusingRecipe,
            Optional.of(itemIngredient),
            Optional.of(fluidIngredient),
            result,
        )
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): Recipe<*> = factory.create(itemIngredient, fluidIngredient, result)

    fun interface Factory<R : HTItemWithFluidToItemRecipe> {
        fun create(itemIngredient: Optional<HTItemIngredient>, fluidIngredient: Optional<HTFluidIngredient>, result: HTItemResult): R
    }
}
