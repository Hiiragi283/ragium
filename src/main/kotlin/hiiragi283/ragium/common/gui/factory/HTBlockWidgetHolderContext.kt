package hiiragi283.ragium.common.gui.factory

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.registry.HTDeferredHolder
import hiiragi283.lib.text.Text
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.gui.menu.HTWidgetContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.MenuProvider
import net.minecraft.world.Nameable
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

data class HTBlockWidgetHolderContext(val factory: Factory, val player: Player, val pos: BlockPos) :
    HTWidgetHolderContext,
    MenuProvider {
    companion object {
        @JvmField
        val MENU_TYPE: HTDeferredHolder<MenuType<*>, MenuType<HTWidgetContainerMenu>> =
            HTDeferredHolder(Registries.MENU, RagiumAPI.id(HTConstants.BLOCK))

        @JvmStatic
        fun openMenu(player: Player, pos: BlockPos): Boolean {
            val block: Block = player.level().getBlockState(pos).block
            if (block is Factory) {
                val context: HTBlockWidgetHolderContext = block.createContext(player, pos)
                return player.openMenu(context).isPresent
            }
            return false
        }

        @JvmStatic
        fun create(containerId: Int, inventory: Inventory, buffer: RegistryFriendlyByteBuf): HTWidgetContainerMenu {
            val player: Player = inventory.player
            val pos: BlockPos = buffer.readBlockPos()
            val block: Block = player.level().getBlockState(pos).block
            if (block is Factory) {
                val context: HTBlockWidgetHolderContext = block.createContext(player, pos)
                return HTWidgetContainerMenu(MENU_TYPE.get(), containerId, inventory, context)
            } else {
                error("Cannot create menu from $block at $pos")
            }
        }
    }

    val level: Level = player.level()
    val state: BlockState = level.getBlockState(pos)
    val blockEntity: BlockEntity? = level.getBlockEntity(pos)

    override fun setup(player: Player, widgetHolder: HTWidgetHolder) {
        factory.setup(this, widgetHolder)
    }

    override fun stillValid(player: Player): Boolean = factory.stillValid(this)

    override fun getDisplayName(): Text = factory.getDisplayName(this)

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTWidgetContainerMenu =
        HTWidgetContainerMenu(MENU_TYPE.get(), containerId, playerInventory, this)

    override fun writeClientSideData(menu: AbstractContainerMenu, buffer: RegistryFriendlyByteBuf) {
        buffer.writeBlockPos(pos)
    }

    //    Factory    //

    fun interface Factory {
        fun setup(context: HTBlockWidgetHolderContext, widgetHolder: HTWidgetHolder)

        fun createContext(player: Player, pos: BlockPos): HTBlockWidgetHolderContext = HTBlockWidgetHolderContext(this, player, pos)

        fun stillValid(context: HTBlockWidgetHolderContext): Boolean {
            val level: Level = context.level
            val pos: BlockPos = context.pos
            if (!level.isInWorldBounds(pos)) return false
            // 異なるブロックに置き換わった場合はfalse
            if (!level.getBlockState(pos).`is`(context.state.block)) return false
            // プレイヤーがアクセスできない距離にいたらfalse
            val player: Player = context.player
            if (!player.isWithinBlockInteractionRange(pos, 4.0)) return false
            // Block Entityが削除された，または異なるインスタンスを参照した場合はfalse
            val blockEntity: BlockEntity = context.blockEntity ?: return true
            if (blockEntity.isRemoved) return false
            return level.getBlockEntity(pos) === blockEntity
        }

        fun getDisplayName(context: HTBlockWidgetHolderContext): Text = (context.blockEntity as? Nameable)?.displayName ?: context.state.block.name
    }
}
