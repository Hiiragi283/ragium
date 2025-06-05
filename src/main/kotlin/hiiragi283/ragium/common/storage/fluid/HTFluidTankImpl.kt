package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.extension.getLevel
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidVariant
import hiiragi283.ragium.setup.RagiumEnchantments
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.neoforged.neoforge.common.util.INBTSerializable
import org.jetbrains.annotations.UnknownNullability

open class HTFluidTankImpl(
    private val nbtKey: String,
    private val baseCapacity: Int,
    private val validator: (HTFluidVariant) -> Boolean,
    private val callback: () -> Unit,
) : HTFluidTank(),
    INBTSerializable<CompoundTag> {
    override var capacity: Int = baseCapacity

    final override fun isValid(variant: HTFluidVariant): Boolean = validator(variant)

    final override fun onContentsChanged() {
        callback()
    }

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
        HTFluidVariant.CODEC
            .encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), resource)
            .ifSuccess { put("fluid", it) }
        putInt("amount", amount)
    }

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        HTFluidVariant.CODEC
            .parse(provider.createSerializationContext(NbtOps.INSTANCE), nbt.get("fluid"))
            .ifSuccess { resource = it }
        amount = nbt.getInt("amount")
        onContentsChanged()
    }
}
