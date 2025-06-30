package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.storage.energy.HTEnergyNetwork
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.energy.IEnergyStorage
import kotlin.math.min

class HTEnergyNetworkInterfaceBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntity(RagiumBlockEntityTypes.ENI, pos, state) {
    private var network: IEnergyStorage? = null

    override fun writeNbt(writer: HTNbtCodec.Writer) {}

    override fun readNbt(reader: HTNbtCodec.Reader) {}

    override fun onRightClickedWithItem(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult {
        val capacityAdd: Int = when {
            stack.`is`(RagiumItemTags.ENI_UPGRADES_BASIC) -> 1_000_000 // 1M
            stack.`is`(RagiumItemTags.ENI_UPGRADES_ADVANCED) -> 10_000_000 // 10M
            stack.`is`(RagiumItemTags.ENI_UPGRADES_ELITE) -> 100_000_000 // 100M
            stack.`is`(RagiumItemTags.ENI_UPGRADES_ULTIMATE) -> 1_000_000_000 // 1G
            else -> return super.onRightClickedWithItem(stack, state, level, pos, player, hand, hitResult)
        }
        (network as? HTEnergyNetwork)?.let { network: HTEnergyNetwork ->
            network.capacity = min(network.capacity + capacityAdd, Int.MAX_VALUE)
            network.setDirty()
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide)
    }

    override fun afterLevelInit(level: Level) {
        network = RagiumAPI.getInstance().getEnergyNetworkManager().getNetwork(level)
    }

    override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = network
}
