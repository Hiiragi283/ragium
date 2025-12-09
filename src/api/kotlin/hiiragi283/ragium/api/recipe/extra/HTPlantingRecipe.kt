package hiiragi283.ragium.api.recipe.extra

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTPlantingRecipe(
    val seed: HTKeyOrTagEntry<Item>,
    val soil: HTItemIngredient,
    val fluid: HTFluidIngredient,
    val crop: HTItemResult,
) : HTExtraItemRecipe<HTMultiRecipeInput> {
    val seedResult = HTItemResult(seed, 1, DataComponentPatch.EMPTY)

    override fun assembleExtraItem(input: HTMultiRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, provider, seedResult)

    override fun test(input: HTMultiRecipeInput): Boolean {
        val seedItem: ItemStack = input.getItem(0)
        val bool1: Boolean = seed.unwrap().map(seedItem.itemHolder::`is`, seedItem::`is`)
        val bool2: Boolean = soil.test(input.getItem(1))
        val bool3: Boolean = fluid.test(input.getFluid(0))
        return bool1 && bool2 && bool3
    }

    override fun assembleItem(input: HTMultiRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, provider, crop)

    override fun isIncomplete(): Boolean {
        val bool1: Boolean = !seed.unwrap().map(BuiltInRegistries.ITEM::containsKey) { true }
        val bool2: Boolean = soil.hasNoMatchingStacks()
        val bool3: Boolean = fluid.hasNoMatchingStacks()
        val bool4: Boolean = crop.hasNoMatchingStack()
        return bool1 || bool2 || bool3 || bool4
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumPlatform.INSTANCE.getPlantingRecipeSerializer()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PLANTING.get()
}
