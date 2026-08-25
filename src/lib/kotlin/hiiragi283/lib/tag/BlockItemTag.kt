package hiiragi283.lib.tag

import hiiragi283.lib.resource.toId
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

@JvmRecord
data class BlockItemTag(val block: TagKey<Block>, val item: TagKey<Item>) {
    constructor(location: Identifier) : this(Registries.BLOCK.createTagKey(location), Registries.ITEM.createTagKey(location))

    constructor(namespace: String, path: String) : this(namespace.toId(path))

    constructor(namespace: String, vararg path: String) : this(namespace.toId(*path))
}
