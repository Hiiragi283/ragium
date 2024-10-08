package hiiragi283.ragium.api.machine

import hiiragi283.ragium.common.block.HTMachineBlock

fun interface HTMachineConvertible {
    fun asMachine(): HTMachineType

    fun asGenerator(): HTMachineType? = asMachine().takeIf(HTMachineType::isGenerator)

    fun asProcessor(): HTMachineType? = asMachine().takeIf(HTMachineType::isProcessor)

    fun isGenerator(): Boolean = asMachine()[HTMachinePropertyKeys.CATEGORY] == HTMachineType.Category.GENERATOR

    fun isProcessor(): Boolean = asMachine()[HTMachinePropertyKeys.CATEGORY] == HTMachineType.Category.PROCESSOR

    fun getBlock(tier: HTMachineTier): HTMachineBlock? = HTMachineBlockRegistry.get(this, tier)

    fun getBlockOrThrow(tier: HTMachineTier): HTMachineBlock = HTMachineBlockRegistry.getOrThrow(this, tier)
}
