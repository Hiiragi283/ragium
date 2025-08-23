package hiiragi283.ragium.api.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredRegister

/**
 * Ragiumで使用する[BlockEntityType]向けの[DeferredRegister]
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class HTDeferredBlockEntityTypeRegister(namespace: String) : DeferredRegister<BlockEntityType<*>>(Registries.BLOCK_ENTITY_TYPE, namespace) {
    fun <BE : BlockEntity> registerType(name: String, factory: BlockEntityType.BlockEntitySupplier<BE>): HTDeferredBlockEntityType<BE> {
        val holder: HTDeferredBlockEntityType<BE> =
            HTDeferredBlockEntityType.createType(ResourceLocation.fromNamespaceAndPath(namespace, name))
        register(name) { _: ResourceLocation -> BlockEntityType.Builder.of(factory).build(null) }
        return holder
    }

    fun <BE : BlockEntity> registerType(
        name: String,
        factory: BlockEntityType.BlockEntitySupplier<BE>,
        clientTicker: BlockEntityTicker<in BE>,
        serverTicker: BlockEntityTicker<in BE>,
    ): HTDeferredBlockEntityType<BE> {
        val holder: HTDeferredBlockEntityType<BE> = registerType(name, factory)
        holder.clientTicker = clientTicker
        holder.serverTicker = serverTicker
        return holder
    }
}
