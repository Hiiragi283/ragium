package hiiragi283.ragium.api.inventory.slot

import hiiragi283.ragium.api.inventory.slot.payload.HTSyncablePayload
import net.minecraft.core.RegistryAccess

/**
 * @see mekanism.common.inventory.container.sync.ISyncableData
 */
interface HTSyncableSlot {
    fun getChange(): HTChangeType

    fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTSyncablePayload?
}
