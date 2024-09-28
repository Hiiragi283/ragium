package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.inventory.*
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.recipe.HTRecipeBase
import hiiragi283.ragium.common.recipe.HTRecipeResult
import hiiragi283.ragium.common.recipe.HTRequireScanRecipe
import hiiragi283.ragium.common.screen.HTMachineScreenHandler
import hiiragi283.ragium.common.world.HTDataDriveManager
import hiiragi283.ragium.common.world.HTEnergyNetwork
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.recipe.RecipeEntry
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull
import kotlin.math.max

abstract class HTMachineBlockEntityBase :
    HTBlockEntityBase,
    HTDelegatedInventory,
    HTTieredMachine,
    NamedScreenHandlerFactory,
    PropertyDelegateHolder {
    override var machineType: HTMachineType<*> = HTMachineType.Default
        protected set
    override var tier: HTMachineTier = HTMachineTier.PRIMITIVE
        protected set

    @Deprecated("")
    constructor(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : super(type, pos, state)

    constructor(
        type: BlockEntityType<*>,
        pos: BlockPos,
        state: BlockState,
        machineType: HTMachineType<*>,
        tier: HTMachineTier,
    ) : this(
        type,
        pos,
        state,
    ) {
        updateProperties(machineType, tier)
    }

    private fun updateProperties(machineType: HTMachineType<*>, tier: HTMachineTier) {
        this.machineType = machineType
        this.tier = tier
    }

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, registryLookup)
        nbt.putString("machine_type", machineType.id.toString())
        nbt.putString("tier", tier.asString())
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, registryLookup)
        val machineType: HTMachineType<*> =
            HTMachineType.REGISTRY.get(Identifier.of(nbt.getString("machine_type"))) ?: return
        val tier: HTMachineTier =
            HTMachineTier.entries.firstOrNull { it.asString() == nbt.getString("tier") } ?: return
        updateProperties(machineType, tier)
    }

    //    HTBlockEntityBase    //

    override val tickRate: Int
        get() = tier.tickRate

    @Suppress("UNCHECKED_CAST")
    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        val input =
            HTMachineRecipe.Input(
                tier,
                getStack(0),
                getStack(1),
                getStack(2),
                getStack(3),
            )
        val recipeType: RecipeType<HTRecipeBase<HTMachineRecipe.Input>> =
            machineType.recipeType as? RecipeType<HTRecipeBase<HTMachineRecipe.Input>> ?: return
        val recipeEntry: RecipeEntry<HTRecipeBase<HTMachineRecipe.Input>> = world.recipeManager
            .getFirstMatch(recipeType, input, world)
            .getOrNull() ?: return
        val recipe: HTRecipeBase<HTMachineRecipe.Input> = recipeEntry.value
        val recipeId: Identifier = recipeEntry.id
        if (!canAcceptOutputs(recipe)) return
        if (!machineType.machineCondition(world, pos)) return
        if ((recipe as? HTRequireScanRecipe)?.requireScan == true) {
            val manager: HTDataDriveManager = HTDataDriveManager.getManager(world) ?: return
            if (recipeId !in manager) return
        }
        modifyOutput(0, recipe)
        modifyOutput(1, recipe)
        modifyOutput(2, recipe)
        decrementInput(0, recipe)
        decrementInput(1, recipe)
        decrementInput(2, recipe)
        onProcessed(world, pos, recipe)
    }

    open fun onProcessed(world: World, pos: BlockPos, recipe: HTRecipeBase<HTMachineRecipe.Input>) {
        // extract energy
        HTEnergyNetwork.getStorage(world)?.let { network: HTEnergyNetwork ->
            val currentAmount: Long = network.amount
            val extracted: Long = currentAmount - tier.recipeCost
            network.setAmount(max(0, extracted))
        }
    }

    private fun canAcceptOutputs(recipe: HTRecipeBase<HTMachineRecipe.Input>): Boolean {
        recipe.outputs.forEachIndexed { index: Int, result: HTRecipeResult ->
            val stackIn: ItemStack = getStack(index + 3)
            if (!result.canAccept(stackIn)) {
                return false
            }
        }
        return true
    }

    private fun decrementInput(slot: Int, recipe: HTRecipeBase<HTMachineRecipe.Input>) {
        val delCount: Int = recipe.getInput(slot)?.count ?: return
        getStack(slot).count -= delCount
    }

    private fun modifyOutput(slot: Int, recipe: HTRecipeBase<HTMachineRecipe.Input>) {
        parent.modifyStack(slot + 4) { stackIn: ItemStack ->
            recipe.getOutput(slot)?.modifyStack(stackIn) ?: stackIn
        }
    }

    //    HTDelegatedInventory    //

    override val parent: HTSidedInventory = HTSidedStorageBuilder(7)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(2, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(3, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(5, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(6, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSided()

    override fun markDirty() {
        super<HTBlockEntityBase>.markDirty()
    }

    //    NamedScreenHandlerFactory    //

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        HTMachineScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos))

    override fun getDisplayName(): Text = machineType.text

    //    PropertyDelegateHolder    //

    override fun getPropertyDelegate(): PropertyDelegate = object : PropertyDelegate {
        override fun get(index: Int): Int = when (index) {
            0 -> ticks
            1 -> tickRate
            else -> throw IndexOutOfBoundsException(index)
        }

        override fun set(index: Int, value: Int) {
        }

        override fun size(): Int = 2
    }
}
