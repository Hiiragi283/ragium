package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.inventory.*
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.util.dropStackAt
import hiiragi283.ragium.common.util.modifyBlockState
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeManager
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
    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val step: Int = state.get(RagiumBlockProperties.LEVEL_7)
        if (step == 7) {
            (world.getBlockEntity(pos) as? HTManualGrinderBlockEntity)?.process(player)
        }
        if (!world.isClient) {
            world.modifyBlockState(pos) { stateIn: BlockState ->
                stateIn.with(RagiumBlockProperties.LEVEL_7, (step + 1) % 8)
            }
        }
        return ActionResult.success(world.isClient)
    }

    //    HTDelegatedInventory    //

    override val parent: HTSidedInventory =
        HTSidedStorageBuilder(1)
            .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
            .buildSided()
    private val matchGetter: RecipeManager.MatchGetter<HTMachineRecipe.Input, HTMachineRecipe> =
        RecipeManager.createCachedMatchGetter(HTMachineType.Single.GRINDER)

    fun process(player: PlayerEntity) {
        val world: World = world ?: return
        val recipe: HTMachineRecipe =
            matchGetter
                .getFirstMatch(
                    HTMachineRecipe.Input(
                        getStack(0),
                        ItemStack.EMPTY,
                        ItemStack.EMPTY,
                        ItemStack.EMPTY,
                    ),
                    world,
                ).map { it.value }
                .getOrNull() ?: return
        dropStackAt(player, recipe.getResult(world.registryManager))
        parent.getStack(0).decrement(recipe.getInput(0)?.count ?: 0)
        world.playSoundAtBlockCenter(
            pos,
            SoundEvents.BLOCK_GRINDSTONE_USE,
            SoundCategory.BLOCKS,
            1.0f,
            1.0f,
            false,
        )
    }

    override fun markDirty() {
        super<HTBaseBlockEntity>.markDirty()
    }
}
