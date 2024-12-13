package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.block.HTBlockWithEntity
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
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
        stack.get(RagiumComponentTypes.DRUM)?.let { (variant: FluidVariant, amount: Long) ->
            if (!variant.isBlank) {
                tooltip.add(
                    Text
                        .translatable(
                            RagiumTranslationKeys.DRUM_FLUID,
                            variant.name.formatted(Formatting.WHITE),
                        ).formatted(Formatting.GRAY),
                )
                tooltip.add(
                    Text
                        .translatable(
                            RagiumTranslationKeys.DRUM_AMOUNT,
                            longText(amount).formatted(Formatting.WHITE),
                        ).formatted(Formatting.GRAY),
                )
            }
        }
        stack.get(HTMachineTier.COMPONENT_TYPE)?.let { tierIn: HTMachineTier ->
            tooltip.add(
                Text
                    .translatable(
                        RagiumTranslationKeys.DRUM_CAPACITY,
                        longText(tierIn.tankCapacity).formatted(Formatting.WHITE),
                    ).formatted(Formatting.GRAY),
            )
        }
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTDrumBlockEntity(pos, state, tier)
}
