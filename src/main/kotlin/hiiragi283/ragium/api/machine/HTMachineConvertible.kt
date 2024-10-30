package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.extension.buildItemStack
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack

fun interface HTMachineConvertible {
    fun asMachine(): HTMachineType

    fun asGenerator(): HTMachineType.Generator = checkNotNull(asMachine() as? HTMachineType.Generator)

    fun asProcessor(): HTMachineType.Processor = checkNotNull(asMachine() as? HTMachineType.Processor)

    fun asGeneratorOrNull(): HTMachineType.Generator? = asMachine() as? HTMachineType.Generator

    fun asProcessorOrNull(): HTMachineType.Processor? = asMachine() as? HTMachineType.Processor

    fun isGenerator(): Boolean = asMachine() is HTMachineType.Generator

    fun isProcessor(): Boolean = asMachine() is HTMachineType.Processor

    fun getBlockOrThrow(tier: HTMachineTier): Block = Blocks.BRICKS

    fun createItemStack(tier: HTMachineTier): ItemStack = buildItemStack(
        RagiumBlocks.META_MACHINE,
    ) {
        add(HTMachineType.COMPONENT_TYPE, asMachine())
        add(HTMachineTier.COMPONENT_TYPE, tier)
    }

    fun isOf(other: HTMachineConvertible): Boolean = asMachine() == other.asMachine()
}
