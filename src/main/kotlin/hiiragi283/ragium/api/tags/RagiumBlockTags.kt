package hiiragi283.ragium.api.tags

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.commonId
import net.minecraft.block.Block
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object RagiumBlockTags {
    //    Custom    //
    /**
     * 常にパイプと接続するブロックのタグ
     */
    @JvmField
    val PIPE_CONNECTABLES: TagKey<Block> = create(RagiumAPI.MOD_ID, "pipe_connectables")

    @JvmStatic
    fun create(namespace: String, path: String): TagKey<Block> = blockTagKey(Identifier.of(namespace, path))

    @JvmStatic
    fun create(path: String): TagKey<Block> = blockTagKey(commonId(path))
}
