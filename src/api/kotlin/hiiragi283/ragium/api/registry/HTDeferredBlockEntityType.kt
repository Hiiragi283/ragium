package hiiragi283.ragium.api.registry

import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.DeferredHolder

class HTDeferredBlockEntityType<BE : BlockEntity> private constructor(key: ResourceKey<BlockEntityType<*>>) :
    DeferredHolder<BlockEntityType<*>, BlockEntityType<BE>>(key) {
        companion object {
            @JvmStatic
            fun <T : BlockEntity> createType(key: ResourceLocation): HTDeferredBlockEntityType<T> = createType(
                ResourceKey.create(
                    Registries.BLOCK_ENTITY_TYPE,
                    key,
                ),
            )

            @JvmStatic
            fun <T : BlockEntity> createType(key: ResourceKey<BlockEntityType<*>>): HTDeferredBlockEntityType<T> =
                HTDeferredBlockEntityType(key)
        }

        fun create(pos: BlockPos, state: BlockState): BE? = get().create(pos, state)

        internal var clientTicker: BlockEntityTicker<in BE>? = null
        internal var serverTicker: BlockEntityTicker<in BE>? = null

        fun getTicker(isClient: Boolean): BlockEntityTicker<in BE>? = when (isClient) {
            true -> clientTicker
            false -> serverTicker
        }
    }
