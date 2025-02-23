package hiiragi283.ragium.api.util

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps

interface HTNbtCodec {
    fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>)

    fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>)
}
