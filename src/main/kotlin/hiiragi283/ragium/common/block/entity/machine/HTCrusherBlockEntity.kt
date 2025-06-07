package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.extension.consumeStackInSlot
import hiiragi283.ragium.api.extension.dropStacksAt
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.inventory.HTCrusherMenu
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper

sealed class HTCrusherBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(type, pos, state),
    MenuProvider {
    protected val inputSlot: ItemStackHandler = itemHandler(1)
    protected val outputSlots: ItemStackHandler = itemHandler(4)

    final override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConstantValues.INPUT_SLOT, inputSlot)
        writer.write(RagiumConstantValues.OUTPUT_SLOT, outputSlots)
    }

    final override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConstantValues.INPUT_SLOT, inputSlot)
        reader.read(RagiumConstantValues.OUTPUT_SLOT, outputSlots)
    }

    final override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        super.onRemove(state, level, pos, newState, movedByPiston)
        inputSlot.dropStacksAt(level, pos)
        outputSlots.dropStacksAt(level, pos)
    }

    //    Ticking    //

    protected val recipeCache: HTRecipeCache<SingleRecipeInput, HTCrushingRecipe> =
        HTRecipeCache.simple(RagiumRecipeTypes.CRUSHING.get())

    final override fun onServerTick(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // 200 tickごとに実行する
        if (!canProcess()) return TriState.DEFAULT
        // インプットに一致するレシピを探索する
        val input = SingleRecipeInput(inputSlot.getStackInSlot(0))
        val recipe: HTCrushingRecipe = recipeCache.getFirstRecipe(input, level) ?: return TriState.FALSE
        // エネルギーを消費できるか判定する
        if (network.extractEnergy(6400, true) != 6400) return TriState.FALSE
        // アウトプットに搬出できるか判定する
        for (output: HTItemOutput in recipe.outputs) {
            if (!ItemHandlerHelper.insertItem(outputSlots, output.get(), true).isEmpty) {
                return TriState.FALSE
            }
        }
        // 実際にアウトプットに搬出する
        for (output: HTItemOutput in recipe.outputs) {
            ItemHandlerHelper.insertItem(outputSlots, output.getChancedStack(level.random), false)
        }
        // インプットを減らす
        inputSlot.consumeStackInSlot(0, recipe.ingredient.count())
        // エネルギーを減らす
        network.extractEnergy(6400, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS)
        return TriState.TRUE
    }

    override fun getItemHandler(direction: Direction?): IItemHandler? = when (direction) {
        Direction.UP -> inputSlot
        Direction.DOWN -> outputSlots
        null -> CombinedInvWrapper(inputSlot, outputSlots)
        else -> null
    }

    //    Menu    //

    override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult {
        if (!level.isClientSide) {
            player.openMenu(this, pos)
        }
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    final override fun getDisplayName(): Component = blockState.block.name

    //    Basic    //

    class Basic(pos: BlockPos, state: BlockState) :
        HTCrusherBlockEntity(RagiumBlockEntityTypes.CRUSHER, pos, state),
        MenuProvider {
        override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
            HTCrusherMenu(containerId, playerInventory, blockPos, upgrades, inputSlot, outputSlots)
    }

    //    Advanced    //

    class Advanced(pos: BlockPos, state: BlockState) : HTCrusherBlockEntity(RagiumBlockEntityTypes.ADVANCED_CRUSHER, pos, state) {
        override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null
    }
}
