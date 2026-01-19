package hiiragi283.ragium.setup

import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.item.alchemy.HTMobEffectInstance
import hiiragi283.core.common.block.HTEffectLiquidBlock
import hiiragi283.core.common.fluid.HTExplosiveFluidType
import hiiragi283.core.common.registry.register.HTFluidContentRegister
import hiiragi283.core.common.registry.register.HTSimpleFluidContent
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.pathfinder.PathType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.fluids.FluidType
import java.util.function.UnaryOperator

object RagiumFluids {
    @JvmField
    val REGISTER = HTFluidContentRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    //    Natural    //

    //    Organic    //

    @JvmStatic
    private fun slimy(): FluidType.Properties = create(SoundEvents.SLIME_BLOCK_PLACE, SoundEvents.SLIME_BLOCK_BREAK)

    @JvmField
    val CRUDE_BIO: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("crude_bio", slimy())

    @JvmField
    val ETHANOL: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("ethanol", liquid())

    @JvmField
    val PLANT_OIL: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("plant_oil", liquid())

    @JvmField
    val BIOFUEL: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("biofuel", liquid())

    @JvmField
    val FERTILIZER: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("fertilizer", liquid())

    //    Oil    //

    @JvmField
    val CRUDE_OIL: HTSimpleFluidContent = REGISTER.registerFlowing(
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
    val NAPHTHA: HTSimpleFluidContent = REGISTER.registerFlowing("naphtha", liquid(), ::HTExplosiveFluidType.partially1(3f))

    @JvmField
    val FUEL: HTSimpleFluidContent = REGISTER.registerFlowing("fuel", liquid(), ::HTExplosiveFluidType.partially1(4f))

    @JvmField
    val LUBRICANT: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("lubricant", liquid()) { it.speedFactor(0.8f) }

    //    Misc    //

    @JvmField
    val MOLTEN_RAGINITE: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("molten_raginite", molten().temperature(1300))

    @JvmField
    val COOLANT: HTSimpleFluidContent = REGISTER.registerSimpleFlowing("coolant", liquid().temperature(273))

    @JvmField
    val CREOSOTE: HTSimpleFluidContent = REGISTER.registerFlowing("creosote", liquid(), ::HTExplosiveFluidType.partially1(2f))

    //    Extensions    //

    @JvmStatic
    private fun create(fill: SoundEvent, empty: SoundEvent): FluidType.Properties = FluidType.Properties
        .create()
        .sound(SoundActions.BUCKET_FILL, fill)
        .sound(SoundActions.BUCKET_EMPTY, empty)

    @JvmStatic
    private fun liquid(): FluidType.Properties = create(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)

    @JvmStatic
    private fun molten(): FluidType.Properties = create(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA)

    @JvmStatic
    fun <TYPE : FluidType> registerEffected(
        name: String,
        properties: FluidType.Properties,
        typeFactory: (FluidType.Properties) -> TYPE,
        effect: Holder<MobEffect>,
        amplifier: Int = 0,
        duration: Int = 5 * 20,
        blockProperties: UnaryOperator<BlockBehaviour.Properties> = UnaryOperator.identity(),
    ): HTSimpleFluidContent = REGISTER.registerFlowing(
        name,
        properties,
        typeFactory,
        ::HTEffectLiquidBlock.partially1(HTMobEffectInstance(effect, duration, amplifier)),
        blockProperties = blockProperties,
    )

    @JvmStatic
    fun registerEffected(
        name: String,
        properties: FluidType.Properties,
        effect: Holder<MobEffect>,
        amplifier: Int = 0,
        duration: Int = 5 * 20,
        blockProperties: UnaryOperator<BlockBehaviour.Properties> = UnaryOperator.identity(),
    ): HTSimpleFluidContent = registerEffected(name, properties, ::FluidType, effect, amplifier, duration, blockProperties)
}
