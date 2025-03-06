package hiiragi283.ragium.api.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class HTBlockEntityTypeRegister(namespace: String) : DeferredRegister<BlockEntityType<*>>(Registries.BLOCK_ENTITY_TYPE, namespace) {
    fun <T : BlockEntity> registerType(
        name: String,
        factory: BlockEntityType.BlockEntitySupplier<T>,
        blocks: Collection<DeferredBlock<*>>,
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> = registerType(name, factory, *blocks.toTypedArray())

    fun <T : BlockEntity> registerType(
        name: String,
        factory: BlockEntityType.BlockEntitySupplier<T>,
        vararg blocks: DeferredBlock<*>,
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> = register(name) { _: ResourceLocation ->
        BlockEntityType.Builder
            .of(
                factory,
                *blocks.map(DeferredBlock<*>::get).toTypedArray(),
            ).build(null)
    }

    fun <T : BlockEntity> registerType(
        name: String,
        factory: BlockEntityType.BlockEntitySupplier<T>,
        blocks: Collection<Block>,
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> = registerType(name, factory, *blocks.toTypedArray())

    fun <T : BlockEntity> registerType(
        name: String,
        factory: BlockEntityType.BlockEntitySupplier<T>,
        vararg blocks: Block,
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> =
        register(name) { _: ResourceLocation -> BlockEntityType.Builder.of(factory, *blocks).build(null) }
}
