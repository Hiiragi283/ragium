package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.extension.negate
import hiiragi283.ragium.api.material.HTItemMaterialVariant
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTFluidContentRegister
import hiiragi283.ragium.common.fluid.HTFluidType
import hiiragi283.ragium.common.material.RagiumMaterialType
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.pathfinder.PathType
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.FluidInteractionRegistry
import net.neoforged.neoforge.fluids.FluidType

object RagiumFluidContents {
    @JvmField
    val REGISTER = HTFluidContentRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(
        name: String,
        properties: FluidType.Properties,
    ): HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        REGISTER.register(name, properties.descriptionId("block.ragium.$name"))

    @JvmStatic
    fun <T : FluidType> register(
        name: String,
        properties: FluidType.Properties,
        typeFactory: (FluidType.Properties) -> T,
    ): HTFluidContent<T, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        REGISTER.register(name, properties.descriptionId("block.ragium.$name"), typeFactory)

    @JvmStatic
    private fun properties(): FluidType.Properties = FluidType.Properties
        .create()
        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)

    @JvmStatic
    private fun molten(): FluidType.Properties = FluidType.Properties
        .create()
        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)

    @JvmStatic
    private fun gaseous(): FluidType.Properties = properties().density(-1000)

    //    Vanilla    //

    @JvmField
    val HONEY: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("honey", properties())

    @JvmField
    val EXPERIENCE: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("experience", properties())

    @JvmField
    val MUSHROOM_STEW: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("mushroom_stew", properties())

    //    Oil    //

    @JvmField
    val CRUDE_OIL: HTFluidContent<HTFluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register(
        "crude_oil",
        molten()
            .canSwim(false)
            .pathType(PathType.LAVA)
            .density(3000)
            .viscosity(6000)
            .motionScale(0.0001),
        HTFluidType.explosive(2f),
    )

    @JvmField
    val NATURAL_GAS: HTFluidContent<HTFluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register(
        "natural_gas",
        gaseous(),
        HTFluidType.explosive(4f),
    )

    @JvmField
    val NAPHTHA: HTFluidContent<HTFluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register(
        "naphtha",
        properties(),
        HTFluidType.explosive(3f),
    )

    @JvmField
    val FUEL: HTFluidContent<HTFluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = register(
        "fuel",
        properties(),
        HTFluidType.explosive(4f),
    )

    @JvmField
    val CRIMSON_FUEL: HTFluidContent<HTFluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register(
            "crimson_fuel",
            properties(),
            HTFluidType.explosive(6f),
        )

    @JvmField
    val LUBRICANT: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("lubricant", properties())

    //    Sap    //

    @JvmField
    val SAP: HTFluidContent<HTFluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("sap", properties(), HTFluidType.solidify(HTResultHelper.INSTANCE.item(Items.SLIME_BALL)))

    @JvmField
    val CRIMSON_SAP: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("crimson_sap", properties())

    @JvmField
    val WARPED_SAP: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("warped_sap", properties())

    //    Molten    //

    @JvmField
    val GILDED_LAVA: HTFluidContent<HTFluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register(
            "gilded_lava",
            molten(),
            HTFluidType.create {
                canVaporize = HTFluidType.IS_ULTRA_WARM
                interactLevel = { level: Level, pos: BlockPos ->
                    level.setBlockAndUpdate(pos, Blocks.GILDED_BLACKSTONE.defaultBlockState())
                }
            },
        )

    @JvmField
    val CRIMSON_BLOOD: HTFluidContent<HTFluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register(
            "crimson_blood",
            molten(),
            HTFluidType.create {
                canVaporize = HTFluidType.IS_ULTRA_WARM.negate()
                dropItem = HTResultHelper.INSTANCE.item(HTItemMaterialVariant.GEM, RagiumMaterialType.CRIMSON_CRYSTAL)
            },
        )

    @JvmField
    val DEW_OF_THE_WARP: HTFluidContent<HTFluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register(
            "dew_of_the_warp",
            molten(),
            HTFluidType.create {
                canVaporize = HTFluidType.IS_ULTRA_WARM.negate()
                dropItem = HTResultHelper.INSTANCE.item(HTItemMaterialVariant.GEM, RagiumMaterialType.WARPED_CRYSTAL)
            },
        )

    @JvmField
    val ELDRITCH_FLUX: HTFluidContent<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> =
        register("eldritch_flux", molten())

    //    Interaction    //

    @JvmStatic
    private fun registerInteraction(content: HTFluidContent<*, *, *>, other: FluidType, getter: (FluidState) -> BlockState) {
        FluidInteractionRegistry.addInteraction(
            content.getType(),
            FluidInteractionRegistry.InteractionInformation(other, getter),
        )
    }

    @JvmStatic
    private fun registerInteraction(
        content: HTFluidContent<*, *, *>,
        other: FluidType,
        toSource: BlockState,
        toFlowing: BlockState,
    ) {
        registerInteraction(content, other) { state: FluidState ->
            when (state.isSource) {
                true -> toSource
                false -> toFlowing
            }
        }
    }

    @JvmStatic
    fun registerInteractions() {
        registerInteraction(
            CRUDE_OIL,
            HTFluidContent.LAVA.getType(),
            Blocks.AIR.defaultBlockState(),
            Blocks.SOUL_SAND.defaultBlockState(),
        )

        registerInteraction(
            HTFluidContent.WATER,
            ELDRITCH_FLUX.getType(),
            Blocks.AIR.defaultBlockState(),
            RagiumBlocks.ELDRITCH_STONE.get().defaultBlockState(),
        )
    }
}
