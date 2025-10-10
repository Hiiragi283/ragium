package hiiragi283.ragium.api.serialization.value

import hiiragi283.ragium.api.RagiumPlatform
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.common.util.INBTSerializable

interface HTValueSerializable : INBTSerializable<CompoundTag> {
    companion object {
        @JvmStatic
        fun trySerialize(obj: Any?, output: HTValueOutput) {
            if (obj is HTValueSerializable) {
                obj.serialize(output)
            }
        }

        @JvmStatic
        fun tryDeserialize(obj: Any?, input: HTValueInput) {
            if (obj is HTValueSerializable) {
                obj.deserialize(input)
            }
        }
    }

    fun serialize(output: HTValueOutput)

    fun deserialize(input: HTValueInput)

    @Deprecated("Use `serialize(HTValueOutput)` instead", level = DeprecationLevel.ERROR)
    override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag {
        val tag = CompoundTag()
        val output: HTValueOutput = RagiumPlatform.INSTANCE.createValueOutput(provider, tag)
        serialize(output)
        return tag
    }

    @Deprecated("Use `deserialize(HTValueInput)` instead", level = DeprecationLevel.ERROR)
    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        val input: HTValueInput = RagiumPlatform.INSTANCE.createValueInput(provider, nbt)
        deserialize(input)
    }
}
