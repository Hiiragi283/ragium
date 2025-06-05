package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.extension.getLevel
import hiiragi283.ragium.api.inventory.HTSlotPos
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemVariant
import hiiragi283.ragium.common.inventory.HTContainerItemSlot
import hiiragi283.ragium.setup.RagiumEnchantments
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.neoforged.neoforge.common.util.INBTSerializable
import org.jetbrains.annotations.UnknownNullability

open class HTItemSlotImpl(
    private val nbtKey: String,
    private val baseCapacity: Int,
    private val validator: (HTItemVariant) -> Boolean,
    private val callback: () -> Unit,
) : HTItemSlot(),
    INBTSerializable<CompoundTag> {
    override var capacity: Int = baseCapacity

    final override fun isValid(variant: HTItemVariant): Boolean = validator(variant)

    final override fun onContentsChanged() {
        callback()
    }

    override fun createContainerSlot(x: Int, y: Int, storageIO: HTStorageIO): Slot = HTContainerItemSlot(
        this,
        storageIO,
        HTSlotPos.getSlotPosX(x),
        HTSlotPos.getSlotPosY(y),
    )

    override fun onUpdateEnchantment(newEnchantments: ItemEnchantments) {
        val level: Int = newEnchantments.getLevel(RagiumEnchantments.CAPACITY) + 1
        capacity = level * baseCapacity
    }

    final override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(nbtKey, this)
    }

    final override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(nbtKey, this)
    }

    //    INBTSerializable    //

    override fun serializeNBT(provider: HolderLookup.Provider): @UnknownNullability CompoundTag = buildNbt {
        HTItemVariant.CODEC
            .encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), resource)
            .ifSuccess { put("item", it) }
        putInt("amount", amount)
    }

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        HTItemVariant.CODEC
            .parse(provider.createSerializationContext(NbtOps.INSTANCE), nbt.get("item"))
            .ifSuccess { resource = it }
        amount = nbt.getInt("amount")
        onContentsChanged()
    }
}
