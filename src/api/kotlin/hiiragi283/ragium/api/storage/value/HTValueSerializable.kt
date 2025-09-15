package hiiragi283.ragium.api.storage.value

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.common.util.INBTSerializable

interface HTValueSerializable : INBTSerializable<CompoundTag> {
    fun serialize(output: HTValueOutput)

    fun deserialize(input: HTValueInput)

    @Deprecated("use serialize(HTValueOutput) instead of this")
    override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag {
        val tag = CompoundTag()
        val output: HTValueOutput = RagiumAPI.INSTANCE.createValueOutput(provider, tag)
        serialize(output)
        return tag
    }

    @Deprecated("use deserialize(HTValueInput) instead of this")
    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        val input: HTValueInput = RagiumAPI.INSTANCE.createValueInput(provider, nbt)
        deserialize(input)
    }
}
