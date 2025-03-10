package hiiragi283.ragium.common.tile.processor

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTBreweryRecipe
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeCache
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeContext
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.inventory.HTBreweryMenu
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.state.BlockState

class HTBreweryBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.BREWERY, pos, state, HTMachineType.BREWERY) {
    private val firstInputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("first_item_input")
    private val secondInputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("second_item_input")
    private val thirdInputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("third_item_input")
    private val outputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("item_output")

    private val inputTank: HTFluidTank = HTFluidTank
        .Builder()
        .setCallback(this::setChanged)
        .build("fluid_input")

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, registryOps)
        firstInputSlot.writeNbt(nbt, registryOps)
        secondInputSlot.writeNbt(nbt, registryOps)
        thirdInputSlot.writeNbt(nbt, registryOps)
        outputSlot.writeNbt(nbt, registryOps)

        inputTank.writeNbt(nbt, registryOps)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.readNbt(nbt, registryOps)
        firstInputSlot.readNbt(nbt, registryOps)
        secondInputSlot.readNbt(nbt, registryOps)
        thirdInputSlot.readNbt(nbt, registryOps)
        outputSlot.readNbt(nbt, registryOps)

        inputTank.readNbt(nbt, registryOps)
    }

    override fun onUpdateEnchantment(newEnchantments: ItemEnchantments) {
        super.onUpdateEnchantment(newEnchantments)
        inputTank.onUpdateEnchantment(newEnchantments)
    }

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.PRECISION

    private val recipeCache: HTMachineRecipeCache<HTBreweryRecipe> = HTMachineRecipeCache(HTRecipeTypes.BREWERY)

    override fun process(level: ServerLevel, pos: BlockPos) {
        val context: HTMachineRecipeContext = HTMachineRecipeContext
            .builder()
            .addInput(0, firstInputSlot)
            .addInput(1, secondInputSlot)
            .addInput(2, thirdInputSlot)
            .addInput(0, inputTank)
            .addOutput(0, outputSlot)
            .build()
        recipeCache.processFirstRecipe(context, level)
    }

    override fun onSucceeded() {
        super.onSucceeded()
        playSound(SoundEvents.BREWING_STAND_BREW)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = HTBreweryMenu(
        containerId,
        playerInventory,
        blockPos,
        firstInputSlot,
        secondInputSlot,
        thirdInputSlot,
        outputSlot,
    )

    //    Item    //

    override fun getItemSlot(slot: Int): HTItemSlot? = when (slot) {
        0 -> firstInputSlot
        1 -> secondInputSlot
        2 -> thirdInputSlot
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
