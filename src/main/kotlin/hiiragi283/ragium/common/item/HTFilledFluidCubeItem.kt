package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.id
import hiiragi283.ragium.api.extension.ifPresent
import hiiragi283.ragium.api.extension.name
import hiiragi283.ragium.api.fluid.HTFluidDrinkingHandlerRegistry
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.Text
import net.minecraft.util.*
import net.minecraft.world.World

class HTFilledFluidCubeItem(settings: Settings) : Item(settings) {
    override fun getName(stack: ItemStack): Text = stack
        .get(RagiumComponentTypes.FLUID)
        ?.value()
        ?.name
        ?.let { Text.translatable(translationKey, it) }
        ?: super.getName(stack)

    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack =
        HTFluidDrinkingHandlerRegistry.drinkFluid(stack, world, user)

    override fun getUseAction(stack: ItemStack): UseAction =
        HTFluidDrinkingHandlerRegistry.getHandler(stack)?.let { UseAction.DRINK } ?: UseAction.NONE

    override fun getMaxUseTime(stack: ItemStack, user: LivingEntity): Int =
        HTFluidDrinkingHandlerRegistry.getHandler(stack)?.let { 32 } ?: 0

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> = HTFluidDrinkingHandlerRegistry
        .getHandler(user.getStackInHand(hand))
        ?.let { ItemUsage.consumeHeldItem(world, user, hand) }
        ?: super.use(world, user, hand)

    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType,
    ) {
        if (type.isAdvanced) {
            val id: Identifier = stack.ifPresent(RagiumComponentTypes.FLUID, RegistryEntry<Fluid>::id) ?: return
            tooltip.add(Text.literal("Fluid Id: $id").formatted(Formatting.DARK_GRAY))
        }
    }
}
