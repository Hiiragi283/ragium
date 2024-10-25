package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.api.fluid.HTFluidDrinkingHandlerRegistry
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

object HTFilledFluidCubeItem : Item(itemSettings()) {
    override fun getName(stack: ItemStack): Text = stack
        .get(RagiumComponentTypes.FLUID)
        ?.let(FluidVariant::of)
        ?.let(FluidVariantAttributes::getName)
        ?.let { Text.translatable(translationKey, it) }
        ?: super.getName(stack)
    
    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        stack
            .get(RagiumComponentTypes.FLUID)
            ?.let { HTFluidDrinkingHandlerRegistry.onDrink(it, stack, world, user) }
        dropStackAt(
            user,
            RagiumContents.Misc.EMPTY_FLUID_CUBE
                .asItem()
                .defaultStack,
        )
        stack.decrementUnlessCreative(1, user)
        return stack
    }

    override fun getUseAction(stack: ItemStack): UseAction = UseAction.DRINK

    override fun getMaxUseTime(stack: ItemStack, user: LivingEntity): Int = 32

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> =
        ItemUsage.consumeHeldItem(world, user, hand)

    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType,
    ) {
        val entry: RegistryEntry.Reference<Fluid> = stack.get(RagiumComponentTypes.FLUID)?.registryEntry ?: return
        /*entry
            .value()
            .let(FluidVariant::of)
            .let(FluidVariantAttributes::getName)
            .string
            .let {
                tooltip.add(Text.literal("Stored Fluid: $it").formatted(Formatting.GRAY))
            }*/
        if (type.isAdvanced) {
            tooltip.add(Text.literal("Fluid Id: ${entry.idAsString}").formatted(Formatting.DARK_GRAY))
        }
    }
}
