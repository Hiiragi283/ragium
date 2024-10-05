package hiiragi283.ragium.api.machine

import hiiragi283.ragium.common.block.HTMachineBlock

fun interface HTMachineConvertible {
    fun asMachine(): HTMachineType

    fun asGenerator(): HTMachineType.Generator? = asMachine() as? HTMachineType.Generator

    fun asProcessor(): HTMachineType.Processor? = asMachine() as? HTMachineType.Processor

    fun isGenerator(): Boolean = asMachine() is HTMachineType.Generator

    fun isProcessor(): Boolean = asMachine() is HTMachineType.Processor

    fun getBlock(tier: HTMachineTier): HTMachineBlock? = HTMachineBlockRegistry.get(this, tier)

    fun getBlockOrThrow(tier: HTMachineTier): HTMachineBlock = HTMachineBlockRegistry.getOrThrow(this, tier)
}
