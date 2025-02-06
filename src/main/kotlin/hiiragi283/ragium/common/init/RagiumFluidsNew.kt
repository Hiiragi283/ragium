package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object RagiumFluidsNew {
    @JvmField
    val REGISTER: DeferredRegister<Fluid> = DeferredRegister.create(Registries.FLUID, RagiumAPI.MOD_ID)

    @JvmField
    val HONEY: DeferredHolder<Fluid, out BaseFlowingFluid> = virtual("honey", RagiumFluidTypes.HONEY)

    @JvmField
    val SNOW: DeferredHolder<Fluid, out BaseFlowingFluid> = virtual("snow", RagiumFluidTypes.SNOW)

    @JvmField
    val CRUDE_OIL: DeferredHolder<Fluid, BaseFlowingFluid.Source> =
        DeferredHolder.create(Registries.FLUID, RagiumAPI.id("crude_oil"))

    @JvmField
    val FLOWING_CRUDE_OIL: DeferredHolder<Fluid, BaseFlowingFluid.Flowing> =
        DeferredHolder.create(Registries.FLUID, RagiumAPI.id("flowing_crude_oil"))

    @JvmStatic
    fun init() {
        val properties: BaseFlowingFluid.Properties = BaseFlowingFluid
            .Properties(
                RagiumFluidTypes.CRUDE_OIL,
                CRUDE_OIL,
                FLOWING_CRUDE_OIL,
            ).block(RagiumBlocks.CRUDE_OIL)
            .bucket(RagiumItems.CRUDE_OIL_BUCKET)
        REGISTER.register(CRUDE_OIL.id.path) { _: ResourceLocation -> BaseFlowingFluid.Source(properties) }
        REGISTER.register(FLOWING_CRUDE_OIL.id.path) { _: ResourceLocation -> BaseFlowingFluid.Flowing(properties) }
    }

    @JvmStatic
    private fun virtual(
        name: String,
        typeHolder: Supplier<out FluidType>,
        builderAction: BaseFlowingFluid.Properties.() -> Unit = {},
    ): DeferredHolder<Fluid, out BaseFlowingFluid> {
        val stillHolder: DeferredHolder<Fluid, Fluid> = DeferredHolder.create(Registries.FLUID, RagiumAPI.id(name))
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
