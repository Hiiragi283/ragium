package hiiragi283.ragium.common.recipe.special

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.registry.HTSimpleFluidHolderLike
import hiiragi283.ragium.api.recipe.HTTankInteractingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil

data class HTBucketInteractingRecipe(val fluid: HTSimpleFluidHolderLike) : HTTankInteractingRecipe {
    override val amount: Int = HTConst.DEFAULT_FLUID_AMOUNT
    private val ingredient: HTFluidIngredient = HTIngredientCreator.create(fluid.get(), amount)

    override fun canEmptyContainer(container: ItemStack): Boolean = FluidUtil
        .getFluidContained(container)
        .map { stack: FluidStack ->
            ingredient.test(stack) && FluidUtil.getFilledBucket(stack).`is`(container.item)
        }.orElse(false)

    override fun emptyContainer(container: ItemStack): Pair<ItemStack, FluidStack> =
        container.craftingRemainingItem to FluidUtil.getFluidContained(container).orElse(FluidStack.EMPTY)

    override fun canFillContainer(container: ItemStack, fluidStack: FluidStack): Boolean =
        container.`is`(Tags.Items.BUCKETS_EMPTY) && ingredient.test(fluidStack)

    override fun fillContainer(container: ItemStack, fluidStack: FluidStack): ItemStack = FluidUtil.getFilledBucket(fluidStack)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BUCKET_INTERACTION
}
