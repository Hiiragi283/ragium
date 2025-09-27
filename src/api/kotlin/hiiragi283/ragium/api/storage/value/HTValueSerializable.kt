package hiiragi283.ragium.api.storage.value

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.common.util.INBTSerializable

interface HTValueSerializable : INBTSerializable<CompoundTag> {
    fun serialize(output: HTValueOutput)

    fun deserialize(input: HTValueInput)

    @Deprecated("Use `serialize(HTValueOutput)` instead", level = DeprecationLevel.ERROR)
    override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag {
        val tag = CompoundTag()
        val output: HTValueOutput = RagiumAPI.INSTANCE.createValueOutput(provider, tag)
        serialize(output)
        return tag
    }

    @Deprecated("Use `deserialize(HTValueInput)` instead", level = DeprecationLevel.ERROR)
    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        val input: HTValueInput = RagiumAPI.INSTANCE.createValueInput(provider, nbt)
        deserialize(input)
    }
}
