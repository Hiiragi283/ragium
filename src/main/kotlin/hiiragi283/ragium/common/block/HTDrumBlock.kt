package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.api.extension.longText
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.block.entity.HTDrumBlockEntity
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos

class HTDrumBlock(val tier: HTMachineTier) : HTBlockWithEntity(blockSettings(Blocks.SMOOTH_STONE)) {
    override fun appendTooltip(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltip: MutableList<Text>,
        options: TooltipType?,
    ) {
        stack.get(RagiumComponentTypes.DRUM)?.let { storage: SingleFluidStorage ->
            if (!storage.isResourceBlank) {
                tooltip.add(
                    Text
                        .translatable(
                            RagiumTranslationKeys.DRUM_FLUID,
                            FluidVariantAttributes.getName(storage.resource).copy().formatted(Formatting.WHITE),
                        ).formatted(Formatting.GRAY),
                )
                tooltip.add(
                    Text
                        .translatable(
                            RagiumTranslationKeys.DRUM_AMOUNT,
                            longText(storage.getAmount()).formatted(Formatting.WHITE),
                        ).formatted(Formatting.GRAY),
                )
                tooltip.add(
                    Text
                        .translatable(
                            RagiumTranslationKeys.DRUM_AMOUNT,
                            longText(storage.capacity).formatted(Formatting.WHITE),
                        ).formatted(Formatting.GRAY),
                )
            }
        }
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTDrumBlockEntity(pos, state, tier)
}
