package hiiragi283.lib.registry

import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType

/**
 * [ブロックエンティティ][BlockEntity]向けの[HTDeferredRegister]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredBlockEntityTypeRegister(namespace: String) :
    HTDeferredRegister<BlockEntityType<*>>(Registries.BLOCK_ENTITY_TYPE, namespace) {
    fun <BE : BlockEntity> registerType(
        name: String,
        factory: BlockEntityType.BlockEntitySupplier<BE>
    ): HTDeferredBlockEntityType<BE> {
        val holder = HTDeferredBlockEntityType<BE>(createId(name))
        this.register(name) { _ -> BlockEntityType(factory, setOf()) }
        return holder
    }

    fun <BE : BlockEntity> registerType(
        name: String,
        factory: BlockEntityType.BlockEntitySupplier<BE>,
        serverTicker: BlockEntityTicker<in BE>?,
        clientTicker: BlockEntityTicker<in BE>? = null
    ): HTDeferredBlockEntityType<BE> {
        val holder: HTDeferredBlockEntityType<BE> = registerType(name, factory)
        holder.clientTicker = clientTicker
        holder.serverTicker = serverTicker
        return holder
    }

    // With supported blocks
    fun <BE : BlockEntity> registerType(
        name: String,
        factory: BlockEntityType.BlockEntitySupplier<BE>,
        blockBuilder: MutableSet<Block>.() -> Unit
    ): HTDeferredBlockEntityType<BE> {
        val holder = HTDeferredBlockEntityType<BE>(createId(name))
        this.register(name) { _ -> BlockEntityType(factory, *buildSet(blockBuilder).toTypedArray()) }
        return holder
    }

    fun <BE : BlockEntity> registerType(
        name: String,
        factory: BlockEntityType.BlockEntitySupplier<BE>,
        serverTicker: BlockEntityTicker<in BE>?,
        clientTicker: BlockEntityTicker<in BE>? = null,
        blockBuilder: MutableSet<Block>.() -> Unit
    ): HTDeferredBlockEntityType<BE> {
        val holder: HTDeferredBlockEntityType<BE> = registerType(name, factory, blockBuilder)
        holder.clientTicker = clientTicker
        holder.serverTicker = serverTicker
        return holder
    }
}
