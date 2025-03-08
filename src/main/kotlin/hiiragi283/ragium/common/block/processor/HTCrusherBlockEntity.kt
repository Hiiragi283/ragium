package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockController
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.api.recipe.HTCrusherRecipe
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeCache
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeContext
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidSlotHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.state.BlockState

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.CRUSHER, pos, state, HTMachineType.CRUSHER, 600),
    HTFluidSlotHandler.Empty,
    HTMultiblockController {
    private val inputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("item_input")
    private val firstOutputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("first_item_output")
    private val secondOutputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("second_item_output")
    private val thirdOutputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("third_item_output")

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.DEFAULT

    private val recipeCache: HTMachineRecipeCache<HTCrusherRecipe> = HTMachineRecipeCache(HTRecipeTypes.CRUSHER)

    override fun process(level: ServerLevel, pos: BlockPos) {
        validateMultiblock(this, null).getOrThrow()
        val context: HTMachineRecipeContext = HTMachineRecipeContext
            .builder()
            .addInput(0, inputSlot)
            .addOutput(0, firstOutputSlot)
            .addOutput(1, secondOutputSlot)
            .addOutput(2, thirdOutputSlot)
            .build()
        recipeCache.processFirstRecipe(context, level)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    //    Item    //

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = when (slot) {
        0 -> HTStorageIO.INPUT
        1 -> HTStorageIO.OUTPUT
        2 -> HTStorageIO.OUTPUT
        3 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    override fun getItemSlot(slot: Int): HTItemSlot? = when (slot) {
        0 -> inputSlot
        1 -> firstOutputSlot
        2 -> secondOutputSlot
        3 -> thirdOutputSlot
        else -> null
    }

    override fun getSlots(): Int = 4

    //    HTMultiblockController    //

    override fun getMultiblockMap(): HTMultiblockMap.Relative? = null

    override fun getDefinition(): HTControllerDefinition? = level?.let { HTControllerDefinition(it, pos, front) }
}
