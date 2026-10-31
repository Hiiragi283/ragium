package hiiragi283.lib.recipe.display

import hiiragi283.lib.item.toTemplateOrNull
import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.crafting.display.FluidStackSlotDisplay

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
data object HTSlotDisplayHelper {
    @JvmField
    val EMPTY: SlotDisplay.Empty = SlotDisplay.Empty.INSTANCE

    // Fluid
    @JvmStatic
    fun create(stack: FluidStack): SlotDisplay = FluidStackSlotDisplay(stack)

    @JvmStatic
    fun create(template: FluidStackTemplate): SlotDisplay = template.create().let(::create)

    // Item
    @JvmStatic
    fun create(item: Holder<Item>?): SlotDisplay = item?.let(SlotDisplay::ItemSlotDisplay) ?: EMPTY

    @JvmStatic
    fun create(stack: ItemStack): SlotDisplay = stack.toTemplateOrNull()?.let(::create) ?: EMPTY

    @JvmStatic
    fun create(template: ItemStackTemplate): SlotDisplay = SlotDisplay.ItemStackSlotDisplay(template)
}
