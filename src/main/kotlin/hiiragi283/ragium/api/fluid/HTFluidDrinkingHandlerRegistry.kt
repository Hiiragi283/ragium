package hiiragi283.ragium.api.fluid

import hiiragi283.ragium.common.RagiumContents
import net.minecraft.entity.LivingEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.world.World

object HTFluidDrinkingHandlerRegistry {
    @JvmStatic
    private val registry: MutableMap<Fluid, HTFluidDrinkingHandler> = mutableMapOf()

    @JvmStatic
    fun register(fluid: RagiumContents.Fluids, handler: HTFluidDrinkingHandler) {
        register(fluid.asFluid(), handler)
    }

    @JvmStatic
    fun register(fluid: Fluid, handler: HTFluidDrinkingHandler) {
        registry[fluid] = handler
    }

    @JvmStatic
    fun get(fluid: Fluid): HTFluidDrinkingHandler? = registry[fluid]

    @JvmStatic
    fun onDrink(
        fluid: Fluid,
        stack: ItemStack,
        world: World,
        user: LivingEntity,
    ) {
        get(fluid)?.onDrink(stack, world, user)
    }
}
