package hiiragi283.ragium.common.recipe

import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.recipe.base.HTItemToItemAndFluidRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.result.HTItemAndFluidResult
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

@JvmRecord
data class RTPotionBottleDrainingRecipe(val bottleType: HTBottleType, override val progressData: HTProgressData) :
    HTItemToItemAndFluidRecipe,
    HTProgressRecipe.Simple<SingleRecipeInput> {
    constructor(bottleType: HTBottleType) : this(bottleType, HTProgressData.time(60))

    override fun test(input: ItemInstance): Boolean =
        bottleType.filledItem.isOf(input) && HTPotionHelper.hasAnyEffect(input)

    override fun apply(input: ItemInstance): HTItemAndFluidResult = HTItemAndFluidResult(
        bottleType.emptyItem.toStack(),
        HTPotionHelper.createFluid(input, HTPotionHelper.BOTTLE_AMOUNT)
    )

    override fun getMatchingStack(input: ItemInstance): ItemInstance = when {
        test(input) -> HTIngredientHelper.copyWithCount(input, 1)
        else -> ItemStack.EMPTY
    }
}
