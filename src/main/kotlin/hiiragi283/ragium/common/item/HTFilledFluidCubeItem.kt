package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.api.fluid.HTFluidDrinkingHandler
import hiiragi283.ragium.api.fluid.HTFluidDrinkingHandlerRegistry
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import net.minecraft.util.*
import net.minecraft.world.World

object HTFilledFluidCubeItem : Item(itemSettings()) {
    override fun getName(stack: ItemStack): Text = stack
        .get(RagiumComponentTypes.FLUID)
        ?.let(FluidVariant::of)
        ?.let(FluidVariantAttributes::getName)
        ?.let { Text.translatable(translationKey, it) }
        ?: super.getName(stack)

    private fun getHandler(stack: ItemStack): HTFluidDrinkingHandler? = stack
        .get(RagiumComponentTypes.FLUID)
        ?.let(HTFluidDrinkingHandlerRegistry::get)

    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        getHandler(stack)?.let { handler: HTFluidDrinkingHandler ->
            handler.onDrink(stack, world, user)
            dropStackAt(
                user,
                RagiumItems.EMPTY_FLUID_CUBE.defaultStack,
            )
            stack.decrementUnlessCreative(1, user)
        }
        return stack
    }

    override fun getUseAction(stack: ItemStack): UseAction = getHandler(stack)?.let { UseAction.DRINK } ?: UseAction.NONE

    override fun getMaxUseTime(stack: ItemStack, user: LivingEntity): Int = getHandler(stack)?.let { 32 } ?: 0

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> = getHandler(user.getStackInHand(hand))
        ?.let { ItemUsage.consumeHeldItem(world, user, hand) }
        ?: super.use(world, user, hand)

    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType,
    ) {
        if (type.isAdvanced) {
            val id: Identifier = stack.get(RagiumComponentTypes.FLUID)?.let(Registries.FLUID::getId) ?: return
            tooltip.add(Text.literal("Fluid Id: $id").formatted(Formatting.DARK_GRAY))
        }
    }
}
