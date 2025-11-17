package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.common.util.HTItemDropHelper
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.FluidTags
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.common.Tags

class HTFisherBlockEntity(pos: BlockPos, state: BlockState) : HTCapturerBlockEntity(RagiumBlocks.FISHER, pos, state) {
    override fun inputFilter(stack: ImmutableItemStack): Boolean = stack.isOf(Tags.Items.TOOLS_FISHING_ROD)

    //    Ticking    //

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        val fishingRod: ItemStack = inputSlot.getStack()?.unwrap() ?: return false
        val checkWater: Boolean = BlockPos
            .betweenClosed(pos.offset(-1, -2, -1), pos.offset(1, -1, 1))
            .all { it == pos || level.getFluidState(it).`is`(FluidTags.WATER) }
        if (!checkWater) return false
        // 釣りの結果を生成する
        val owner: ServerPlayer? = getOwnerPlayer(level)
        val params: LootParams = LootParams
            .Builder(level)
            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
            .withParameter(LootContextParams.TOOL, fishingRod)
            .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, owner)
            .withLuck(owner?.luck ?: 0f)
            .create(LootContextParamSets.FISHING)
        val lootTable: LootTable = level.server.reloadableRegistries().getLootTable(BuiltInLootTables.FISHING)
        val drops: List<ItemStack> = lootTable.getRandomItems(params)
        // 釣りの結果をスロットに入れる
        for (stack: ItemStack in drops) {
            val remainder: ImmutableItemStack? = HTStackSlotHelper.insertStacks(outputSlots, stack.toImmutable(), HTStorageAction.EXECUTE)
            HTItemDropHelper.dropStackAt(level, pos.above(), remainder)
            fishingRod.hurtAndBreak(1, level, null as LivingEntity?) { inputSlot.setStackUnchecked(null) }
            inputSlot.setStackUnchecked(fishingRod.toImmutable())
        }
        level.playSound(null, pos, SoundEvents.FISHING_BOBBER_SPLASH, SoundSource.BLOCKS, 0.5f, 1.0f)
        return true
    }

    override fun getTickRate(): Int = 20 * 3
}
