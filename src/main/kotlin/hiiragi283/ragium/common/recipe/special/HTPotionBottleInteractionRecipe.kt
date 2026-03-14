package hiiragi283.ragium.common.recipe.special

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.util.HCPotionFluidHelper
import hiiragi283.ragium.api.recipe.HTTankInteractingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.fluids.FluidStack

data object HTPotionBottleInteractionRecipe : HTTankInteractingRecipe {
    override val amount: Int = HTConst.DEFAULT_FLUID_AMOUNT / 4

    override fun canEmptyContainer(container: ItemStack): Boolean {
        val bool1: Boolean = HTBottleType.entries.any { container.`is`(it.asItem()) }
        val bool2: Boolean = HTPotionHelper.getContents(container) != null
        return bool1 && bool2
    }

    override fun emptyContainer(container: ItemStack): Pair<ItemStack, FluidStack> {
        val content: HTPotionContents = HTPotionHelper.getContents(container) ?: return ItemStack.EMPTY to FluidStack.EMPTY
        return ItemStack(Items.GLASS_BOTTLE) to HCPotionFluidHelper.createFluid(content, amount)
    }

    override fun canFillContainer(container: ItemStack, fluidStack: FluidStack): Boolean {
        if (!container.`is`(Items.GLASS_BOTTLE)) return false
        val contents: HTPotionContents = HTPotionHelper.getContents(fluidStack) ?: return false
        return !contents.isEmpty
    }

    override fun fillContainer(container: ItemStack, fluidStack: FluidStack): ItemStack = HTPotionHelper
        .getContents(fluidStack)
        ?.let(HTPotionHelper::createPotion)
        ?: ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.POTION_BOTTLE_INTERACTION
}
