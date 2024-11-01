package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.buildItemStack
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.block.Block
import net.minecraft.item.ItemStack

interface HTMachineConvertible {
    fun asMachine(): HTMachineType

    fun asConsumer(): HTMachineType.Consumer =
        checkNotNull(asMachine() as? HTMachineType.Consumer) { "Machine Type; $key is not Consumer!" }

    fun asGenerator(): HTMachineType.Generator =
        checkNotNull(asMachine() as? HTMachineType.Generator) { "Machine Type; $key is not Generator!" }

    fun asProcessor(): HTMachineType.Processor =
        checkNotNull(asMachine() as? HTMachineType.Processor) { "Machine Type; $key is not Processor!" }

    fun isConsumer(): Boolean = asMachine() is HTMachineType.Consumer

    fun isGenerator(): Boolean = asMachine() is HTMachineType.Generator

    fun isProcessor(): Boolean = asMachine() is HTMachineType.Processor

    fun getBaseBlock(): Block = when (asMachine()) {
        is HTMachineType.Consumer -> RagiumBlocks.META_CONSUMER
        is HTMachineType.Generator -> RagiumBlocks.META_GENERATOR
        is HTMachineType.Processor -> RagiumBlocks.META_PROCESSOR
    }

    fun createItemStack(tier: HTMachineTier): ItemStack = buildItemStack(
        getBaseBlock(),
    ) {
        add(HTMachineType.COMPONENT_TYPE, asMachine())
        add(HTMachineTier.COMPONENT_TYPE, tier)
    }

    fun isOf(other: HTMachineConvertible): Boolean = asMachine() == other.asMachine()

    val key: HTMachineTypeKey
        get() = RagiumAPI
            .getInstance()
            .machineTypeRegistry
            .getKeyOrThrow(asMachine())
}
