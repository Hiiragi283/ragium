package hiiragi283.ragium.common.item

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.util.HTItemDropHelper
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.item.component.HTDefaultLootTickets
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.parameters.LootContextParams

class HTLootTicketItem(properties: Properties) :
    Item(properties.rarity(Rarity.RARE)),
    HTSubCreativeTabContents {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack: ItemStack = player.getItemInHand(usedHand)
        val lootTableKey: ResourceKey<LootTable> = stack
            .get(RagiumDataComponents.LOOT_TICKET)
            ?.getRandomLoot(player.random)
            ?: return InteractionResultHolder.fail(stack)
        if (level is ServerLevel) {
            val lootTable: LootTable = level.server
                .reloadableRegistries()
                .getLootTable(lootTableKey)
            val params: LootParams = LootParams
                .Builder(level)
                .withParameter(LootContextParams.ORIGIN, player.position())
                .withLuck(player.luck)
                .create(LootContextParamSets.EMPTY)
            val lootItems: List<ItemStack> = lootTable.getRandomItems(params)
            if (lootItems.isEmpty()) return InteractionResultHolder.pass(stack)
            for (stackIn: ItemStack in lootItems) {
                HTItemDropHelper.giveStackTo(player, stackIn)
            }
            if (player is ServerPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger(player, stack)
            }
            stack.consume(1, player)
            level.playSound(
                null,
                player.blockPosition(),
                SoundEvents.CHEST_OPEN,
                SoundSource.PLAYERS,
            )
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        super.appendHoverText(stack, context, tooltips, flag)
        val keys: List<ResourceKey<LootTable>> = stack.get(RagiumDataComponents.LOOT_TICKET)?.lootTables ?: return
        keys
            .map(ResourceKey<LootTable>::location)
            .map(ResourceLocation::toString)
            .map { RagiumTranslation.TOOLTIP_LOOT_TABLE_ID.translateColored(HTDefaultColor.YELLOW, HTDefaultColor.WHITE, it) }
            .forEach(tooltips::add)
    }

    override fun addItems(baseItem: HTItemHolderLike<*>, context: HTSubCreativeTabContents.Context) {
        for (tickets: HTDefaultLootTickets in HTDefaultLootTickets.entries) {
            createItemStack(baseItem, RagiumDataComponents.LOOT_TICKET, tickets.targets).let(context)
        }
    }
}
