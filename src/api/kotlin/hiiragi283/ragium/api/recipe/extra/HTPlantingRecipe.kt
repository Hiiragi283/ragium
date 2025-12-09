package hiiragi283.ragium.api.recipe.extra

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTPlantingRecipe(
    val seed: HTKeyOrTagEntry<Item>,
    val soil: HTItemIngredient,
    val fluid: HTFluidIngredient,
    val crop: HTItemResult,
) : HTExtraItemRecipe {
    val seedResult = HTItemResult(seed, 1, DataComponentPatch.EMPTY)

    override fun assembleExtraItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        seedResult.getStackOrNull(provider)

    override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? = crop.getStackOrNull(provider)

    override fun matches(input: HTRecipeInput, level: Level): Boolean {
        val seedItem: ImmutableItemStack = input.item(0) ?: return false
        val bool1: Boolean = seed.unwrap().map(seedItem::isOf, seedItem::isOf)
        val bool2: Boolean = input.testCatalyst(1, soil)
        val bool3: Boolean = input.testFluid(0, fluid)
        return bool1 && bool2 && bool3
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumPlatform.INSTANCE.getPlantingRecipeSerializer()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PLANTING.get()
}
