package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.block.entity.HTManualGrinderBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumBlockEntityTypes {

    @JvmField
    val MANUAL_GRINDER: BlockEntityType<HTManualGrinderBlockEntity> =
        register("manual_grinder", ::HTManualGrinderBlockEntity, RagiumBlocks.MANUAL_GRINDER)

    private fun <T : BlockEntity> register(
        name: String,
        factory: BlockEntityType.BlockEntityFactory<T>,
        vararg blocks: Block,
    ): BlockEntityType<T> = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        Ragium.id(name),
        BlockEntityType.Builder.create(factory, *blocks).build()
    )

}