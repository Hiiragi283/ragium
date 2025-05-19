package hiiragi283.ragium.api.network

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps

interface HTNbtCodec {
    /**
     * 指定した[nbt]に値を書き込みます。
     */
    fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>)

    /**
     * 指定した[nbt]から値を読み取ります。
     */
    fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>)
}
