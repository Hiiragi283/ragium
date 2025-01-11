package hiiragi283.ragium.api.extension

import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
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
import java.util.*

//    Open    //

/**
 * エンダーチェストのスクリーンを開きます。
 * @param world エンダーチェストを開こうとしているワールド
 * @param player エンダーチェストを開こうとしているプレイヤー
 * @return スクリーンを開けた場合はtrue
 */
fun openEnderChest(world: World, player: PlayerEntity): Boolean {
    if (!world.isClient) {
        return player
            .openHandledScreen(
                SimpleNamedScreenHandlerFactory({ syncId: Int, playerInv: PlayerInventory, _: PlayerEntity ->
                    GenericContainerScreenHandler.createGeneric9x3(
                        syncId,
                        playerInv,
                        playerInv.player.enderChestInventory,
                    )
                }, Text.translatable("container.enderchest")),
            ).toDataResult { "Failed to open Ender Chest screen!" }
            .ifSuccess {
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
            }.mapOrElse({ true }, { false })
    }
    return false
}

/**
 * 指定した値からバックパックのスクリーンを開きます。
 * @param world バックパックを開こうとしているワールド
 * @param player バックパックを開こうとしているプレイヤー
 * @param hand バックパックを持っている手
 * @return スクリーンを開けた場合は[TypedActionResult.success]，それ以外の場合は[TypedActionResult.pass]
 */
fun openBackpackScreen(world: WorldAccess, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> =
    openBackpackScreen(world, player, player.getStackInHand(hand))

/**
 * 指定した値からバックパックのスクリーンを開きます。
 * @param world バックパックを開こうとしているワールド
 * @param player バックパックを開こうとしているプレイヤー
 * @param stack バックパックとなる[ItemStack]
 * @return スクリーンを開けた場合は[TypedActionResult.success]，それ以外の場合は[TypedActionResult.pass]
 */
fun openBackpackScreen(world: WorldAccess, player: PlayerEntity, stack: ItemStack): TypedActionResult<ItemStack> = when {
    stack.contains(RagiumComponentTypes.COLOR) -> when {
        openBackpackScreen(world, player, stack.getOrDefault(RagiumComponentTypes.COLOR, DyeColor.WHITE)) -> {
            TypedActionResult.success(stack, world.isClient)
        }

        else -> TypedActionResult.pass(stack)
    }

    else -> TypedActionResult.pass(stack)
}

/**
 * 指定した値からバックパックのスクリーンを開きます。
 * @param world バックパックを開こうとしているワールド
 * @param player バックパックを開こうとしているプレイヤー
 * @param color バックパックの色
 * @return スクリーンを開けた場合はtrue，それ以外の場合はfalse
 */
fun openBackpackScreen(world: WorldAccess, player: PlayerEntity, color: DyeColor): Boolean = world
    .getBackpackManager()
    .map { it[color] }
    .map { inventory: SimpleInventory ->
        player.openHandledScreen(
            SimpleNamedScreenHandlerFactory({ syncId: Int, playerInv: PlayerInventory, _: PlayerEntity ->
                GenericContainerScreenHandler.createGeneric9x6(syncId, playerInv, inventory)
            }, RagiumItems.BACKPACK.get().name),
        )
    }.map(OptionalInt::isPresent)
    .ifSuccess { player.playSound(SoundEvents.BLOCK_VAULT_OPEN_SHUTTER, 1.0f, 1.0f) }
    .orElse(false)

//    ScreenHandlerContext    //

/**
 * [ScreenHandlerContext.get]の戻り値を展開します。
 * @return この[ScreenHandlerContext]が空の場合はnull
 */
fun <T : Any> ScreenHandlerContext.getOrNull(getter: (World, BlockPos) -> T?): T? = get(getter, null)

/**
 * この[ScreenHandlerContext]から[BlockEntity]を取得します。
 */
fun ScreenHandlerContext.getBlockEntity(): BlockEntity? = getOrNull(World::getBlockEntity)
