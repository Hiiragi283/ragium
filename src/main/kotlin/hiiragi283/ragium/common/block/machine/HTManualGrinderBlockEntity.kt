package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.machine.HTMachineAccess
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTGrinderRecipe
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeCache
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeContext
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidSlotHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.items.ItemHandlerHelper
import java.util.*

class HTManualGrinderBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntity(RagiumBlockEntityTypes.MANUAL_GRINDER, pos, state),
    HTMachineAccess,
    HTFluidSlotHandler.Empty {
    private val inputSlot: HTItemSlot = HTItemSlot.Builder().setCallback(this::setChanged).build("item_input")

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        inputSlot.writeNbt(nbt, registryOps)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        inputSlot.readNbt(nbt, registryOps)
    }

    override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult {
        if (player.isFakePlayer) {
            process(level, pos, player)
            return InteractionResult.sidedSuccess(level.isClientSide)
        }
        val step: Int = state.getOrNull(BlockStateProperties.AGE_7) ?: return InteractionResult.FAIL
        if (step == 7) {
            process(level, pos, player)
        }
        if (!level.isClientSide) {
            level.setBlock(pos, state.cycle(BlockStateProperties.AGE_7), 2)
        }
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    private val recipeCache: HTMachineRecipeCache<HTGrinderRecipe> = HTMachineRecipeCache(HTRecipeTypes.GRINDER)

    private fun process(level: Level, pos: BlockPos, player: Player) {
        // Find matching recipe
        val context: HTMachineRecipeContext = HTMachineRecipeContext.builder().addInput(0, inputSlot).build()
        recipeCache
            .getFirstRecipe(context, level)
            .onSuccess { recipe: HTGrinderRecipe ->
                // Drop output
                ItemHandlerHelper.giveItemToPlayer(player, recipe.itemOutput.get())
                // Shrink input
                inputSlot.extract(recipe.input.count, false)
                // Play sound
                level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS)
            }.onFailure { _: Throwable ->
                // Drop input
                inputSlot.dropStack(player)
            }
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        dropStackAt(level, pos, inputSlot.stack)
    }

    //    HTMachineAccess    //

    override val levelAccess: Level?
        get() = level
    override val machineType: HTMachineType = HTMachineType.GRINDER
    override val pos: BlockPos
        get() = blockPos
    override val containerData: ContainerData = SimpleContainerData(2)
    override val costModifier: Int = 0
    override val enchantments: ItemEnchantments = ItemEnchantments.EMPTY

    override fun onUpdateEnchantment(newEnchantments: ItemEnchantments) {
    }

    override fun getErrorMessage(): String? = null

    //    Item    //

    override fun getItemSlot(slot: Int): HTItemSlot? = inputSlot

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = HTStorageIO.INPUT

    override fun getSlots(): Int = 1

    override val ownerUUID: UUID? = null
}
