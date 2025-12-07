package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.extra.HTPlantingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe

class HTPlantingRecipeBuilder(
    private val seed: HTKeyOrTagEntry<Item>,
    private val soil: HTItemIngredient,
    private val fluid: HTFluidIngredient,
    private val crop: HTItemResult,
) : HTRecipeBuilder<HTPlantingRecipeBuilder>(RagiumConst.PLANTING) {
    companion object {
        @JvmStatic
        fun create(
            seed: HTItemHolderLike,
            soil: HTItemIngredient,
            fluid: HTFluidIngredient,
            crop: HTItemResult,
        ): HTPlantingRecipeBuilder =
            HTPlantingRecipeBuilder(HTKeyOrTagHelper.INSTANCE.create(Registries.ITEM, seed.getId()), soil, fluid, crop)

        @JvmStatic
        fun create(
            seed: TagKey<Item>,
            soil: HTItemIngredient,
            fluid: HTFluidIngredient,
            crop: HTItemResult,
        ): HTPlantingRecipeBuilder = HTPlantingRecipeBuilder(HTKeyOrTagHelper.INSTANCE.create(seed), soil, fluid, crop)
    }

    override fun getPrimalId(): ResourceLocation = crop.id

    override fun createRecipe(): Recipe<*> = HTPlantingRecipe(seed, soil, fluid, crop)
}
