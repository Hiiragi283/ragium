package hiiragi283.ragium.common.item.tool

import hiiragi283.ragium.common.component.HTSpawnerData
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.BaseSpawner
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.SpawnerBlockEntity

class HTDuraluminCaseItem(properties: Properties) : Item(properties.stacksTo(1)) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val player: Player? = context.player
        val level: Level = context.level
        val pos: BlockPos = context.clickedPos
        val stack: ItemStack = context.itemInHand
        // Place spawner if custom data present
        if (stack.has(RagiumComponentTypes.SPAWNER_DATA)) {
            stack.get(RagiumComponentTypes.SPAWNER_DATA)?.placeSpawner(level, pos.relative(context.clickedFace))
            stack.consume(1, player)
            return InteractionResult.sidedSuccess(level.isClientSide)
        }
        // Get spawner data
        val blockEntity: SpawnerBlockEntity =
            level.getBlockEntity(pos) as? SpawnerBlockEntity ?: return InteractionResult.FAIL
        val spawner: BaseSpawner = blockEntity.spawner
        val entity: Entity = spawner.getOrCreateDisplayEntity(level, pos) ?: return InteractionResult.FAIL
        // Put the data into held stack
        stack.set(RagiumComponentTypes.SPAWNER_DATA, HTSpawnerData(entity.type))
        level.destroyBlock(pos, false)
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        stack.get(RagiumComponentTypes.SPAWNER_DATA)?.addToTooltip(context, tooltips::add, flag)
    }

    override fun isFoil(stack: ItemStack): Boolean = stack.has(RagiumComponentTypes.SPAWNER_DATA) || super.isFoil(stack)
}
