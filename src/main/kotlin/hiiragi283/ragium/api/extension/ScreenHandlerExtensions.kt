package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.common.block.entity.HTBlockEntityBase
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

//    Open    //

fun openEnderChest(world: World, player: PlayerEntity) {
    if (!world.isClient) {
        player.openHandledScreen(
            SimpleNamedScreenHandlerFactory({ syncId: Int, playerInv: PlayerInventory, _: PlayerEntity ->
                GenericContainerScreenHandler.createGeneric9x3(
                    syncId,
                    playerInv,
                    playerInv.player.enderChestInventory,
                )
            }, Text.translatable("container.enderchest")),
        )
    }
    world.playSound(
        null,
        player.x,
        player.y,
        player.z,
        SoundEvents.BLOCK_ENDER_CHEST_OPEN,
        SoundCategory.BLOCKS,
        0.5f,
        1.0f,
    )
}

fun openBackpackScreen(world: WorldAccess, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> =
    openBackpackScreen(world, player, player.getStackInHand(hand))

fun openBackpackScreen(world: WorldAccess, player: PlayerEntity, stack: ItemStack): TypedActionResult<ItemStack> =
    if (stack.contains(RagiumComponentTypes.COLOR)) {
        openBackpackScreen(world, player, stack.getOrDefault(RagiumComponentTypes.COLOR, DyeColor.WHITE))
        TypedActionResult.success(stack, world.isClient)
    } else {
        TypedActionResult.pass(stack)
    }

fun openBackpackScreen(world: WorldAccess, player: PlayerEntity, color: DyeColor) {
    world.backpackManager
        .map { it[color] }
        .ifSuccess { inventory: SimpleInventory ->
            player.openHandledScreen(
                SimpleNamedScreenHandlerFactory({ syncId: Int, playerInv: PlayerInventory, _: PlayerEntity ->
                    GenericContainerScreenHandler.createGeneric9x6(syncId, playerInv, inventory)
                }, RagiumItems.BACKPACK.name),
            )
            player.playSound(SoundEvents.BLOCK_VAULT_OPEN_SHUTTER, 1.0f, 1.0f)
        }
}

//    ScreenHandlerContext    //

/**
 * Unwrap [ScreenHandlerContext.get] with null default value
 */
fun <T : Any> ScreenHandlerContext.getOrNull(getter: (World, BlockPos) -> T?): T? = get(getter, null)

/**
 * Get [BlockEntity] from [this] screen handler context, or null if failed
 */
fun ScreenHandlerContext.getBlockEntity(): BlockEntity? = getOrNull(World::getBlockEntity)

/**
 * Get [Inventory] from [this] screen handler context by [HTBlockEntityBase.asInventory], or null if failed
 */
fun ScreenHandlerContext.getInventory(size: Int): Inventory =
    (getBlockEntity() as? HTBlockEntityBase)?.asInventory() ?: SimpleInventory(size)

/**
 * Get [HTMachineBlockEntityBase] from [this] screen handler context, or null if failed
 */
fun ScreenHandlerContext.getMachineEntity(): HTMachineBlockEntityBase? = getBlockEntity() as? HTMachineBlockEntityBase
