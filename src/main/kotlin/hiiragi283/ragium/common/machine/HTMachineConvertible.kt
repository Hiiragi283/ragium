package hiiragi283.ragium.common.machine

import hiiragi283.ragium.common.block.HTMachineBlockBase

fun interface HTMachineConvertible {
    fun asMachine(): HTMachineType

    fun asGenerator(): HTMachineType.Generator? = asMachine() as? HTMachineType.Generator

    fun asProcessor(): HTMachineType.Processor? = asMachine() as? HTMachineType.Processor

    fun isGenerator(): Boolean = asMachine() is HTMachineType.Generator

    fun isProcessor(): Boolean = asMachine() is HTMachineType.Processor

    fun getBlock(tier: HTMachineTier): HTMachineBlockBase? = HTMachineBlockRegistry.get(this, tier)

    fun getBlockOrThrow(tier: HTMachineTier): HTMachineBlockBase = HTMachineBlockRegistry.getOrThrow(this, tier)
}
