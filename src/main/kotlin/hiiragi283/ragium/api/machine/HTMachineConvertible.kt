package hiiragi283.ragium.api.machine

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.util.buildItemStack
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack

fun interface HTMachineConvertible {
    fun asMachine(): HTMachineType

    fun asGenerator(): HTMachineType? = asMachine().takeIf(HTMachineType::isGenerator)

    fun asProcessor(): HTMachineType? = asMachine().takeIf(HTMachineType::isProcessor)

    fun isGenerator(): Boolean = asMachine().category == HTMachineType.Category.GENERATOR

    fun isProcessor(): Boolean = asMachine().category == HTMachineType.Category.PROCESSOR

    fun getBlockOrThrow(tier: HTMachineTier): Block = Blocks.BRICKS

    fun createItemStack(tier: HTMachineTier): ItemStack = buildItemStack(
        RagiumContents.META_MACHINE,
    ) {
        add(RagiumComponentTypes.MACHINE_TYPE, asMachine())
        add(RagiumComponentTypes.MACHINE_TIER, tier)
    }
}
