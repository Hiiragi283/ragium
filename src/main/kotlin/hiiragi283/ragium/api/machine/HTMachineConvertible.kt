package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.extension.buildItemStack
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack

fun interface HTMachineConvertible {
    fun asMachine(): HTMachineType

    fun asGenerator(): HTMachineType? = asMachine().takeIf(HTMachineType::isGenerator)

    fun asProcessor(): HTMachineType? = asMachine().takeIf(HTMachineType::isProcessor)

    fun isGenerator(): Boolean = asMachine()[HTMachinePropertyKeys.CATEGORY] == HTMachineType.Category.GENERATOR

    fun isProcessor(): Boolean = asMachine()[HTMachinePropertyKeys.CATEGORY] == HTMachineType.Category.PROCESSOR

    fun getBlockOrThrow(tier: HTMachineTier): Block = Blocks.BRICKS

    fun createItemStack(tier: HTMachineTier): ItemStack = buildItemStack(
        RagiumBlocks.META_MACHINE,
    ) {
        add(HTMachineType.COMPONENT_TYPE, asMachine())
        add(HTMachineTier.COMPONENT_TYPE, tier)
    }

    fun isOf(other: HTMachineConvertible): Boolean = asMachine() == other.asMachine()
}
