package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.isOf
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineTypes
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.block.BlockState
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class HTManualForgeBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntityBase(RagiumBlockEntityTypes.MANUAL_FORGE, pos, state) {
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
        if (!stackMain.isOf(RagiumContents.Misc.FORGE_HAMMER)) return
        val stackOff: ItemStack = player.getStackInHand(Hand.OFF_HAND)
        val recipe: HTMachineRecipe = recipeCache
            .getFirstMatch(
                HTMachineInput.create(
                    RagiumMachineTypes.Processor.METAL_FORMER,
                    HTMachineTier.PRIMITIVE,
                ) { add(stackOff) },
                world,
            ).getOrNull()
            ?.value
            ?: return
        dropStackAt(player, recipe.getResult(world.registryManager))
        stackMain.damage(1, player, EquipmentSlot.MAINHAND)
        stackOff.decrement(recipe.itemInputs.getOrNull(0)?.amount ?: 0)
        world.playSoundAtBlockCenter(
            pos,
            SoundEvents.BLOCK_ANVIL_USE,
            SoundCategory.BLOCKS,
            1.0f,
            1.0f,
            false,
        )
    }
}
