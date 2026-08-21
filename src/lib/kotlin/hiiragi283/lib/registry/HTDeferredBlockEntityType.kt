package hiiragi283.lib.registry

import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

/**
 * [ブロックエンティティ][BlockEntity]向けの[HTDeferredHolder]の拡張クラスです。
 * @param BE ブロックエンティティのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredBlockEntityType<BE : BlockEntity> : HTDeferredHolder<BlockEntityType<*>, BlockEntityType<BE>> {
    constructor(key: ResourceKey<BlockEntityType<*>>) : super(key)

    constructor(id: Identifier) : super(Registries.BLOCK_ENTITY_TYPE, id)

    fun create(pos: BlockPos, state: BlockState): BE = get().create(pos, state)

    internal var clientTicker: BlockEntityTicker<in BE>? = null
    internal var serverTicker: BlockEntityTicker<in BE>? = null

    fun getTicker(isClient: Boolean): BlockEntityTicker<in BE>? = when (isClient) {
        true -> clientTicker
        false -> serverTicker
    }
}
