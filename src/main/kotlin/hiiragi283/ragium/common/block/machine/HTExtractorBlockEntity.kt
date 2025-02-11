package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.energy.HTMachineEnergyData
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.recipe.HTExtractorRecipe
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTRecipeGetter
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.inventory.HTExtractorContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper

class HTExtractorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.EXTRACTOR, pos, state, RagiumMachineKeys.EXTRACTOR) {
    private val itemInput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)
    private val itemOutput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)
    private val outputTank: HTMachineFluidTank = RagiumAPI.getInstance().createTank(this::setChanged)

    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.of(
        listOf(
            itemInput.createSlot(0),
            itemOutput.createSlot(0),
        ),
        listOf(outputTank),
    )

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        super.updateEnchantments(newEnchantments)
        outputTank.updateCapacity(this)
    }

    private val recipeCache: HTRecipeGetter.Cached<HTMachineRecipeInput, HTExtractorRecipe> =
        HTRecipeGetter.Cached(RagiumRecipeTypes.EXTRACTOR.get())

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.CHEMICAL

    override fun process(level: ServerLevel, pos: BlockPos) {
        // Find matching recipe
        val input: HTMachineRecipeInput = HTMachineRecipeInput.of(enchantments, itemInput.getStackInSlot(0))
        val recipe: HTExtractorRecipe = recipeCache.getFirstRecipe(input, level).getOrThrow()
        // Try to insert outputs
        recipe.canInsert(itemOutput, outputTank)
        // Insert outputs
        recipe.insertOutputs(itemOutput, outputTank, level, pos)
        // Decrement input
        itemInput.getStackInSlot(0).shrink(recipe.input.count())
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTExtractorContainerMenu(containerId, playerInventory, blockPos, CombinedInvWrapper(itemInput, itemOutput))

    override fun interactWithFluidStorage(player: Player): Boolean = outputTank.interactWithFluidStorage(player, HTStorageIO.GENERIC)

    override fun getItemHandler(direction: Direction?): CombinedInvWrapper =
        CombinedInvWrapper(HTStorageIO.INPUT.wrapItemHandler(itemInput), HTStorageIO.OUTPUT.wrapItemHandler(itemOutput))

    override fun getFluidHandler(direction: Direction?): IFluidHandler = HTStorageIO.OUTPUT.wrapFluidHandler(outputTank)
}
