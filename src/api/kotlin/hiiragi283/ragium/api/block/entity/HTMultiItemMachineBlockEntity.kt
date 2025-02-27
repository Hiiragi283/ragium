package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTMultiItemRecipe
import hiiragi283.ragium.api.recipe.base.HTRecipeType
import hiiragi283.ragium.api.storage.HTFluidTank
import hiiragi283.ragium.api.storage.HTItemSlot
import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.function.Supplier

abstract class HTMultiItemMachineBlockEntity(
    type: Supplier<out BlockEntityType<*>>,
    pos: BlockPos,
    state: BlockState,
    machineType: HTMachineType,
) : HTMachineBlockEntity(type, pos, state, machineType) {
    private val firstItemSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("first_item")
    private val secondItemSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("second_item")
    private val thirdItemSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("third_item")
    private val outputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("output")

    private val inputTank: HTFluidTank = HTFluidTank
        .Builder()
        .setCallback(this::setChanged)
        .build("fluid_input")

    abstract val recipeType: HTRecipeType<out HTMultiItemRecipe>

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, dynamicOps)
        firstItemSlot.writeNbt(nbt, dynamicOps)
        secondItemSlot.writeNbt(nbt, dynamicOps)
        thirdItemSlot.writeNbt(nbt, dynamicOps)
        outputSlot.writeNbt(nbt, dynamicOps)

        inputTank.writeNbt(nbt, dynamicOps)
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.readNbt(nbt, dynamicOps)
        firstItemSlot.readNbt(nbt, dynamicOps)
        secondItemSlot.readNbt(nbt, dynamicOps)
        thirdItemSlot.readNbt(nbt, dynamicOps)
        outputSlot.readNbt(nbt, dynamicOps)

        inputTank.readNbt(nbt, dynamicOps)
    }

    override fun process(level: ServerLevel, pos: BlockPos) {
        val input: HTMachineRecipeInput = HTMachineRecipeInput
            .Builder()
            .addItem(firstItemSlot)
            .addItem(secondItemSlot)
            .addItem(thirdItemSlot)
            .addFluid(inputTank)
            .build()
        val recipe: HTMultiItemRecipe = recipeType.getFirstRecipe(input, level).getOrThrow()

        val output: ItemStack = recipe.itemOutput.get()
        if (!outputSlot.canInsert(output)) throw HTMachineException.MergeOutput(false)

        if (!firstItemSlot.canShrink(recipe.itemInputs[0].count, true)) throw HTMachineException.ShrinkInput(false)
        recipe.itemInputs.getOrNull(1)?.let { ingredient: HTItemIngredient ->
            if (!secondItemSlot.canShrink(ingredient.count, true)) throw HTMachineException.ShrinkInput(false)
        }
        recipe.itemInputs.getOrNull(2)?.let { ingredient: HTItemIngredient ->
            if (!thirdItemSlot.canShrink(ingredient.count, true)) throw HTMachineException.ShrinkInput(false)
        }
        recipe.fluidInput.ifPresent { ingredient: SizedFluidIngredient ->
            if (!inputTank.canShrink(ingredient.amount(), true)) throw HTMachineException.ShrinkInput(false)
        }

        outputSlot.insertItem(output, false)
        firstItemSlot.shrinkStack(recipe.itemInputs[0].count, false)
        recipe.itemInputs.getOrNull(1)?.let { ingredient: HTItemIngredient ->
            secondItemSlot.shrinkStack(ingredient.count, true)
        }
        recipe.itemInputs.getOrNull(2)?.let { ingredient: HTItemIngredient ->
            thirdItemSlot.shrinkStack(ingredient.count, true)
        }
        recipe.fluidInput.ifPresent { ingredient: SizedFluidIngredient ->
            inputTank.canShrink(ingredient.amount(), false)
        }
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        RagiumAPI.getInstance().createMultiItemMenu(
            containerId,
            playerInventory,
            blockPos,
            firstItemSlot,
            secondItemSlot,
            thirdItemSlot,
            outputSlot,
        )

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        super.updateEnchantments(newEnchantments)
        inputTank.updateCapacity(this)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        firstItemSlot.dropStack(level, pos)
        secondItemSlot.dropStack(level, pos)
        thirdItemSlot.dropStack(level, pos)
        outputSlot.dropStack(level, pos)
    }

    //    Item    //

    override fun getItemSlot(slot: Int): HTItemSlot? = when (slot) {
        0 -> firstItemSlot
        1 -> secondItemSlot
        2 -> thirdItemSlot
        3 -> outputSlot
        else -> null
    }

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = when (slot) {
        0 -> HTStorageIO.INPUT
        1 -> HTStorageIO.INPUT
        2 -> HTStorageIO.INPUT
        3 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    override fun getSlots(): Int = 4

    //    Fluid    //

    override fun getFluidTank(tank: Int): HTFluidTank? = inputTank

    override fun getFluidIoFromSlot(tank: Int): HTStorageIO = HTStorageIO.INPUT

    override fun getTanks(): Int = 1
}
