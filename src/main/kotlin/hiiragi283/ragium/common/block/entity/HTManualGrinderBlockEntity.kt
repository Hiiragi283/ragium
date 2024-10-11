package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.inventory.*
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.api.util.dropStackAt
import hiiragi283.ragium.api.util.modifyBlockState
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.block.BlockState
import net.minecraft.component.ComponentMap
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeEntry
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class HTManualGrinderBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.MANUAL_GRINDER, pos, state),
    HTDelegatedInventory.Simple {
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

    override val parent: HTSimpleInventory =
        HTSidedStorageBuilder(1)
            .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
            .buildSimple()

    private fun process(player: PlayerEntity) {
        val world: World = world ?: return
        val recipe: HTMachineRecipe = world.recipeManager
            .getFirstMatch(
                RagiumRecipeTypes.MACHINE,
                HTMachineRecipe.Input.create(
                    RagiumMachineTypes.Processor.GRINDER,
                    HTMachineTier.PRIMITIVE,
                    getStack(0),
                    ItemStack.EMPTY,
                    ItemStack.EMPTY,
                    ItemStack.EMPTY,
                    ComponentMap.EMPTY,
                ),
                world,
            ).map(RecipeEntry<HTMachineRecipe>::value)
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
        super<HTBlockEntityBase>.markDirty()
    }
}
