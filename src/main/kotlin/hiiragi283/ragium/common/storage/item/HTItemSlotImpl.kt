package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.extension.getHighestLevel
import hiiragi283.ragium.api.inventory.HTSlotPos
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemVariant
import hiiragi283.ragium.api.tag.RagiumEnchantmentTags
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
    callback: () -> Unit,
) : HTItemSlot(validator, callback) {
    override var capacity: Int = baseCapacity

    override fun onUpdateEnchantment(newEnchantments: ItemEnchantments) {
        val level: Int = newEnchantments.getHighestLevel(RagiumEnchantmentTags.CAPACITY) + 1
        capacity = level * baseCapacity
    }

    override fun createContainerSlot(x: Int, y: Int, storageIO: HTStorageIO): Slot = HTContainerItemSlot(
        this,
        storageIO,
        HTSlotPos.getSlotPosX(x),
        HTSlotPos.getSlotPosY(y),
    )

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        nbt.put(
            nbtKey,
            buildNbt {
                HTItemVariant.CODEC
                    .encodeStart(registryOps, resource)
                    .ifSuccess { put("item", it) }
                putInt("amount", amount)
            },
        )
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        val nbtIn: CompoundTag = nbt.getCompound(nbtKey)
        HTItemVariant.CODEC
            .parse(registryOps, nbtIn.get("item"))
            .ifSuccess { resource = it }
        amount = nbtIn.getInt("amount")
    }
}
