package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.api.extension.getLevel
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidVariant
import hiiragi283.ragium.common.init.RagiumEnchantments
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.item.enchantment.ItemEnchantments

class HTFluidTankImpl(
    private val nbtKey: String,
    private val baseCapacity: Int,
    validator: (HTFluidVariant) -> Boolean,
    callback: Runnable,
) : HTFluidTank(validator, callback) {
    override var capacity: Int = baseCapacity

    override fun onUpdateEnchantment(newEnchantments: ItemEnchantments) {
        val level: Int = newEnchantments.getLevel(RagiumEnchantments.CAPACITY) + 1
        capacity = level * baseCapacity
    }

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        val nbtIn = CompoundTag()
        HTFluidVariant.CODEC
            .encodeStart(registryOps, resource)
            .ifSuccess { nbtIn.put("fluid", it) }
        nbtIn.putInt("amount", amount)
        nbt.put(nbtKey, nbtIn)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        val nbtIn: CompoundTag = nbt.getCompound(nbtKey)
        HTFluidVariant.CODEC
            .parse(registryOps, nbtIn.get("fluid"))
            .ifSuccess { resource = it }
        amount = nbtIn.getInt("amount")
    }
}
