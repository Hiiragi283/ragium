package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.block.entity.HTBlockEntityFactory
import hiiragi283.ragium.api.registry.HTDeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType

/**
 * Ragiumで使用する[BlockEntityType]向けの[HTDeferredRegister]
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class HTDeferredBlockEntityTypeRegister(namespace: String) :
    HTDeferredRegister<BlockEntityType<*>>(Registries.BLOCK_ENTITY_TYPE, namespace) {
    fun <BE : BlockEntity> registerType(name: String, factory: HTBlockEntityFactory<BE>): HTDeferredBlockEntityType<BE> {
        val holder = HTDeferredBlockEntityType<BE>(createId(name))
        register(name) { _: ResourceLocation -> BlockEntityType.Builder.of(factory).build(null) }
        return holder
    }

    fun <BE : BlockEntity> registerType(
        name: String,
        factory: HTBlockEntityFactory<BE>,
        clientTicker: BlockEntityTicker<in BE>,
        serverTicker: BlockEntityTicker<in BE>,
    ): HTDeferredBlockEntityType<BE> {
        val holder: HTDeferredBlockEntityType<BE> = registerType(name, factory)
        holder.clientTicker = clientTicker
        holder.serverTicker = serverTicker
        return holder
    }
}
