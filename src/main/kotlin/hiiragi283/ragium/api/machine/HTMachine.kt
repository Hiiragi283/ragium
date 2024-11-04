package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.buildItemStack
import hiiragi283.ragium.api.machine.block.HTMachineBlock
import hiiragi283.ragium.api.property.HTPropertyHolder
import net.minecraft.item.ItemStack

@JvmDefaultWithCompatibility
interface HTMachine {
    val key: HTMachineKey

    fun getType(): HTMachineTypeNew = RagiumAPI.getInstance().machineRegistry.getType(key)

    fun isConsumer(): Boolean = getType() == HTMachineTypeNew.CONSUMER

    fun isGenerator(): Boolean = getType() == HTMachineTypeNew.GENERATOR

    fun isProcessor(): Boolean = getType() == HTMachineTypeNew.PROCESSOR

    fun asProperties(): HTPropertyHolder = RagiumAPI.getInstance().machineRegistry.getProperty(key)

    fun getBlock(tier: HTMachineTier): HTMachineBlock? = RagiumAPI
        .getInstance()
        .machineRegistry.blocks
        .get(key, tier)

    fun createItemStack(tier: HTMachineTier): ItemStack = buildItemStack(getBlock(tier))
}
