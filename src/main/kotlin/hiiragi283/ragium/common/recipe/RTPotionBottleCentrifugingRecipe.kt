package hiiragi283.ragium.common.recipe

import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.recipe.base.HTItemSeparatingRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate

@JvmRecord
data class RTPotionBottleCentrifugingRecipe(val bottleType: HTBottleType, override val progressData: HTProgressData) :
    HTItemSeparatingRecipe,
    HTProgressRecipe.Holder {
    companion object {
        @JvmField
        val PROGRESS_DATA: HTProgressData = HTProgressData.time(60)
    }

    constructor(bottleType: HTBottleType) : this(bottleType, PROGRESS_DATA)

    override fun test(input: ItemInstance): Boolean = bottleType.filledItem.isOf(input) && HTPotionHelper.hasAnyEffect(input)

    override fun apply(input: ItemInstance): HTItemSeparatingRecipe.Output = HTItemSeparatingRecipe.Output(
        bottleType.emptyItem.asStackOrEmpty(),
        HTPotionHelper.createFluid(input, HTPotionHelper.BOTTLE_AMOUNT).mapOrElse(FluidStackTemplate::create) { FluidStack.EMPTY },
    )

    override fun getMatchingStack(input: ItemInstance): ItemInstance = when {
        test(input) -> HTIngredientHelper.copyWithCount(input, 1)
        else -> ItemStack.EMPTY
    }

    override fun getProgressData(input: ItemInstance): HTProgressData = progressData
}
