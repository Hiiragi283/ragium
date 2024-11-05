package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.modifyBlockState
import hiiragi283.ragium.api.inventory.*
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
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
            process(player)
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
        HTStorageBuilder(1)
            .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
            .buildSimple()

    private val recipeCache: HTRecipeCache<HTMachineInput, HTMachineRecipe> =
        HTRecipeCache(RagiumRecipeTypes.MACHINE)

    private fun process(player: PlayerEntity) {
        val world: World = world ?: return
        val recipe: HTMachineRecipe = recipeCache
            .getFirstMatch(
                HTMachineInput.create(
                    RagiumMachineKeys.GRINDER,
                    HTMachineTier.PRIMITIVE,
                ) { add(getStack(0)) },
                world,
            ).getOrNull()
            ?.value
            ?: return
        dropStackAt(player, recipe.getResult(world.registryManager))
        parent.getStack(0).decrement(recipe.itemInputs[0].amount)
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
