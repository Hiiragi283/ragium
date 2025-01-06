package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.HTBlockEntityBase
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.extension.variantStack
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTTieredFluidStorage
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTManualMixerBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.MANUAL_MIXER, pos, state),
    SidedStorageBlockEntity {
    private val recipeCache: HTRecipeCache<HTMachineInput, HTMachineRecipe> =
        HTRecipeCache(RagiumRecipeTypes.MACHINE)

    override fun onRightClicked(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        if (fluidStorage.interactWithFluidStorage(player)) {
            return ActionResult.success(world.isClient)
        }
        process(player)
        return ActionResult.success(world.isClient)
    }

    private fun process(player: PlayerEntity) {
        val world: World = world ?: return
        val stackMain: ItemStack = player.getStackInHand(Hand.MAIN_HAND)
        val stackOff: ItemStack = player.getStackInHand(Hand.OFF_HAND)
        val recipe: HTMachineRecipe = recipeCache
            .getFirstMatch(
                HTMachineInput.create(
                    RagiumMachineKeys.MIXER,
                    HTMachineTier.PRIMITIVE,
                ) {
                    add(stackMain)
                    add(stackOff)
                    add(fluidStorage.variantStack)
                },
                world,
            ).getOrNull() ?: return
        dropStackAt(player, recipe.getResult(world.registryManager))
        stackMain.decrement(recipe.getItemIngredient(0)?.count ?: 0)
        stackOff.decrement(recipe.getItemIngredient(1)?.count ?: 0)
        recipe.getFluidIngredient(0)?.onConsume(fluidStorage)
        RagiumMachineKeys.MIXER.getEntryOrNull()?.ifPresent(HTMachinePropertyKeys.SOUND) {
            world.playSound(null, pos, it, SoundCategory.BLOCKS)
        }
    }

    //    SidedStorageBlockEntity    //

    private val fluidStorage = HTTieredFluidStorage(HTMachineTier.PRIMITIVE, HTStorageIO.INPUT, null, this::markDirty)

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage
}
