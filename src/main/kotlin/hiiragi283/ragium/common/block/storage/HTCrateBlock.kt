package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.api.extension.longText
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.block.HTBlockWithEntity
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos

class HTCrateBlock(val tier: HTMachineTier) : HTBlockWithEntity.Horizontal(blockSettings(Blocks.SMOOTH_STONE)) {
    override fun appendTooltip(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltip: MutableList<Text>,
        options: TooltipType,
    ) {
        tooltip.add(
            Text
                .translatable(
                    RagiumTranslationKeys.CRATE_CAPACITY,
                    longText(tier.crateCapacity).formatted(Formatting.WHITE),
                ).formatted(Formatting.GRAY),
        )
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTCrateBlockEntity(pos, state, tier)
}
