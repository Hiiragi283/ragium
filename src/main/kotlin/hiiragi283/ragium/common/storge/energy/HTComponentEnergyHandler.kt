package hiiragi283.ragium.common.storge.energy

import hiiragi283.core.api.storage.attachments.HTAttachedEnergy
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.api.storage.energy.HTEnergyHandler
import hiiragi283.core.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storge.attachment.HTComponentHandler
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack

/**
 * [HTEnergyBattery]に基づいたコンポーネント向けの実装
 * @see mekanism.common.attachments.containers.energy.ComponentBackedEnergyHandler
 */
class HTComponentEnergyHandler(attachedTo: ItemStack, size: Int, containerFactory: ContainerFactory<HTEnergyBattery>) :
    HTComponentHandler<Int, HTEnergyBattery, HTAttachedEnergy>(attachedTo, size, containerFactory),
    HTEnergyHandler {
    override fun capabilityCodec(): HTCapabilityCodec<HTEnergyBattery, HTAttachedEnergy> = HTCapabilityCodec.ENERGY

    override fun getEnergyBattery(side: Direction?): HTEnergyBattery? = getContainers().firstOrNull()
}
