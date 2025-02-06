package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTFluidContent
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.awt.Color

enum class RagiumFluids(val color: Color, val textureType: TextureType = TextureType.LIQUID) : HTFluidContent {
    // Water
    HYDROGEN(Color(0x000099), TextureType.GASEOUS),
    OXYGEN(Color(0x66cccc), TextureType.GASEOUS),
    STEAM(Color.WHITE, TextureType.GASEOUS),

    // Acid
    SULFURIC_ACID(Color(0xff3300), TextureType.STICKY),
    HYDROFLUORIC_ACID(Color(0xffcc33)),

    // Base
    LAPIS_SOLUTION(Color(0x3333ff)),

    // Oil
    NAPHTHA(Color(0xff9900)),
    FUEL(Color(0xcc6633)),
    NITRO_FUEL(Color(0xff33333)),

    // Bio
    PLANT_OIL(Color(0x99cc33)),
    BIOMASS(Color(0x006600), TextureType.STICKY),
    ETHANOL(Color(0x99ffff)),
    BIODIESEL(Color(0x99ff00)),

    // Saps
    SAP(Color(0x996633), TextureType.STICKY),
    CRIMSON_SAP(Color(0x660000), TextureType.STICKY),
    WARPED_SAP(Color(0x006666), TextureType.STICKY),
    ;

    companion object {
        @JvmField
        val REGISTER: DeferredRegister<Fluid> = DeferredRegister.create(Registries.FLUID, RagiumAPI.MOD_ID)

        @JvmField
        val TYPE_REGISTER: DeferredRegister<FluidType> =
            DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, RagiumAPI.MOD_ID)

        @JvmStatic
        fun register(eventBus: IEventBus) {
            RagiumFluids.entries.forEach { fluid: RagiumFluids ->
                // Fluid Type
                TYPE_REGISTER.register(fluid.serializedName) { _: ResourceLocation -> FluidType(FluidType.Properties.create()) }
                // Fluid
                REGISTER.register(fluid.serializedName) { _: ResourceLocation ->
                    BaseFlowingFluid.Source(
                        BaseFlowingFluid.Properties(
                            fluid.typeHolder,
                            fluid.fluidHolder,
                            fluid.fluidHolder,
                        ),
                    )
                }
            }

            TYPE_REGISTER.register(eventBus)
            REGISTER.register(eventBus)
        }
    }

    private val id: ResourceLocation = RagiumAPI.id(serializedName)

    override val fluidHolder: DeferredHolder<Fluid, FlowingFluid> = DeferredHolder.create(Registries.FLUID, id)
    override val typeHolder: DeferredHolder<FluidType, FluidType> =
        DeferredHolder.create(NeoForgeRegistries.Keys.FLUID_TYPES, id)

    //    TextureType    //

    enum class TextureType {
        GASEOUS,
        LIQUID,
        STICKY,
    }

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()
}
