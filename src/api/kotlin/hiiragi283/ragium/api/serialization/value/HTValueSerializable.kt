package hiiragi283.ragium.api.serialization.value

import hiiragi283.ragium.api.RagiumPlatform
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.common.util.INBTSerializable

/**
 * [HTValueInput], [HTValueOutput]に対応した[INBTSerializable]の拡張インターフェース
 */
interface HTValueSerializable : INBTSerializable<CompoundTag> {
    /**
     * [output]に値を書き込みます。
     */
    fun serialize(output: HTValueOutput)

    /**
     * [input]から値を読み取ります。
     */
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

    interface Empty : HTValueSerializable {
        override fun serialize(output: HTValueOutput) {}

        override fun deserialize(input: HTValueInput) {}
    }
}
