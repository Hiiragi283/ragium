package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.extension.fluidAmountText
import hiiragi283.ragium.api.extension.fluidCapacityText
import hiiragi283.ragium.api.extension.getItemData
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumEnchantments
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.SimpleFluidContent

class HTDrumBlock(properties: Properties) : HTEntityBlock(properties) {
    override fun appendHoverText(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        val content: SimpleFluidContent = stack.get(RagiumComponentTypes.FLUID_CONTENT) ?: return
        // Title
        tooltipComponents.add(content.fluidType.description)
        // Amount
        tooltipComponents.add(fluidAmountText(content.amount))
        // Capacity
        val lookup: HolderLookup.RegistryLookup<Enchantment> =
            context.registries()?.lookupOrThrow(Registries.ENCHANTMENT) ?: return
        val enchantments: ItemEnchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
        val capacityLevel: Int = enchantments.getLevel(lookup.getOrThrow(RagiumEnchantments.CAPACITY)) + 1
        val tier: HTMachineTier = stack.getItemData(RagiumAPI.DataMapTypes.MACHINE_TIER) ?: return
        tooltipComponents.add(fluidCapacityText(tier.tankCapacity * capacityLevel))
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): HTDrumBlockEntity =
        HTDrumBlockEntity(pos, state, state.getItemData(RagiumAPI.DataMapTypes.MACHINE_TIER) ?: HTMachineTier.BASIC)
}
