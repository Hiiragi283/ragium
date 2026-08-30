package hiiragi283.lib.material.part.property

import hiiragi283.lib.property.HTPropertyKey
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.world.level.block.state.BlockBehaviour

data object HTPartPropertyKeys {
    @JvmField
    val TAG_PREFIX: HTPropertyKey.Simple<HTTagPrefix> = HTPropertyKey.Simple(RagiumAPI.id("tag_prefix"))

    //    Block    //

    /**
     * ブロックの[プロパティ][BlockBehaviour.Properties]を管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val BLOCK_PROP: HTPropertyKey.Simple<BlockBehaviour.Properties> = HTPropertyKey.Simple(RagiumAPI.id("block_properties"))
}
