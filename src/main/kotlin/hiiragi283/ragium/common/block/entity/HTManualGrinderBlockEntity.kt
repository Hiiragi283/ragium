package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlocks.Properties.LEVEL_7
import hiiragi283.ragium.common.inventory.*
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.recipe.HTMachineRecipeInput
import hiiragi283.ragium.common.util.modifyBlockState
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.recipe.RecipeManager
import net.minecraft.registry.RegistryWrapper
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class HTManualGrinderBlockEntity(pos: BlockPos, state: BlockState) :
    HTBaseBlockEntity(RagiumBlockEntityTypes.MANUAL_GRINDER, pos, state),
    HTDelegatedInventory {
    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        parent.writeNbt(nbt, registryLookup)
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        parent.readNbt(nbt, registryLookup)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val step: Int = state.get(LEVEL_7)
        if (step == 7) {
            (world.getBlockEntity(pos) as? HTManualGrinderBlockEntity)?.process()
        }
        if (!world.isClient) {
            world.modifyBlockState(pos) { stateIn: BlockState ->
                stateIn.with(LEVEL_7, (step + 1) % 8)
            }
        }
        world.playSoundAtBlockCenter(
            pos,
            SoundEvents.BLOCK_GRINDSTONE_USE,
            SoundCategory.BLOCKS,
            1.0f,
            1.0f,
            false,
        )
        return ActionResult.success(world.isClient)
    }

    //    HTDelegatedInventory    //

    override val parent: HTSidedInventory =
        HTSidedStorageBuilder(2)
            .set(0, HTStorageIO.INPUT, HTStorageSides.SIDE)
            .set(1, HTStorageIO.OUTPUT, HTStorageSides.DOWN)
            .buildSided()
    private val matchGetter: RecipeManager.MatchGetter<HTMachineRecipeInput, HTMachineRecipe> =
        RecipeManager.createCachedMatchGetter(HTMachineType.Single.GRINDER)

    fun process() {
        val world: World = world ?: return
        val recipe: HTMachineRecipe =
            matchGetter
                .getFirstMatch(
                    HTMachineRecipeInput(
                        getStack(0),
                        ItemStack.EMPTY,
                        ItemStack.EMPTY,
                        ItemStack.EMPTY,
                    ),
                    world,
                ).map { it.value }
                .getOrNull() ?: return
        val output: ItemStack = recipe.getResult(world.registryManager)
        if (canAcceptOutput(output)) {
            parent.modifyStack(1) { stackIn: ItemStack ->
                when {
                    stackIn.isEmpty -> output
                    ItemStack.areItemsAndComponentsEqual(stackIn, output) -> stackIn.apply { count += output.count }
                    else -> stackIn
                }
            }
            parent.getStack(0).decrement(recipe.inputs[0].count)
        }
    }

    private fun canAcceptOutput(output: ItemStack): Boolean {
        val stackIn: ItemStack = parent.getStack(1)
        return when {
            stackIn.isEmpty -> true
            ItemStack.areItemsAndComponentsEqual(stackIn, output) -> true
            else -> false
        }
    }

    override fun markDirty() {
        super<HTBaseBlockEntity>.markDirty()
    }
}
