package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.block.entity.HTBurningBoxBlockEntity
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.block.entity.HTManualGrinderBlockEntity
import hiiragi283.ragium.common.block.entity.HTWaterCollectorBlockEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumBlockEntityTypes {
    @JvmField
    val MACHINE: BlockEntityType<HTMachineBlockEntity> =
        register("machine", ::HTMachineBlockEntity)

    @JvmField
    val MANUAL_GRINDER: BlockEntityType<HTManualGrinderBlockEntity> =
        register("manual_grinder", ::HTManualGrinderBlockEntity)

    @JvmField
    val WATER_COLLECTOR: BlockEntityType<HTWaterCollectorBlockEntity> =
        register("water_collector", ::HTWaterCollectorBlockEntity)

    @JvmField
    val BURNING_BOX: BlockEntityType<HTBurningBoxBlockEntity> =
        register("burning_box", ::HTBurningBoxBlockEntity)

    private fun <T : BlockEntity> register(name: String, factory: BlockEntityType.BlockEntityFactory<T>): BlockEntityType<T> =
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Ragium.id(name),
            BlockEntityType.Builder.create(factory).build(),
        )
}
