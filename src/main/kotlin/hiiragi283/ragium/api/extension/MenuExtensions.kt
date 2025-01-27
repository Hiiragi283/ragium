package hiiragi283.ragium.api.extension

import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.MenuConstructor
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import java.util.*
import java.util.function.Consumer

/**
 * 指定した値からGUIを開きます
 * @param this GUIを開くプレイヤー
 * @param factory GUIを返すブロック
 * @param title GUIのタイトル
 * @param consumer [RegistryFriendlyByteBuf]に値を書き込むブロック
 */
fun Player.openMenu(factory: MenuConstructor, title: Component, consumer: Consumer<RegistryFriendlyByteBuf>): OptionalInt =
    openMenu(SimpleMenuProvider(factory, title), consumer)

//    ContainerLevelAccess    //

/**
 * [ContainerLevelAccess]を返します。
 * @return [BlockEntity.hasLevel]が`false`の場合は[ContainerLevelAccess.NULL]
 */
fun BlockEntity?.createMenuAccess(): ContainerLevelAccess =
    this?.ifPresentWorld { ContainerLevelAccess.create(it, blockPos) } ?: ContainerLevelAccess.NULL

/**
 * [ContainerLevelAccess.evaluate]の戻り値を展開します。
 * @return この[ContainerLevelAccess]が空の場合は`null`
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun <T : Any> ContainerLevelAccess.getOrNull(getter: (Level, BlockPos) -> T?): T? = evaluate(getter, null)

/**
 * この[ContainerLevelAccess]から[BlockEntity]を取得します。
 */
fun ContainerLevelAccess.getBlockEntity(): BlockEntity? = getOrNull(Level::getBlockEntity)
