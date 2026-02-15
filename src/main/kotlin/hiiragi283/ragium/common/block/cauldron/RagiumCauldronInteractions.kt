package hiiragi283.ragium.common.block.cauldron

import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.BlockPos
import net.minecraft.core.cauldron.CauldronInteraction
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.LayeredCauldronBlock
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.SoundActions
import java.util.function.Predicate

object RagiumCauldronInteractions {
    @JvmField
    val LATEX: CauldronInteraction.InteractionMap = CauldronInteraction.newInteractionMap(RagiumAPI.id("latex").toString())

    @JvmStatic
    fun init() {
        // Latex
        val latexMap: MutableMap<Item, CauldronInteraction> = LATEX.map
        latexMap[Items.BUCKET] = fill(HCFluids.LATEX) { state: BlockState ->
            state.getValue(LayeredCauldronBlock.LEVEL) == LayeredCauldronBlock.MAX_FILL_LEVEL
        }
    }

    @JvmStatic
    private fun fill(fluid: HTFluidContent, predicate: Predicate<BlockState>): CauldronInteraction =
        CauldronInteraction { state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, stack: ItemStack ->
            val fillSound: SoundEvent = fluid.getFluidType().getSound(SoundActions.BUCKET_FILL) ?: SoundEvents.BUCKET_FILL
            CauldronInteraction.fillBucket(state, level, pos, player, hand, stack, fluid.getBucketHolder().toStack(), predicate, fillSound)
        }
}
