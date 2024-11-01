package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.trade.HTMerchant
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTTraderStationBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.TRADER_STATION, pos, state) {
    private var merchant = HTMerchant()

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        nbt.putInt("exp", merchant.experience)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        merchant.setExperienceFromServer(nbt.getInt("exp"))
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = when (world.isClient) {
        true -> ActionResult.SUCCESS
        else -> {
            merchant.offers.clear()
            merchant.customer = player
            merchant.sendOffers(player, Text.literal("Trader Station"), 1)
            ActionResult.CONSUME
        }
    }
}
