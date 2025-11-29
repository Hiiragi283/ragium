package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.function.BlockWithContextFactory
import hiiragi283.ragium.api.function.partially1
import hiiragi283.ragium.api.item.alchemy.HTMobEffectInstance
import hiiragi283.ragium.api.registry.HTBasicFluidContent
import hiiragi283.ragium.api.registry.HTFluidContentRegister
import hiiragi283.ragium.common.block.fluid.HTEffectLiquidBlock
import hiiragi283.ragium.common.block.fluid.HTMagicalLiquidBlock
import hiiragi283.ragium.common.block.fluid.HTMagmaticLiquidBlock
import hiiragi283.ragium.common.fluid.HTEndFluidType
import hiiragi283.ragium.common.fluid.HTExplosiveFluidType
import hiiragi283.ragium.common.fluid.HTNetherFluidType
import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.pathfinder.PathType
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.FluidInteractionRegistry
import net.neoforged.neoforge.fluids.FluidType
import java.util.function.UnaryOperator

object RagiumFluidContents {
    @JvmField
    val REGISTER = HTFluidContentRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun registerSimple(
        name: String,
        properties: FluidType.Properties,
        blockProperties: UnaryOperator<BlockBehaviour.Properties> = UnaryOperator.identity(),
    ): HTBasicFluidContent = REGISTER.registerSimple(name, properties.descriptionId("block.ragium.$name"), blockProperties)

    @JvmStatic
    fun <TYPE : FluidType, BLOCK : LiquidBlock> register(
        name: String,
        properties: FluidType.Properties,
        typeFactory: (FluidType.Properties) -> TYPE,
        blockFactory: BlockWithContextFactory<BaseFlowingFluid.Source, BLOCK>,
        blockProperties: UnaryOperator<BlockBehaviour.Properties> = UnaryOperator.identity(),
    ): HTBasicFluidContent =
        REGISTER.register(name, properties.descriptionId("block.ragium.$name"), typeFactory, blockFactory, blockProperties)

    @JvmStatic
    fun <TYPE : FluidType> registerEffected(
        name: String,
        properties: FluidType.Properties,
        typeFactory: (FluidType.Properties) -> TYPE,
        effect: Holder<MobEffect>,
        amplifier: Int = 0,
        duration: Int = 5 * 20,
        blockProperties: UnaryOperator<BlockBehaviour.Properties> = UnaryOperator.identity(),
    ): HTBasicFluidContent = register(
        name,
        properties,
        typeFactory,
        ::HTEffectLiquidBlock.partially1(HTMobEffectInstance(effect, duration, amplifier)),
        blockProperties,
    )

    @JvmStatic
    fun registerEffected(
        name: String,
        properties: FluidType.Properties,
        effect: Holder<MobEffect>,
        amplifier: Int = 0,
        duration: Int = 5 * 20,
        blockProperties: UnaryOperator<BlockBehaviour.Properties> = UnaryOperator.identity(),
    ): HTBasicFluidContent = registerEffected(name, properties, ::FluidType, effect, amplifier, duration, blockProperties)

    @JvmStatic
    fun <TYPE : FluidType> register(
        name: String,
        properties: FluidType.Properties,
        typeFactory: (FluidType.Properties) -> TYPE,
        blockProperties: UnaryOperator<BlockBehaviour.Properties> = UnaryOperator.identity(),
    ): HTBasicFluidContent = REGISTER.register(name, properties.descriptionId("block.ragium.$name"), typeFactory, blockProperties)

