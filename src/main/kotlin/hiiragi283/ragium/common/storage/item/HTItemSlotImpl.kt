package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.extension.getLevel
import hiiragi283.ragium.api.inventory.HTSlotPos
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemVariant
import hiiragi283.ragium.common.init.RagiumEnchantments
import hiiragi283.ragium.common.inventory.HTContainerItemSlot
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.enchantment.ItemEnchantments

class HTItemSlotImpl(
    private val nbtKey: String,
    private val baseCapacity: Int,
    validator: (HTItemVariant) -> Boolean,
    callback: Runnable,
) : HTItemSlot(validator, callback) {
    override var capacity: Int = baseCapacity

    override fun onUpdateEnchantment(newEnchantments: ItemEnchantments) {
        val level: Int = newEnchantments.getLevel(RagiumEnchantments.CAPACITY) + 1
        capacity = level * baseCapacity
    }

    override fun createContainerSlot(x: Int, y: Int, storageIO: HTStorageIO): Slot = HTContainerItemSlot(
        this,
        storageIO,
        HTSlotPos.getSlotPosX(x),
        HTSlotPos.getSlotPosY(y),
    )

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        val nbtIn = CompoundTag()
        HTItemVariant.CODEC
            .encodeStart(registryOps, resource)
            .ifSuccess { nbtIn.put("item", it) }
        nbtIn.putInt("amount", amount)
        nbt.put(nbtKey, nbtIn)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        val nbtIn: CompoundTag = nbt.getCompound(nbtKey)
        HTItemVariant.CODEC
            .parse(registryOps, nbtIn.get("item"))
            .ifSuccess { resource = it }
        amount = nbtIn.getInt("amount")
    }
}
