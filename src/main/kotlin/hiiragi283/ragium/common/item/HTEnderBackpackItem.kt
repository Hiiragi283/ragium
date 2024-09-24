package hiiragi283.ragium.common.item

import hiiragi283.ragium.common.util.itemSettings
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

object HTEnderBackpackItem : HTBaseItem(itemSettings()) {
    private val TITLE: Text = Text.translatable("container.enderchest")

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient) {
            user.openHandledScreen(
                SimpleNamedScreenHandlerFactory(::openEnderChest, TITLE),
            )
        }
        world.playSound(
            null,
            user.x,
            user.y,
            user.z,
            SoundEvents.BLOCK_ENDER_CHEST_OPEN,
            SoundCategory.BLOCKS,
            0.5f,
            1.0f,
        )
        return TypedActionResult.success(user.getStackInHand(hand), world.isClient)
    }

    @JvmStatic
    private fun openEnderChest(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        GenericContainerScreenHandler.createGeneric9x3(
            syncId,
            playerInventory,
            playerInventory.player.enderChestInventory,
        )
}
