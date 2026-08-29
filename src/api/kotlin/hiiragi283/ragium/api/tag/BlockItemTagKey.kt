package hiiragi283.ragium.api.tag

import hiiragi283.core.api.tag.createTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * ブロックとアイテムの[TagKey]を束ねたクラスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.1.0
 */
@JvmRecord
data class BlockItemTagKey(val block: TagKey<Block>, val item: TagKey<Item>) {
    constructor(id: ResourceLocation) : this(Registries.BLOCK.createTagKey(id), Registries.ITEM.createTagKey(id))
}
