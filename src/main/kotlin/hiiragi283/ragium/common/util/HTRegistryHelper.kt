package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.extension.holdersNotEmpty
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid

object HTRegistryHelper {
    @JvmStatic
    fun fluidHolderSequence(): Sequence<Holder<Fluid>> = BuiltInRegistries.FLUID
        .holdersNotEmpty()
        .filter { holder: Holder<Fluid> ->
            val fluid: Fluid = holder.value()
            (fluid as? FlowingFluid)?.isSource(fluid.defaultFluidState()) ?: false
        }

    @JvmStatic
    fun fluidSequence(): Sequence<Fluid> = fluidHolderSequence().map(Holder<Fluid>::value)

    @JvmStatic
    fun itemHolderSequence(): Sequence<Holder<Item>> = BuiltInRegistries.ITEM.holdersNotEmpty()

    @JvmStatic
    fun itemSequence(): Sequence<Item> = itemHolderSequence().map(Holder<Item>::value)

    @JvmStatic
    fun itemStackSequence(): Sequence<ItemStack> = itemSequence().map(Item::getDefaultInstance)
}
