package hiiragi283.ragium.common.recipe

import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidInstance

@JvmRecord
data class RTPotionBottleFillingRecipe(val bottleType: HTBottleType, override val progressData: HTProgressData) :
    HTItemAndFluidToItemRecipe,
    HTProgressRecipe.Simple<HTItemAndFluidRecipeInput> {
    constructor(bottleType: HTBottleType) : this(bottleType, HTProgressData.time(60))

    override fun test(first: ItemInstance, second: FluidInstance): Boolean = when {
        !bottleType.emptyItem.isOf(first) -> false
        !HTPotionHelper.hasAnyEffect(second) -> false
        else -> second.amount() >= HTPotionHelper.BOTTLE_AMOUNT
    }

    override fun apply(first: ItemInstance, second: FluidInstance): ItemStack =
        bottleType.filledItem.toStack(patch = HTPotionHelper.createPotionPatch(second))

    override fun getMatchingStack(first: ItemInstance, second: FluidInstance): Pair<ItemInstance, FluidInstance> = Pair(
        HTIngredientHelper.copyWithCount(first, 1),
        HTIngredientHelper.copyWithAmount(second, HTPotionHelper.BOTTLE_AMOUNT)
    )
}
