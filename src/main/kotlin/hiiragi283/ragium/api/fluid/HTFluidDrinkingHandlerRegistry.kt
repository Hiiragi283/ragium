package hiiragi283.ragium.api.fluid

import hiiragi283.ragium.api.content.HTFluidContent
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.common.advancement.HTDrankFluidCriterion
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItemsNew
import net.minecraft.entity.LivingEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.world.World

/**
 * A registry for [HTFluidDrinkingHandler]
 */
object HTFluidDrinkingHandlerRegistry {
    @JvmStatic
    private val registry: MutableMap<Fluid, HTFluidDrinkingHandler> = mutableMapOf()

    @JvmStatic
    fun register(fluid: HTFluidContent, handler: HTFluidDrinkingHandler) {
        register(fluid.get(), handler)
    }

    /**
     * Register [handler] for [fluid]
     */
    @JvmStatic
    fun register(fluid: Fluid, handler: HTFluidDrinkingHandler) {
        registry[fluid] = handler
    }

    /**
     * Get [Entry] from [fluid]
     */
    @JvmStatic
    fun get(fluid: Fluid): Entry? = registry[fluid]?.let { handler: HTFluidDrinkingHandler -> Entry(fluid, handler) }

    /**
     * Get [Entry] from [stack]
     */
    @JvmStatic
    fun getHandler(stack: ItemStack): Entry? = stack
        .get(RagiumComponentTypes.FLUID)
        ?.let(HTFluidDrinkingHandlerRegistry::get)

    /**
     * Run [HTFluidDrinkingHandler.onDrink] for give parameters
     * @return transformed [stack]
     */
    @JvmStatic
    fun drinkFluid(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        if (!world.isClient) {
            getHandler(stack)?.let { (fluid: Fluid, handler: HTFluidDrinkingHandler) ->
                handler.onDrink(stack, world, user)
                HTDrankFluidCriterion.trigger(user, fluid)
                dropStackAt(
                    user,
                    RagiumItemsNew.EMPTY_FLUID_CUBE,
                )
                stack.decrementUnlessCreative(1, user)
            }
        }
        return stack
    }

    //    Entry    //

    data class Entry(val fluid: Fluid, val handler: HTFluidDrinkingHandler)
}
