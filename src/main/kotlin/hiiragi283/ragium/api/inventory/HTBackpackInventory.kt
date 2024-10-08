package hiiragi283.ragium.api.inventory

import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.ScreenHandlerFactory
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.text.Text
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class HTBackpackInventory(isLarge: Boolean) :
    HTSimpleInventory(
        when (isLarge) {
            true -> 6
            false -> 3
        } * 9,
    ) {
    var parent: ItemStack? = null

    private val factory = SimpleNamedScreenHandlerFactory(
        when (isLarge) {
            true -> ScreenHandlerFactory { syncId: Int, playerInventory: PlayerInventory, _: PlayerEntity ->
                GenericContainerScreenHandler.createGeneric9x6(syncId, playerInventory, this)
            }

            false -> ScreenHandlerFactory { syncId: Int, playerInventory: PlayerInventory, _: PlayerEntity ->
                GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this)
            }
        },
        Text.translatable("container.chest"),
    )

    fun openInventory(world: World, player: PlayerEntity, stack: ItemStack): TypedActionResult<ItemStack> {
        parent = stack
        if (!world.isClient) {
            player.openHandledScreen(factory)
        }
        return TypedActionResult.success(stack, world.isClient)
    }

    override fun getMaxCount(stack: ItemStack): Int = 1024

    override fun onClose(player: PlayerEntity) {
        parent?.set(RagiumComponentTypes.INVENTORY, this)
    }
}
