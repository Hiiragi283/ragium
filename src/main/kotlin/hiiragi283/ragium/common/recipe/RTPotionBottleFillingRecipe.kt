package hiiragi283.ragium.common.recipe

import hiiragi283.lib.item.ItemStack
import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
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
        !first.`is`(bottleType.emptyItem) -> false
        !HTPotionHelper.hasAnyEffect(second) -> false
        else -> second.amount() >= HTPotionHelper.BOTTLE_AMOUNT
    }

    override fun apply(first: ItemInstance, second: FluidInstance): ItemStack =
        ItemStack(bottleType.filledItem, 1, HTPotionHelper.createPotionPatch(second))

    override fun getRequiredAmount(first: ItemInstance, second: FluidInstance): Pair<Int, Int> = when {
        test(first, second) -> 1 to HTPotionHelper.BOTTLE_AMOUNT
        else -> 0 to 0
    }
}
