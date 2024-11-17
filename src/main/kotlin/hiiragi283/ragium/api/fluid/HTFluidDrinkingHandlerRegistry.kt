package hiiragi283.ragium.api.fluid

import hiiragi283.ragium.common.init.RagiumFluids
import net.minecraft.fluid.Fluid

object HTFluidDrinkingHandlerRegistry {
    @JvmStatic
    private val registry: MutableMap<Fluid, HTFluidDrinkingHandler> = mutableMapOf()

    @JvmStatic
    fun register(fluid: RagiumFluids, handler: HTFluidDrinkingHandler) {
        register(fluid.value, handler)
    }

    @JvmStatic
    fun register(fluid: Fluid, handler: HTFluidDrinkingHandler) {
        registry[fluid] = handler
    }

    @JvmStatic
    fun get(fluid: Fluid): HTFluidDrinkingHandler? = registry[fluid]
}
