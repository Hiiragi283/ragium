package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.block.entity.*
import hiiragi283.ragium.common.block.entity.generator.*
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumBlockEntityTypes {
    @JvmField
    val ALCHEMICAL_INFUSER: BlockEntityType<HTAlchemicalInfuserBlockEntity> =
        register("alchemical_infuser", ::HTAlchemicalInfuserBlockEntity)

    @JvmField
    val BUFFER: BlockEntityType<HTBufferBlockEntity> =
        register("buffer", ::HTBufferBlockEntity)

    @JvmField
    val BLAZING_BOX: BlockEntityType<HTBlazingBoxBlockEntity> =
        register("blazing_box", ::HTBlazingBoxBlockEntity)

    @JvmField
    val BURNING_BOX: BlockEntityType<HTBurningBoxBlockEntity> =
        register("burning_box", ::HTBurningBoxBlockEntity)

    @JvmField
    val CREATIVE_SOURCE: BlockEntityType<HTCreativeSourceBlockEntity> =
        register("creative_source", ::HTCreativeSourceBlockEntity)

    @JvmField
    val ITEM_DISPLAY: BlockEntityType<HTItemDisplayBlockEntity> =
        register("item_display", ::HTItemDisplayBlockEntity)

    @JvmField
    val MANUAL_GRINDER: BlockEntityType<HTManualGrinderBlockEntity> =
        register("manual_grinder", ::HTManualGrinderBlockEntity)

    @JvmField
    val WATER_COLLECTOR: BlockEntityType<HTWaterCollectorBlockEntity> =
        register("water_collector", ::HTWaterCollectorBlockEntity)

    @JvmField
    val WATER_GENERATOR: BlockEntityType<HTWaterGeneratorBlockEntity> =
        register("water_generator", ::HTWaterGeneratorBlockEntity)

    @JvmField
    val WIND_GENERATOR: BlockEntityType<HTWindGeneratorBlockEntity> =
        register("wind_generator", ::HTWindGeneratorBlockEntity)

    @JvmStatic
    private fun <T : HTBaseBlockEntity> register(name: String, factory: BlockEntityType.BlockEntityFactory<T>): BlockEntityType<T> =
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Ragium.id(name),
            BlockEntityType.Builder.create(factory).build(),
        )

    @JvmStatic
    fun init() {
        ALCHEMICAL_INFUSER.addSupportedBlock(RagiumContents.ALCHEMICAL_INFUSER)
        ITEM_DISPLAY.addSupportedBlock(RagiumContents.ITEM_DISPLAY)
    }
}
