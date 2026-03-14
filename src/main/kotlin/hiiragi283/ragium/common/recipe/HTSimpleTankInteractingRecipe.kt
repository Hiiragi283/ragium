package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.registry.HTSimpleFluidHolderLike
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.registry.isOf
import hiiragi283.core.api.registry.toStack
import hiiragi283.ragium.api.recipe.HTTankInteractingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import java.util.*

class HTSimpleTankInteractingRecipe(
    val emptyContainer: HTSimpleItemHolderLike,
    val filledContainer: HTSimpleItemHolderLike,
    val fluid: HTSimpleFluidHolderLike,
    val amount: Int,
    val fluidTag: Optional<TagKey<Fluid>>,
) : HTTankInteractingRecipe {
    override fun emptyContainer(container: ItemStack): Pair<ItemStack, FluidStack> = when {
        filledContainer.isOf(container) -> emptyContainer.toStack() to fluid.toStack(amount)
        else -> ItemStack.EMPTY to FluidStack.EMPTY
    }

    override fun fillContainer(container: ItemStack, fluidStack: FluidStack): ItemStack {
        val bool1: Boolean = emptyContainer.isOf(container)
        val bool2: Boolean = fluidTag.map(fluidStack::`is`).orElse(false) || fluid.isOf(fluidStack)
        val bool3: Boolean = fluidStack.amount >= amount
        return when {
            bool1 && bool2 && bool3 -> filledContainer.toStack()
            else -> ItemStack.EMPTY
        }
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.TANK_INTERACTING
}
