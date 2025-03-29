package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets

/**
 * @see net.minecraft.world.level.storage.loot.BuiltInLootTables
 */
class HTRagiTicketItem(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack: ItemStack = player.getItemInHand(usedHand)
        val tableId: ResourceLocation =
            stack.get(RagiumComponentTypes.LOOT_TABLE_ID) ?: return InteractionResultHolder.fail(stack)
        if (level is ServerLevel) {
            val lootTable: LootTable = level.server
                .reloadableRegistries()
                .getLootTable(ResourceKey.create(Registries.LOOT_TABLE, tableId))
            val params: LootParams = LootParams.Builder(level).withLuck(player.luck).create(LootContextParamSets.EMPTY)
            val lootItems: List<ItemStack> = lootTable.getRandomItems(params)
            if (lootItems.isEmpty()) return InteractionResultHolder.pass(stack)
            for (stackIn: ItemStack in lootItems) {
                dropStackAt(player, stackIn)
            }
            stack.consume(1, player)
            level.playSound(
                null,
                player.blockPosition(),
                SoundEvents.ENDER_DRAGON_DEATH,
                SoundSource.PLAYERS,
            )
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
    }
}
