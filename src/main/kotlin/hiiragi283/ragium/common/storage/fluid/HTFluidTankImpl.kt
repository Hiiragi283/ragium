package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.extension.getLevel
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidVariant
import hiiragi283.ragium.setup.RagiumEnchantments
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.item.enchantment.ItemEnchantments

open class HTFluidTankImpl(
    private val nbtKey: String,
    private val baseCapacity: Int,
    private val validator: (HTFluidVariant) -> Boolean,
    private val callback: () -> Unit,
) : HTFluidTank() {
    override var capacity: Int = baseCapacity

    final override fun isValid(variant: HTFluidVariant): Boolean = validator(variant)

    final override fun onContentsChanged() {
        callback()
    }

    override fun onUpdateEnchantment(newEnchantments: ItemEnchantments) {
        val level: Int = newEnchantments.getLevel(RagiumEnchantments.CAPACITY) + 1
        capacity = level * baseCapacity
    }

    final override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        nbt.put(
            nbtKey,
            buildNbt {
                HTFluidVariant.CODEC
                    .encodeStart(registryOps, resource)
                    .ifSuccess { put("fluid", it) }
                putInt("amount", amount)
            },
        )
    }

    final override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        val nbtIn: CompoundTag = nbt.getCompound(nbtKey)
        HTFluidVariant.CODEC
            .parse(registryOps, nbtIn.get("fluid"))
            .ifSuccess { resource = it }
        amount = nbtIn.getInt("amount")
        onContentsChanged()
    }
}
