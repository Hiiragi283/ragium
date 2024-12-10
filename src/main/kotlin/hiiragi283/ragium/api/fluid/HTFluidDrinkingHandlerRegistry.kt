package hiiragi283.ragium.api.fluid

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.common.advancement.HTDrankFluidCriterion
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.entity.LivingEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.world.World

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
    fun get(fluid: Fluid): Entry? = registry[fluid]?.let { handler: HTFluidDrinkingHandler -> Entry(fluid, handler) }

    @JvmStatic
    fun getHandler(stack: ItemStack): Entry? = stack
        .get(RagiumComponentTypes.FLUID)
        ?.let(HTFluidDrinkingHandlerRegistry::get)

    @JvmStatic
    fun drinkFluid(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        if (!world.isClient) {
            getHandler(stack)?.let { (fluid: Fluid, handler: HTFluidDrinkingHandler) ->
                handler.onDrink(stack, world, user)
                HTDrankFluidCriterion.trigger(user, fluid)
                dropStackAt(
                    user,
                    RagiumItems.EMPTY_FLUID_CUBE.defaultStack,
                )
                stack.decrementUnlessCreative(1, user)
            }
        }
        return stack
    }

    //    Entry    //

    data class Entry(val fluid: Fluid, val handler: HTFluidDrinkingHandler)
}
