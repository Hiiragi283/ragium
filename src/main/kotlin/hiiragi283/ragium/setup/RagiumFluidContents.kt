package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.function.negate
import hiiragi283.ragium.api.registry.HTBasicFluidContentNew
import hiiragi283.ragium.api.registry.HTFluidContentRegister
import hiiragi283.ragium.common.fluid.HTFluidType
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.pathfinder.PathType
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.fluids.FluidInteractionRegistry
import net.neoforged.neoforge.fluids.FluidType

object RagiumFluidContents {
    @JvmField
    val REGISTER = HTFluidContentRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(name: String, properties: FluidType.Properties): HTBasicFluidContentNew =
        REGISTER.registerSimple(name, properties.descriptionId("block.ragium.$name"))

    @JvmStatic
    fun <TYPE : FluidType> register(
        name: String,
        properties: FluidType.Properties,
        typeFactory: (FluidType.Properties) -> TYPE,
    ): HTBasicFluidContentNew = REGISTER.register(name, properties.descriptionId("block.ragium.$name"), typeFactory)

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
    val AWKWARD_WATER: HTBasicFluidContentNew = register("awkward_water", liquid())

    @JvmField
    val HONEY: HTBasicFluidContentNew = register("honey", liquid())

    @JvmField
    val EXPERIENCE: HTBasicFluidContentNew = register("experience", liquid())

    @JvmField
    val MUSHROOM_STEW: HTBasicFluidContentNew = register("mushroom_stew", liquid())

    //    Organic    //

    @JvmField
    val CREAM: HTBasicFluidContentNew = register("cream", liquid())

    @JvmField
    val CHOCOLATE: HTBasicFluidContentNew = register("chocolate", liquid())

    @JvmField
    val RAGI_CHERRY_JUICE: HTBasicFluidContentNew = register("ragi_cherry_juice", liquid())

    @JvmField
    val ORGANIC_MUTAGEN: HTBasicFluidContentNew = register("organic_mutagen", liquid())

    //    Oil    //

    @JvmField
    val CRUDE_OIL: HTBasicFluidContentNew = register(
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
    val NATURAL_GAS: HTBasicFluidContentNew = register("natural_gas", gaseous(), HTFluidType.explosive(4f))

    @JvmField
    val NAPHTHA: HTBasicFluidContentNew = register("naphtha", liquid(), HTFluidType.explosive(3f))

    @JvmField
    val LUBRICANT: HTBasicFluidContentNew = register("lubricant", liquid())

    //    Fuel    //

    @JvmField
    val FUEL: HTBasicFluidContentNew = register("fuel", liquid(), HTFluidType.explosive(4f))

    @JvmField
    val CRIMSON_FUEL: HTBasicFluidContentNew = register("crimson_fuel", liquid(), HTFluidType.explosive(6f))

    @JvmField
    val GREEN_FUEL: HTBasicFluidContentNew = register("green_fuel", liquid())

    //    Sap    //

    @JvmField
    val SAP: HTBasicFluidContentNew = register("sap", liquid(), HTFluidType.solidify(HTResultHelper.item(Items.SLIME_BALL)))

    @JvmField
    val CRIMSON_SAP: HTBasicFluidContentNew = register("crimson_sap", liquid())

    @JvmField
    val WARPED_SAP: HTBasicFluidContentNew = register("warped_sap", liquid())

    //    Molten    //

    @JvmField
    val CRIMSON_BLOOD: HTBasicFluidContentNew =
        register(
            "crimson_blood",
            molten(),
            HTFluidType.create {
                canVaporize = HTFluidType.IS_ULTRA_WARM.negate()
                dropItem = HTResultHelper.item(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.CRIMSON_CRYSTAL)
            },
        )

    @JvmField
    val DEW_OF_THE_WARP: HTBasicFluidContentNew =
        register(
            "dew_of_the_warp",
            molten(),
            HTFluidType.create {
                canVaporize = HTFluidType.IS_ULTRA_WARM.negate()
                dropItem = HTResultHelper.item(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.WARPED_CRYSTAL)
            },
        )

    @JvmField
    val ELDRITCH_FLUX: HTBasicFluidContentNew = register("eldritch_flux", molten())

    //    Chemicals    //

    @JvmField
    val NITRIC_ACID: HTBasicFluidContentNew = register("nitric_acid", liquid())

    @JvmField
    val SULFURIC_ACID: HTBasicFluidContentNew = register("sulfuric_acid", liquid())

    @JvmField
    val MIXTURE_ACID: HTBasicFluidContentNew = register("mixture_acid", liquid())

    //    Interaction    //

    @JvmStatic
    private fun registerInteraction(content: FluidType, other: FluidType, getter: (FluidState) -> BlockState) {
        FluidInteractionRegistry.addInteraction(
            content,
            FluidInteractionRegistry.InteractionInformation(other, getter),
        )
    }

    @JvmStatic
    private fun registerInteraction(
        content: FluidType,
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
            CRUDE_OIL.getType(),
            NeoForgeMod.LAVA_TYPE.value(),
            Blocks.AIR.defaultBlockState(),
            Blocks.SOUL_SAND.defaultBlockState(),
        )

        registerInteraction(
            NeoForgeMod.WATER_TYPE.value(),
            ELDRITCH_FLUX.getType(),
            Blocks.AIR.defaultBlockState(),
            RagiumBlocks.ELDRITCH_STONE.get().defaultBlockState(),
        )
    }
}
