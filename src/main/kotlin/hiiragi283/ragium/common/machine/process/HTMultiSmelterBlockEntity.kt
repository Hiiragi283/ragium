package hiiragi283.ragium.common.machine.process

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTStorageBuilder
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTStorageSide
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.HTFurnaceRecipeProcessor
import hiiragi283.ragium.common.screen.HTSimpleMachineScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.SmeltingRecipe
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos

class HTMultiSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.MULTI_SMELTER, pos, state),
    HTMultiblockController {
    override var key: HTMachineKey = RagiumMachineKeys.MULTI_SMELTER

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    override val inventory: SidedInventory = HTStorageBuilder(2)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSided()

    override val fluidStorage: HTMachineFluidStorage = HTStorageBuilder(0).buildMachineFluidStorage()

    override val processor: HTFurnaceRecipeProcessor<SmeltingRecipe> =
        HTFurnaceRecipeProcessor(RecipeType.SMELTING, inventory, 0, 1)

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        HTSimpleMachineScreenHandler(syncId, playerInventory, packet, createContext())

    //    HTMultiblockController    //

    override var showPreview: Boolean = false
    override var isValid: Boolean = false

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        builder.addLayer(-1..1, -1, 1..3, HTMultiblockComponent.Simple(tier.getCasing()))
        builder.addHollow(-1..1, 0, 1..3, HTMultiblockComponent.Simple(tier.getCoil()))
        builder.addLayer(-1..1, 1, 1..3, HTMultiblockComponent.Simple(tier.getStorageBlock()))
    }
}
