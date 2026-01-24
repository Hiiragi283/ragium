package hiiragi283.ragium.common.item.block

import com.lowdragmc.lowdraglib2.gui.factory.HeldItemUIMenuType
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.gui.HTModularUIHelper
import hiiragi283.core.api.item.HTDescriptionBlockItem
import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.ragium.common.block.storage.HTUniversalChestBlock
import hiiragi283.ragium.common.item.HTUniversalChestManager
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class HTUniversalChestBlockItem(block: HTUniversalChestBlock, properties: Properties) :
    HTDescriptionBlockItem<HTUniversalChestBlock>(block, properties),
    HTSubCreativeTabContents,
    HeldItemUIMenuType.HeldItemUI {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        if (player is ServerPlayer) {
            HeldItemUIMenuType.openUI(player, usedHand)
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide)
    }

    //    HTSubCreativeTabContents    //

    override fun addItems(baseItem: HTItemHolderLike<*>, context: HTSubCreativeTabContents.Context) {
        DyeColor.entries
            .map { color: DyeColor -> createItemStack(baseItem, RagiumDataComponents.COLOR, color) }
            .forEach(context)
    }

    override fun shouldAddDefault(): Boolean = false

    //    HeldItemUIMenuType.HeldItemUI    //

    override fun createUI(holder: HeldItemUIMenuType.HeldItemUIHolder): ModularUI {
        val color: DyeColor = holder.itemStack.getOrDefault(RagiumDataComponents.COLOR, DyeColor.WHITE)
        val player: Player = holder.player
        val server: MinecraftServer = player.server ?: HiiragiCoreAPI.getActiveServer() ?: return HTModularUIHelper.createEmptyUI(player)
        return HTUniversalChestManager.createUI(
            player,
            holder.itemStack.hoverName,
            HTUniversalChestManager.getHandler(server, color),
        )
    }
}
