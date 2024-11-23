package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.buildStyle
import hiiragi283.ragium.api.extension.itemSettings
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.ClickEvent
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

object HTGuideBookItem : Item(itemSettings().maxCount(1)) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack: ItemStack = user.getStackInHand(hand)
        if (!world.isClient) {
            user.sendMessage(
                Text.literal("Open an official wiki site").setStyle(
                    buildStyle {
                        // withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.of("Open an official wiki site")))
                        withClickEvent(
                            ClickEvent(
                                ClickEvent.Action.OPEN_URL,
                                "https://github.com/Hiiragi283/ragium/wiki",
                            ),
                        )
                    },
                ),
            )
        }
        return TypedActionResult.success(stack, world.isClient)
    }
}
