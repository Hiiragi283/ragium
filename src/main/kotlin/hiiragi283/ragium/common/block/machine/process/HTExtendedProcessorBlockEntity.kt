package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.block.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.extension.sendPacket
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockManager
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.api.tags.RagiumBlockTags
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.machine.HTBlockTagPattern
import hiiragi283.ragium.common.machine.HTTieredBlockPattern
import hiiragi283.ragium.common.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.screen.HTLargeMachineScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTExtendedProcessorBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.EXTENDED_PROCESSOR, pos, state),
    HTMultiblockProvider {
    companion object {
        private val DEFAULT_KEY: HTMachineKey = HTMachineKey.of(RagiumAPI.id("large_processor"))
    }

    override var key: HTMachineKey = DEFAULT_KEY

    val isDefault: Boolean
        get() = key == DEFAULT_KEY

    override val inventory: HTMachineInventory = HTMachineInventory.ofLarge()

    override val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.ofLarge(this)

    override val processor = HTMachineRecipeProcessor(
        inventory,
        intArrayOf(0, 1, 2),
        intArrayOf(4, 5, 6),
        3,
        fluidStorage,
        intArrayOf(0, 1),
        intArrayOf(2, 3),
    )

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTLargeMachineScreenHandler(syncId, playerInventory, createContext())

    //    HTMultiblockProvider    //

    override val multiblockManager = HTMultiblockManager(::getWorld, pos, this)

    override fun beforeBuild(world: World?, pos: BlockPos, player: PlayerEntity?) {
        super.beforeBuild(world, pos, player)
        val parent: HTMachineBlockEntityBase = world?.getMachineEntity(pos.offset(facing.opposite, 2)) ?: return
        key = parent.key
        // tier = parent.tier TODO
        player?.sendPacket(payload)
    }

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        builder.addLayer(-1..1, -1, 1..3, HTTieredBlockPattern.ofContent(HTMachineTier::getCasing))
        builder.addHollow(-1..1, 0, 1..3, HTTieredBlockPattern.ofContent(HTMachineTier::getHull))
        builder.addLayer(-1..1, 1, 1..3, HTTieredBlockPattern.ofContent(HTMachineTier::getStorageBlock))
        builder.add(0, 0, 2, HTBlockTagPattern(RagiumBlockTags.MACHINES))
    }
}
