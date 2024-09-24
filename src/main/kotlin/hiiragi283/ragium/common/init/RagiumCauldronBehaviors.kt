package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.util.dropStackAt
import net.minecraft.block.BlockState
import net.minecraft.block.LeveledCauldronBlock
import net.minecraft.block.cauldron.CauldronBehavior
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.ItemActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object RagiumCauldronBehaviors {
    @JvmField
    val WASH_RAW_RAGINITE =
        CauldronBehavior { state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, _: Hand, stack: ItemStack ->
            if (stack.isOf(RagiumContents.Dusts.RAW_RAGINITE.asItem())) {
                if (!world.isClient) {
                    val count: Int = stack.count
                    stack.count = -1
                    dropStackAt(player, ItemStack(RagiumContents.Dusts.RAGINITE, count))
                    LeveledCauldronBlock.decrementFluidLevel(state, world, pos)
                }
                ItemActionResult.success(world.isClient)
            } else {
                ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
            }
        }

    @JvmStatic
    fun init() {
        register(CauldronBehavior.WATER_CAULDRON_BEHAVIOR, RagiumContents.Dusts.RAW_RAGINITE, WASH_RAW_RAGINITE)
    }

    @JvmStatic
    private fun register(map: CauldronBehavior.CauldronBehaviorMap, item: ItemConvertible, behavior: CauldronBehavior) {
        map.map[item.asItem()] = behavior
    }
}
