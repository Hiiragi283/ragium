package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.block.entity.*
import hiiragi283.ragium.common.block.entity.generator.HTBlazingBoxBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTBurningBoxBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTWaterGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTWindGeneratorBlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumBlockEntityTypes {
    @JvmField
    val MANUAL_GRINDER: BlockEntityType<HTManualGrinderBlockEntity> =
        register("manual_grinder", ::HTManualGrinderBlockEntity)

    @JvmField
    val WATER_COLLECTOR: BlockEntityType<HTWaterCollectorBlockEntity> =
        register("water_collector", ::HTWaterCollectorBlockEntity)

    @JvmField
    val BURNING_BOX: BlockEntityType<HTBurningBoxBlockEntity> =
        register("burning_box", ::HTBurningBoxBlockEntity)

    @JvmField
    val WATER_GENERATOR: BlockEntityType<HTWaterGeneratorBlockEntity> =
        register("water_generator", ::HTWaterGeneratorBlockEntity)

    @JvmField
    val WIND_GENERATOR: BlockEntityType<HTWindGeneratorBlockEntity> =
        register("wind_generator", ::HTWindGeneratorBlockEntity)

    @JvmField
    val BLAZING_BOX: BlockEntityType<HTBlazingBoxBlockEntity> =
        register("blazing_box", ::HTBlazingBoxBlockEntity)

    @JvmField
    val ALCHEMICAL_INFUSER: BlockEntityType<HTAlchemicalInfuserBlockEntity> =
        register("alchemical_infuser", ::HTAlchemicalInfuserBlockEntity)

    @JvmField
    val ITEM_DISPLAY: BlockEntityType<HTItemDisplayBlockEntity> =
        register("item_display", ::HTItemDisplayBlockEntity)

    private fun <T : HTBaseBlockEntity> register(name: String, factory: BlockEntityType.BlockEntityFactory<T>): BlockEntityType<T> =
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Ragium.id(name),
            BlockEntityType.Builder.create(factory).build(),
        )
}
