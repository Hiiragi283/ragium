package hiiragi283.lib.resource

import hiiragi283.lib.registry.createKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * [ブロック][Block]と[アイテム][Item]の[ResourceKey]を束ねたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.4
 */
@JvmRecord
data class BlockItemKey(val block: ResourceKey<Block>, val item: ResourceKey<Item>) {
    constructor(location: Identifier) : this(
        Registries.BLOCK.createKey(location),
        Registries.ITEM.createKey(location)
    )

    constructor(namespace: String, path: String) : this(namespace.toId(path))
}
