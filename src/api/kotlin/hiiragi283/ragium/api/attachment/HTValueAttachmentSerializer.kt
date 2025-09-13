package hiiragi283.ragium.api.attachment

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.storage.value.HTValueInput
import hiiragi283.ragium.api.storage.value.HTValueOutput
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.attachment.IAttachmentHolder
import net.neoforged.neoforge.attachment.IAttachmentSerializer

/**
 * [HTValueInput], [HTValueOutput]に対応した[IAttachmentSerializer]の拡張インターフェース
 */
interface HTValueAttachmentSerializer<T : Any> : IAttachmentSerializer<CompoundTag, T> {
    fun read(holder: IAttachmentHolder, input: HTValueInput): T

    fun write(attachment: T, output: HTValueOutput)

    //    IAttachmentSerializer    //

    override fun read(holder: IAttachmentHolder, tag: CompoundTag, provider: HolderLookup.Provider): T {
        val input: HTValueInput = RagiumAPI.getInstance().createValueInput(provider, tag)
        return read(holder, input)
    }

    override fun write(attachment: T, provider: HolderLookup.Provider): CompoundTag? {
        val tag = CompoundTag()
        val output: HTValueOutput = RagiumAPI.getInstance().createValueOutput(provider, tag)
        write(attachment, output)
        return tag.takeUnless(CompoundTag::isEmpty)
    }
}
