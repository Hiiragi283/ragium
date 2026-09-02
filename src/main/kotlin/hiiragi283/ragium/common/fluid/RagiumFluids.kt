package hiiragi283.ragium.common.fluid

import hiiragi283.lib.HTConstants
import hiiragi283.lib.color.HTColoredCollection
import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.registry.HTFluidContentRegister
import hiiragi283.lib.resource.toId
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.item.HTPotionBucketItem
import hiiragi283.ragium.common.item.component.RagiumConsumables
import net.minecraft.core.component.DataComponents
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.Items
import net.minecraft.world.level.pathfinder.PathType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.fluids.FluidType

data object RagiumFluids {
    @JvmField
    val REGISTER = HTFluidContentRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    @JvmStatic
    private fun create(fill: SoundEvent, empty: SoundEvent): FluidType.Properties = FluidType.Properties
        .create()
        .sound(SoundActions.BUCKET_FILL, fill)
        .sound(SoundActions.BUCKET_EMPTY, empty)

    @JvmStatic
    private fun liquid(): FluidType.Properties = create(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)

    @JvmStatic
    private fun gaseous(): FluidType.Properties =
        liquid().canSwim(false).canExtinguish(false).supportsBoating(false).density(-1000)

    @JvmStatic
    private fun molten(lightLevel: Int = 15, temp: Int = 1300): FluidType.Properties =
        create(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA)
            .lightLevel(lightLevel)
            .temperature(temp)

    //    Vanilla    //

    @JvmField
    val POTION: HTFluidContent.Virtual = REGISTER.registerVirtual(HTConstants.POTION) {
        properties = liquid()
        typeFactory = ::HTPotionFluidType
        bucketFactory = ::HTPotionBucketItem
        bucketProperties = {
            it
                .component(DataComponents.CONSUMABLE, RagiumConsumables.FLUID_BUCKET)
                .component(DataComponents.POTION_DURATION_SCALE, 4f)
                .usingConvertsTo(Items.BUCKET)
        }
    }

    @JvmField
    val DYES: HTColoredCollection<HTFluidContent.Flowing> = HTColoredCollection { color: HTDefaultColor ->
        val name: String = color.serializedName
        REGISTER.registerFlowing("${name}_dye") {
            properties = liquid()
            typeFactory = { prop: FluidType.Properties -> HTDyedFluidType(color, prop) }
            fluidTag = HTConstants.COMMON.toId("dyes", name)
            bucketTag = HTConstants.COMMON.toId("buckets", "dye", name)
        }
    }

    @JvmField
    val HONEY: HTFluidContent.Flowing = REGISTER.registerFlowing("honey") {
        properties = create(SoundEvents.HONEY_BLOCK_PLACE, SoundEvents.HONEY_BLOCK_BREAK)
    }

    @JvmField
    val OMINOUS_FLUX: HTFluidContent.Flowing = REGISTER.registerFlowing("ominous_flux") { properties = molten() }

    @JvmField
    val MOLTEN_GLASS: HTFluidContent.Virtual = REGISTER.registerVirtual("molten_glass") { properties = molten() }

    @JvmField
    val MOLTEN_REDSTONE: HTFluidContent.Virtual = REGISTER.registerVirtual("molten_redstone") { properties = molten() }

    @JvmField
    val MOLTEN_GLOWSTONE: HTFluidContent.Virtual = REGISTER.registerVirtual("molten_glowstone") {
        properties = molten()
    }

    @JvmField
    val MOLTEN_ENDER: HTFluidContent.Virtual = REGISTER.registerVirtual("molten_ender") { properties = molten() }

    @JvmField
    val MOLTEN_BLAZE: HTFluidContent.Virtual = REGISTER.registerVirtual("molten_blaze") { properties = molten() }

    //    Element    //

    // 1st
    @JvmField
    val HYDROGEN: HTFluidContent.Virtual = REGISTER.registerVirtual("hydrogen") { properties = gaseous() }

    // 3rd
    @JvmField
    val OXYGEN: HTFluidContent.Virtual = REGISTER.registerVirtual("oxygen") { properties = gaseous() }

    // 4th
    @JvmField
    val CHLORINE: HTFluidContent.Virtual = REGISTER.registerVirtual("chloride") { properties = gaseous() }

    //    Chemical    //

    // 3rd
    @JvmField
    val CREOSOTE: HTFluidContent.Flowing = REGISTER.registerFlowing("creosote") { properties = liquid() }

    @JvmField
    val CRUDE_OIL: HTFluidContent.Flowing = REGISTER.registerFlowing("crude_oil") {
        properties = molten()
            .canSwim(false)
            .pathType(PathType.LAVA)
            .density(3000)
            .viscosity(6000)
            .motionScale(0.0001)
        typeFactory = { HTExplosiveFluidType(2f, it) }

        blockProperties = { it.speedFactor(0.4f) }
    }

    @JvmField
    val NAPHTHA: HTFluidContent.Flowing = REGISTER.registerFlowing("naphtha") {
        properties = liquid()
        typeFactory = { HTExplosiveFluidType(3f, it) }
    }

    @JvmField
    val FUEL: HTFluidContent.Flowing = REGISTER.registerFlowing("fuel") {
        properties = liquid()
        typeFactory = { HTExplosiveFluidType(4f, it) }
    }

    @JvmField
    val AROMATIC_COMPOUND: HTFluidContent.Flowing = REGISTER.registerFlowing("aromatic_compound") {
        properties = liquid()
        typeFactory = { HTExplosiveFluidType(3f, it) }
    }

    // 4th
    @JvmField
    val NAOH_SOLUTION: HTFluidContent.Virtual = REGISTER.registerVirtual("sodium_hydroxide_solution") {
        properties =
            liquid()
    }

    @JvmField
    val SULFUR_DIOXIDE: HTFluidContent.Virtual = REGISTER.registerVirtual("sulfur_dioxide") { properties = gaseous() }

    @JvmField
    val SULFUR_TRIOXIDE: HTFluidContent.Virtual = REGISTER.registerVirtual("sulfur_trioxide") { properties = gaseous() }

    @JvmField
    val SULFURIC_ACID: HTFluidContent.Virtual = REGISTER.registerVirtual("sulfuric_acid") { properties = liquid() }

    @JvmField
    val HYDROGEN_CHLORIDE: HTFluidContent.Virtual = REGISTER.registerVirtual("hydrogen_chloride") {
        properties =
            gaseous()
    }

    @JvmField
    val HYDROCHLORIC_ACID: HTFluidContent.Virtual = REGISTER.registerVirtual("hydrochloric_acid") {
        properties =
            liquid()
    }

    // 5th
    @JvmField
    val CAOH_SOLUTION: HTFluidContent.Virtual = REGISTER.registerVirtual("calcium_hydroxide_solution") {
        properties =
            liquid()
    }
}
