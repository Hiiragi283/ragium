package hiiragi283.lib.gui.sync

import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.util.HTDelegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import net.minecraft.core.RegistryAccess
import net.minecraft.world.item.ItemStack

/**
 * [ItemStack]向けの[HTSyncableSlot]の拡張インターフェースです。
 *
 * 参照 : [Mekanism - SyncableItemStack](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/inventory/container/sync/SyncableItemStack.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTItemSyncSlot(property: ReadWriteProperty<Any?, ItemStack>) : HTIntSyncSlot {
    constructor(property: KMutableProperty0<ItemStack>) : this(HTDelegates.LazyDelegate(property::get, property::set))

    constructor(slot: HTBasicItemSlot) : this(HTDelegates.LazyDelegate(slot::getStackCopy, slot::setStack))

    private var lastStack: ItemStack = ItemStack.EMPTY

    var asItemStack: ItemStack by property

    override var amountAsInt: Int
        get() = asItemStack.count
        set(value) {
            asItemStack = asItemStack.copyWithCount(value)
        }

    override fun getChange(): HTChangeType? {
        val current: ItemStack = this.asItemStack
        if (current.isEmpty && lastStack.isEmpty) {
            return null
        }
        val sameItem: Boolean = ItemStack.isSameItemSameComponents(current, lastStack)
        if (!sameItem || this.amountAsInt != this.lastStack.count) {
            this.lastStack = current.copy()
            return when {
                sameItem -> HTChangeType.PARTIAL
                else -> HTChangeType.FULL
            }
        }
        return null
    }

    override fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTSyncablePayload? = when (changeType) {
        HTChangeType.PARTIAL -> HTIntSyncPayload(this.amountAsInt)
        HTChangeType.FULL -> HTItemSyncPayload(this.asItemStack.copy())
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): ItemStack = asItemStack

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: ItemStack) {
        asItemStack = value
    }

    override fun toString(): String = "HTItemSyncSlot(stack=$asItemStack)"
}
