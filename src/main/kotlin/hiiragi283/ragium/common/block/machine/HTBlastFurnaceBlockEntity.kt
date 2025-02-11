package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.energy.HTMachineEnergyData
import hiiragi283.ragium.api.extension.canInsert
import hiiragi283.ragium.api.extension.insertOrDrop
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.recipe.HTBlastFurnaceRecipe
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTRecipeCache
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.inventory.HTMultiItemContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper

class HTBlastFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.BLAST_FURNACE, pos, state, RagiumMachineKeys.BLAST_FURNACE) {
    private val itemInput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(3, this::setChanged)
    private val itemOutput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)

    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.ofItem(
        listOf(
            itemInput.createSlot(0),
            itemInput.createSlot(1),
            itemInput.createSlot(2),
            itemOutput.createSlot(0),
        ),
    )

    private val recipeCache: HTRecipeCache<HTMachineRecipeInput, HTBlastFurnaceRecipe> =
        HTRecipeCache(RagiumRecipeTypes.BLAST_FURNACE)

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData =
        HTMachineEnergyData.Consume.DEFAULT

    override fun process(level: ServerLevel, pos: BlockPos) {
        checkMultiblockOrThrow()
        val input: HTMachineRecipeInput = HTMachineRecipeInput.of(
            enchantments,
            listOf(
                itemInput.getStackInSlot(0),
                itemInput.getStackInSlot(1),
                itemInput.getStackInSlot(2),
            ),
            listOf(),
        )
        val holder: RecipeHolder<HTBlastFurnaceRecipe> = recipeCache.getFirstRecipe(input, level).getOrThrow()
        val recipe: HTBlastFurnaceRecipe = holder.value()
        if (!itemInput.canConsumeAll()) throw HTMachineException.ConsumeInput(false)
        val output: ItemStack = recipe.itemResults[0].getItem(enchantments)
        if (!itemOutput.canInsert(output)) throw HTMachineException.MergeResult(false)
        itemOutput.insertOrDrop(level, pos, output)
        itemInput.consumeItem(0, recipe.firstInput.count(), false)
        itemInput.consumeItem(1, recipe.secondInput.count(), false)
        recipe.thirdInput.ifPresent { third: SizedIngredient ->
            itemInput.consumeItem(2, third.count(), false)
        }
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTMultiItemContainerMenu(containerId, playerInventory, blockPos, CombinedInvWrapper(itemInput, itemOutput))

    override fun interactWithFluidStorage(player: Player): Boolean = false

    override fun getItemHandler(direction: Direction?): CombinedInvWrapper = CombinedInvWrapper(
        HTStorageIO.INPUT.wrapItemHandler(itemInput),
        HTStorageIO.OUTPUT.wrapItemHandler(itemOutput),
    )
}
