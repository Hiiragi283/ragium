package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.capability.HTFluidCapabilities
import hiiragi283.ragium.api.item.HTDescriptionBlockItem
import hiiragi283.ragium.api.item.HTSubCreativeTabContents
import hiiragi283.ragium.api.item.createItemStack
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.attachments.HTAttachedItems
import hiiragi283.ragium.api.storage.fluid.HTFluidView
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.block.storage.HTTankBlock
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import java.util.function.Consumer

class HTTankBlockItem(block: HTTankBlock, properties: Properties) :
    HTDescriptionBlockItem<HTTankBlock>(block, properties),
    HTSubCreativeTabContents {
    /**
     * @see mekanism.common.item.block.machine.ItemBlockFluidTank.addStats
     */
    override fun addStats(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        val isCreative: Boolean = HTUpgradeHelper.getHandler(stack)?.isCreative() ?: false
        val view: HTFluidView = HTFluidCapabilities.getFluidView(stack, 0) ?: return
        // Fluid Name
        val stack: ImmutableFluidStack? = view.getStack()
        when {
            stack == null -> RagiumTranslation.EMPTY.translateColored(ChatFormatting.DARK_RED)
            isCreative -> RagiumTranslation.STORED.translateColored(
                ChatFormatting.LIGHT_PURPLE,
                stack,
                ChatFormatting.GRAY,
                RagiumTranslation.INFINITE,
            )
            else -> RagiumTranslation.STORED_MB.translateColored(
                ChatFormatting.LIGHT_PURPLE,
                stack,
                ChatFormatting.GRAY,
                stack.amount(),
            )
        }.let(tooltips::add)
        // Tank Capacity
        when (isCreative) {
            true -> RagiumTranslation.CAPACITY.translateColored(
                ChatFormatting.BLUE,
                ChatFormatting.GRAY,
                RagiumTranslation.INFINITE,
            )
            false -> RagiumTranslation.CAPACITY_MB.translateColored(
                ChatFormatting.BLUE,
                ChatFormatting.GRAY,
                view.getCapacity(),
            )
        }.let(tooltips::add)
    }

    override fun addItems(baseItem: HTItemHolderLike, parameters: CreativeModeTab.ItemDisplayParameters, consumer: Consumer<ItemStack>) {
        // Creative Tank
        createItemStack(
            baseItem,
            RagiumDataComponents.MACHINE_UPGRADES,
            HTAttachedItems(listOf(RagiumItems.CREATIVE_UPGRADE.toImmutableStack(1))),
        ).let(consumer::accept)
    }
}
