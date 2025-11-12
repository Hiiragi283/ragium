package hiiragi283.ragium.common.item.tool

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.item.createItemStack
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class HTUniversalBundleItem(properties: Properties) : Item(properties.stacksTo(1)) {
    companion object {
        @JvmStatic
        fun createBundle(color: DyeColor): ItemStack = createItemStack(RagiumItems.UNIVERSAL_BUNDLE, RagiumDataComponents.COLOR, color)

        @JvmStatic
        fun openBundle(level: Level, player: Player, stack: ItemStack): InteractionResultHolder<ItemStack> {
            val color: DyeColor = stack.get(RagiumDataComponents.COLOR) ?: return InteractionResultHolder.fail(stack)
            if (level is ServerLevel) {
                val handler: HTItemHandler = RagiumPlatform.INSTANCE.getUniversalBundle(level.server, color)
                RagiumMenuTypes.UNIVERSAL_BUNDLE.openMenu(player, stack.hoverName, handler) {}
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
        }
    }

    override fun getDefaultInstance(): ItemStack = createBundle(DyeColor.WHITE)

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> =
        openBundle(level, player, player.getItemInHand(usedHand))
}
