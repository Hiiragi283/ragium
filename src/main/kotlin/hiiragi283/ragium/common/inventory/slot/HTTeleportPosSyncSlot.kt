package hiiragi283.ragium.common.inventory.slot

import hiiragi283.ragium.api.inventory.slot.HTChangeType
import hiiragi283.ragium.api.inventory.slot.HTSyncableSlot
import hiiragi283.ragium.api.inventory.slot.payload.HTSyncablePayload
import hiiragi283.ragium.api.item.component.HTTeleportPos
import hiiragi283.ragium.common.inventory.slot.payload.HTTeleportPosSyncPayload
import net.minecraft.core.RegistryAccess
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * @see mekanism.common.inventory.container.sync.SyncableBlockPos
 */
class HTTeleportPosSyncSlot(private val getter: Supplier<HTTeleportPos?>, private val setter: Consumer<HTTeleportPos?>) : HTSyncableSlot {
    private var lastPosHash: Int = 0

    fun getHTTeleportPos(): HTTeleportPos? = this.getter.get()

    fun setHTTeleportPos(pos: HTTeleportPos?) {
        this.setter.accept(pos)
    }

    override fun getChange(): HTChangeType {
        val current: HTTeleportPos? = this.getHTTeleportPos()
        val hash: Int = current?.hashCode() ?: 0
        return if (hash == this.lastPosHash) {
            HTChangeType.EMPTY
        } else {
            this.lastPosHash = hash
            HTChangeType.FULL
        }
    }

    override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTSyncablePayload =
        HTTeleportPosSyncPayload(this.getHTTeleportPos())
}
