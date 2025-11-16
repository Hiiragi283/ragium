package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.function.negate
import hiiragi283.ragium.api.registry.HTFlowingFluidContent
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTFluidContentRegister
import hiiragi283.ragium.api.registry.HTSimpleFluidContent
import hiiragi283.ragium.common.fluid.HTFluidType
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.pathfinder.PathType
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.fluids.FluidInteractionRegistry
import net.neoforged.neoforge.fluids.FluidType

object RagiumFluidContents {
    @JvmField
    val REGISTER = HTFluidContentRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(name: String, properties: FluidType.Properties): HTSimpleFluidContent =
        REGISTER.register(name, properties.descriptionId("block.ragium.$name"))

    @JvmStatic
    fun <T : FluidType> register(
        name: String,
        properties: FluidType.Properties,
        typeFactory: (FluidType.Properties) -> T,
    ): HTFlowingFluidContent<T> = REGISTER.register(name, properties.descriptionId("block.ragium.$name"), typeFactory)

    @JvmStatic
    private fun liquid(): FluidType.Properties = FluidType.Properties
        .create()
        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)

    @JvmStatic
    private fun molten(): FluidType.Properties = FluidType.Properties
        .create()
        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)

    @JvmStatic
    private fun gaseous(): FluidType.Properties = liquid().density(-1000)

    //    Vanilla    //

    @JvmField
    val AWKWARD_WATER: HTSimpleFluidContent = register("awkward_water", liquid())

    @JvmField
    val HONEY: HTSimpleFluidContent = register("honey", liquid())

    @JvmField
    val EXPERIENCE: HTSimpleFluidContent = register("experience", liquid())

    @JvmField
    val MUSHROOM_STEW: HTSimpleFluidContent = register("mushroom_stew", liquid())

    //    Organic    //

    @JvmField
    val CHOCOLATE: HTSimpleFluidContent = register("chocolate", liquid())

    @JvmField
    val MEAT: HTSimpleFluidContent = register("meat", liquid())

    @JvmField
    val ORGANIC_MUTAGEN: HTSimpleFluidContent = register("organic_mutagen", liquid())

    //    Oil    //

    @JvmField
    val CRUDE_OIL: HTSimpleFluidContent = register(
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
    val NATURAL_GAS: HTSimpleFluidContent = register("natural_gas", gaseous(), HTFluidType.explosive(4f))

    @JvmField
    val NAPHTHA: HTSimpleFluidContent = register("naphtha", liquid(), HTFluidType.explosive(3f))

    @JvmField
    val LUBRICANT: HTSimpleFluidContent = register("lubricant", liquid())

    //    Fuel    //

    @JvmField
    val FUEL: HTSimpleFluidContent = register("fuel", liquid(), HTFluidType.explosive(4f))

    @JvmField
    val CRIMSON_FUEL: HTSimpleFluidContent = register("crimson_fuel", liquid(), HTFluidType.explosive(6f))

    @JvmField
    val GREEN_FUEL: HTSimpleFluidContent = register("green_fuel", liquid())

    //    Sap    //

    @JvmField
    val SAP: HTSimpleFluidContent = register("sap", liquid(), HTFluidType.solidify(HTResultHelper.item(Items.SLIME_BALL)))

    @JvmField
    val CRIMSON_SAP: HTSimpleFluidContent = register("crimson_sap", liquid())

    @JvmField
    val WARPED_SAP: HTSimpleFluidContent = register("warped_sap", liquid())

    //    Molten    //

    @JvmField
    val CRIMSON_BLOOD: HTSimpleFluidContent =
        register(
            "crimson_blood",
            molten(),
            HTFluidType.create {
                canVaporize = HTFluidType.IS_ULTRA_WARM.negate()
                dropItem = HTResultHelper.item(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.CRIMSON_CRYSTAL)
            },
        )

    @JvmField
    val DEW_OF_THE_WARP: HTSimpleFluidContent =
        register(
            "dew_of_the_warp",
            molten(),
            HTFluidType.create {
                canVaporize = HTFluidType.IS_ULTRA_WARM.negate()
                dropItem = HTResultHelper.item(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.WARPED_CRYSTAL)
            },
        )

    @JvmField
    val ELDRITCH_FLUX: HTSimpleFluidContent = register("eldritch_flux", molten())

    //    Chemicals    //

    // val NITRIC_ACID: HTSimpleFluidContent = register("nitric_acid", liquid())

    // val SULFURIC_ACID: HTSimpleFluidContent = register("sulfuric_acid", liquid())

    // val MIXTURE_ACID: HTSimpleFluidContent = register("mixture_acid", liquid())

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
