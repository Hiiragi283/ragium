package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object RagiumBlockTags {
    //    Custom    //
    @JvmField
    val COILS: TagKey<Block> = create(Ragium.MOD_ID, "coils")

    @JvmStatic
    fun create(namespace: String, path: String): TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, Identifier.of(namespace, path))

    @JvmStatic
    fun create(path: String): TagKey<Block> = create(TagUtil.C_TAG_NAMESPACE, path)
}