    @JvmStatic
    private fun liquid(): FluidType.Properties = FluidType.Properties
        .create()
        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)

    @JvmStatic
    private fun molten(temp: Int = 1300): FluidType.Properties = FluidType.Properties
        .create()
        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
        .temperature(temp)

    @JvmStatic
    private fun gaseous(): FluidType.Properties = liquid().density(-1000)

    //    Foods    //

    @JvmField
    val HONEY: HTBasicFluidContent = registerSimple("honey", liquid()) { it.speedFactor(0.4f) }

    @JvmField
    val MUSHROOM_STEW: HTBasicFluidContent = registerEffected("mushroom_stew", liquid(), MobEffects.SATURATION)

    @JvmField
    val CREAM: HTBasicFluidContent = registerSimple("cream", liquid()) { it.speedFactor(0.8f) }

    @JvmField
    val CHOCOLATE: HTBasicFluidContent = registerSimple("chocolate", liquid()) { it.speedFactor(0.8f) }

    @JvmField
    val RAGI_CHERRY_JUICE: HTBasicFluidContent = registerSimple("ragi_cherry_juice", liquid())

    //    Organic    //

    @JvmField
    val SLIME: HTBasicFluidContent = registerEffected("slime", liquid(), MobEffects.OOZING) { it.speedFactor(0.4f) }

    @JvmField
    val GELLED_EXPLOSIVE: HTBasicFluidContent = registerSimple("gelled_explosive", liquid())

    @JvmField
    val ORGANIC_MUTAGEN: HTBasicFluidContent = registerEffected("organic_mutagen", liquid(), MobEffects.POISON, 4)

    //    Oil    //

    @JvmField
    val CRUDE_OIL: HTBasicFluidContent = register(
        "crude_oil",
        molten()
            .canSwim(false)
            .pathType(PathType.LAVA)
            .density(3000)
            .viscosity(6000)
            .motionScale(0.0001),
        ::HTExplosiveFluidType.partially1(2f),
    ) { it.speedFactor(0.4f) }

    @JvmField
    val NATURAL_GAS: HTBasicFluidContent =
        registerEffected("natural_gas", gaseous(), ::HTExplosiveFluidType.partially1(4f), MobEffects.LEVITATION)

    @JvmField
    val NAPHTHA: HTBasicFluidContent = register("naphtha", liquid(), ::HTExplosiveFluidType.partially1(3f))

    @JvmField
    val LUBRICANT: HTBasicFluidContent = registerSimple("lubricant", liquid()) { it.speedFactor(0.8f) }

    //    Fuel    //

    @JvmField
    val FUEL: HTBasicFluidContent = register("fuel", liquid(), ::HTExplosiveFluidType.partially1(4f))

    @JvmField
    val CRIMSON_FUEL: HTBasicFluidContent = register("crimson_fuel", liquid(), ::HTExplosiveFluidType.partially1(6f))

    //    Sap    //

    @JvmField
    val SAP: HTBasicFluidContent = registerSimple("sap", liquid())

    @JvmField
    val CRIMSON_SAP: HTBasicFluidContent = register("crimson_sap", liquid(), ::HTNetherFluidType)

    @JvmField
    val WARPED_SAP: HTBasicFluidContent = register("warped_sap", liquid(), ::HTNetherFluidType)

    //    Molten    //

    @JvmField
    val DESTABILIZED_RAGINITE: HTBasicFluidContent = registerSimple("destabilized_raginite", molten())

    @JvmField
    val CRIMSON_BLOOD: HTBasicFluidContent =
        register("crimson_blood", molten(), ::HTNetherFluidType, ::HTMagmaticLiquidBlock)

    @JvmField
    val DEW_OF_THE_WARP: HTBasicFluidContent =
        register("dew_of_the_warp", molten(), ::HTNetherFluidType, ::HTMagmaticLiquidBlock)

    @JvmField
    val ELDRITCH_FLUX: HTBasicFluidContent =
        register("eldritch_flux", molten(), ::HTEndFluidType, ::HTMagicalLiquidBlock)

    //    Misc    //

    @JvmField
    val EXPERIENCE: HTBasicFluidContent = registerEffected("experience", liquid(), MobEffects.HUNGER, 63)

    @JvmField
    val SULFURIC_ACID: HTBasicFluidContent = registerEffected("sulfuric_acid", liquid(), MobEffects.WITHER)

    @JvmField
    val COOLANT: HTBasicFluidContent = registerSimple("coolant", liquid())

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
