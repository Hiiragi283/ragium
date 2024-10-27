package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.extract
import hiiragi283.ragium.api.extension.resourceAmount
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTIngredient
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class HTManualMixerBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.MANUAL_MIXER, pos, state),
    SidedStorageBlockEntity {
    private val recipeCache: HTRecipeCache<HTMachineInput, HTMachineRecipe> =
        HTRecipeCache(RagiumRecipeTypes.MACHINE)

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
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
                    RagiumMachineTypes.Processor.MIXER,
                    HTMachineTier.PRIMITIVE,
                ) {
                    add(stackMain)
                    add(stackOff)
                    add(fluidStorage.resourceAmount)
                },
                world,
            ).getOrNull()
            ?.value
            ?: return
        dropStackAt(player, recipe.getResult(world.registryManager))
        stackMain.decrement(recipe.itemInputs.getOrNull(0)?.amount ?: 0)
        stackOff.decrement(recipe.itemInputs.getOrNull(1)?.amount ?: 0)
        extractFluid(recipe)
        world.playSoundAtBlockCenter(
            pos,
            SoundEvents.ITEM_BUCKET_EMPTY,
            SoundCategory.BLOCKS,
            1.0f,
            1.0f,
            false,
        )
    }

    private fun extractFluid(recipe: HTMachineRecipe) {
        val fluidInput: HTIngredient.Fluid = recipe.fluidInputs.getOrNull(0) ?: return
        useTransaction { transaction: Transaction ->
            val foundVariant: FluidVariant = StorageUtil.findExtractableResource(
                fluidStorage,
                { fluidInput.test(it, fluidStorage.amount) },
                transaction,
            ) ?: return@useTransaction
            if (fluidInput.test(foundVariant, fluidInput.amount)) {
                val extracted: Long = fluidStorage.extract(foundVariant, fluidInput.amount, transaction)
                if (extracted > 0) {
                    transaction.commit()
                } else {
                    transaction.abort()
                }
            }
        }
    }

    //    SidedStorageBlockEntity    //

    private val fluidStorage: SingleFluidStorage = SingleFluidStorage.withFixedCapacity(FluidConstants.BUCKET * 4) {}

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage
}
