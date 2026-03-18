package hiiragi283.ragium.common.data.tank

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.util.HCPotionFluidHelper
import hiiragi283.ragium.api.data.tank.HTTankInteraction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.fluids.FluidStack

data object HTPotionTankInteraction : HTTankInteraction.Serializable {
    @JvmField
    val CODEC: MapCodec<HTPotionTankInteraction> = MapCodec.unit { HTPotionTankInteraction }

    override fun type(): MapCodec<HTPotionTankInteraction> = CODEC

    override val amount: Int = HTConst.DEFAULT_FLUID_AMOUNT / 4

    override fun canEmptyContainer(container: HTItemResourceType): Boolean {
        val bool1: Boolean = HTBottleType.entries.any { container.isOf(it.asItem()) }
        val bool2: Boolean = HTPotionHelper.getContents(container) != null
        return bool1 && bool2
    }

    override fun emptyContainer(container: HTItemResourceType): Pair<ItemStack, FluidStack> {
        val content: BottledPotionContents = HTPotionHelper.getContents(container) ?: return ItemStack.EMPTY to FluidStack.EMPTY
        return ItemStack(Items.GLASS_BOTTLE) to HCPotionFluidHelper.createFluid(content, amount)
    }

    override fun canFillContainer(container: HTItemResourceType, fluidStack: HTFluidResourceType): Boolean {
        if (!container.isOf(Items.GLASS_BOTTLE)) return false
        val contents: BottledPotionContents = HTPotionHelper.getContents(fluidStack) ?: return false
        return !contents.isEmpty
    }

    override fun fillContainer(container: HTItemResourceType, fluidStack: HTFluidResourceType): ItemStack = HTPotionHelper
        .getContents(fluidStack)
        ?.let(HTPotionHelper::createPotion)
        ?: ItemStack.EMPTY
}
