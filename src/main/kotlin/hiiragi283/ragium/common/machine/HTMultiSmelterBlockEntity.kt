package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.fluid.HTMachineFluidStorage
import hiiragi283.ragium.api.inventory.HTSidedInventory
import hiiragi283.ragium.api.inventory.HTStorageBuilder
import hiiragi283.ragium.api.inventory.HTStorageIO
import hiiragi283.ragium.api.inventory.HTStorageSide
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.api.recipe.HTFurnaceRecipeProcessor
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSimpleMachineScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.SmeltingRecipe
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTMultiSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntityBase(RagiumBlockEntityTypes.MULTI_SMELTER, pos, state),
    HTMultiblockController {
    override var key: HTMachineKey = RagiumMachineKeys.MULTI_SMELTER

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        HTSimpleMachineScreenHandler(syncId, playerInventory, packet, createContext())

    override fun processRecipe(world: World, pos: BlockPos): Boolean = processor.process(world)

    override val fluidStorage: HTMachineFluidStorage = HTStorageBuilder(0).buildFluidStorage()

    //    HTDelegatedInventory    //

    private val inventory: HTSidedInventory = HTStorageBuilder(2)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSided()

    override fun asInventory(): SidedInventory = inventory

    private var processor: HTFurnaceRecipeProcessor<SmeltingRecipe> =
        HTFurnaceRecipeProcessor(RecipeType.SMELTING, inventory, 0, 1, tier.smelterMulti)

    override fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {
        processor = HTFurnaceRecipeProcessor(RecipeType.SMELTING, inventory, 0, 1, newTier.smelterMulti)
    }

    //    HTMultiblockController    //

    override var showPreview: Boolean = false

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        builder.addLayer(-1..1, -1, 1..3, HTMultiblockComponent.of(tier.getBaseBlock()))
        builder.addHollow(-1..1, 0, 1..3, HTMultiblockComponent.of(tier.getCoil()))
        builder.addLayer(-1..1, 1, 1..3, HTMultiblockComponent.of(tier.getStorageBlock()))
    }
}
