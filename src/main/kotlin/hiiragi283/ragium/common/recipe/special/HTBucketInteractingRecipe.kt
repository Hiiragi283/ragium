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
    private val ingredient: HTFluidIngredient = HTIngredientCreator.create(fluid.get(), HTConst.DEFAULT_FLUID_AMOUNT)

    override fun emptyContainer(container: ItemStack): Pair<ItemStack, FluidStack> {
        val fluidStack: FluidStack = FluidUtil.getFluidContained(container).orElse(FluidStack.EMPTY)
        return when {
            ingredient.test(fluidStack) -> container.craftingRemainingItem to fluidStack
            else -> ItemStack.EMPTY to FluidStack.EMPTY
        }
    }

    override fun fillContainer(container: ItemStack, fluidStack: FluidStack): ItemStack = when {
        container.`is`(Tags.Items.BUCKETS_EMPTY) && ingredient.test(fluidStack) -> FluidUtil.getFilledBucket(fluidStack)
        else -> ItemStack.EMPTY
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BUCKET_INTERACTION
}
