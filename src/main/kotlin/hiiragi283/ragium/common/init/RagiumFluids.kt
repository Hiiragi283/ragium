package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredFluid
import hiiragi283.ragium.api.registry.HTDeferredFluidType
import hiiragi283.ragium.api.registry.HTFluidRegister
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.FluidType

object RagiumFluids {
    @JvmField
    val REGISTER = HTFluidRegister(RagiumAPI.MOD_ID)

    @JvmField
    val GLASS: HTDeferredFluid<out BaseFlowingFluid> = virtual("glass", RagiumFluidTypes.GLASS)

    @JvmField
    val HONEY: HTDeferredFluid<out BaseFlowingFluid> = virtual("honey", RagiumFluidTypes.HONEY) {
        bucket(RagiumItems.HONEY_BUCKET)
    }

    @JvmField
    val SNOW: HTDeferredFluid<out BaseFlowingFluid> = virtual("snow", RagiumFluidTypes.SNOW) {
        bucket(Items::POWDER_SNOW_BUCKET)
    }

    @JvmField
    val CRUDE_OIL: HTDeferredFluid<BaseFlowingFluid.Source> =
        HTDeferredFluid.createFluid<BaseFlowingFluid.Source>(RagiumAPI.id("crude_oil"))

    @JvmField
    val FLOWING_CRUDE_OIL: HTDeferredFluid<BaseFlowingFluid.Flowing> =
        HTDeferredFluid.createFluid<BaseFlowingFluid.Flowing>(RagiumAPI.id("flowing_crude_oil"))

    @JvmStatic
    fun init() {
        // Crude Oil
        val properties: BaseFlowingFluid.Properties = BaseFlowingFluid
            .Properties(
                RagiumFluidTypes.CRUDE_OIL,
                CRUDE_OIL,
                FLOWING_CRUDE_OIL,
            ).block(RagiumBlocks.CRUDE_OIL)
            .bucket(RagiumItems.CRUDE_OIL_BUCKET)
        REGISTER.register(CRUDE_OIL.id.path) { _: ResourceLocation -> BaseFlowingFluid.Source(properties) }
        REGISTER.register(FLOWING_CRUDE_OIL.id.path) { _: ResourceLocation -> BaseFlowingFluid.Flowing(properties) }
        // Virtual
        RagiumVirtualFluids.entries.forEach { fluid: RagiumVirtualFluids ->
            // Fluid Type
            RagiumFluidTypes.register(fluid.serializedName, FluidType.Properties.create())
            // Fluid
            virtual(fluid.serializedName, fluid.typeHolder)
        }
    }

    @JvmStatic
    private fun virtual(
        name: String,
        typeHolder: HTDeferredFluidType<out FluidType>,
        builderAction: BaseFlowingFluid.Properties.() -> Unit = {},
    ): HTDeferredFluid<out BaseFlowingFluid> {
        val stillHolder: HTDeferredFluid<Fluid> = HTDeferredFluid.createFluid<Fluid>(RagiumAPI.id(name))
        return REGISTER.register(name) { _: ResourceLocation ->
            BaseFlowingFluid.Source(
                BaseFlowingFluid
                    .Properties(
                        typeHolder,
                        stillHolder,
                        stillHolder,
                    ).apply(builderAction),
            )
        }
    }
}
