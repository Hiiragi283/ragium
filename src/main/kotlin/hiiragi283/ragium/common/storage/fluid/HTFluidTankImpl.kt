package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.extension.getHighestLevel
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidVariant
import hiiragi283.ragium.api.tag.RagiumEnchantmentTags
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.item.enchantment.ItemEnchantments

class HTFluidTankImpl(
    private val nbtKey: String,
    private val baseCapacity: Int,
    validator: (HTFluidVariant) -> Boolean,
    callback: () -> Unit,
) : HTFluidTank(validator, callback) {
    override var capacity: Int = baseCapacity

    override fun onUpdateEnchantment(newEnchantments: ItemEnchantments) {
        val level: Int = newEnchantments.getHighestLevel(RagiumEnchantmentTags.CAPACITY) + 1
        capacity = level * baseCapacity
    }

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
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

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        val nbtIn: CompoundTag = nbt.getCompound(nbtKey)
        HTFluidVariant.CODEC
            .parse(registryOps, nbtIn.get("fluid"))
            .ifSuccess { resource = it }
        amount = nbtIn.getInt("amount")
    }
}
