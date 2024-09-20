package hiiragi283.ragium.common.item

import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.inventory.HTParentedInventory
import hiiragi283.ragium.common.util.itemSettings
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.ScreenHandlerFactory
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class HTBackpackItem private constructor(private val isLarge: Boolean) :
    Item(
        itemSettings()
            .maxCount(1)
            .fireproof()
            .component(RagiumComponentTypes.INVENTORY, HTParentedInventory(getSize(isLarge))),
    ) {
        companion object {
            @JvmField
            val NORMAL = HTBackpackItem(false)

            @JvmField
            val LARGE = HTBackpackItem(true)

            private val TITLE: Text = Text.translatable("container.chest")

            private fun getSize(isLarge: Boolean) = when (isLarge) {
                true -> 6
                false -> 3
            } * 9

            private fun createHandler(isLarge: Boolean, inventory: HTParentedInventory): ScreenHandlerFactory = when (isLarge) {
                true -> ScreenHandlerFactory { syncId: Int, playerInventory: PlayerInventory, _: PlayerEntity ->
                    GenericContainerScreenHandler.createGeneric9x6(syncId, playerInventory, inventory)
                }

                false -> ScreenHandlerFactory { syncId: Int, playerInventory: PlayerInventory, _: PlayerEntity ->
                    GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, inventory)
                }
            }
        }

        override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
            val stack: ItemStack = user.getStackInHand(hand)
            val inventory: HTParentedInventory =
                stack.get(RagiumComponentTypes.INVENTORY) as? HTParentedInventory ?: HTParentedInventory(getSize(isLarge))
            inventory.parent = stack
            if (!world.isClient) {
                user.openHandledScreen(SimpleNamedScreenHandlerFactory(createHandler(isLarge, inventory), TITLE))
            }
            return TypedActionResult.success(stack, world.isClient)
        }
    }
