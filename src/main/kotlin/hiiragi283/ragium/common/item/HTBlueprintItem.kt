package hiiragi283.ragium.common.item

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.toText
import hiiragi283.ragium.setup.RagiumDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.world.entity.SlotAccess
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class HTBlueprintItem(properties: Properties) : Item(properties) {
    companion object {
        const val MAX_NUMBER = 7

        @JvmField
        val RANGE: IntRange = 0..MAX_NUMBER

        @JvmField
        val RANGE_CODEC: BiCodec<ByteBuf, Int> = BiCodecs.intRange(0, MAX_NUMBER)
    }

    override fun overrideOtherStackedOnMe(
        stack: ItemStack,
        other: ItemStack,
        slot: Slot,
        action: ClickAction,
        player: Player,
        access: SlotAccess,
    ): Boolean {
        if (action == ClickAction.SECONDARY) {
            stack.update(RagiumDataComponents.BLUEPRINT_NUMBER, 0) {
                when {
                    it < MAX_NUMBER -> it + 1
                    else -> 0
                }
            }
            return true
        }
        return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access)
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Text>,
        flag: TooltipFlag,
    ) {
        tooltips += "Number: ${stack.getOrDefault(RagiumDataComponents.BLUEPRINT_NUMBER, 0)}".toText()
    }
}
