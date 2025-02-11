package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.extension.dropStacks
import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.extension.replaceBlockState
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.machine.HTMachineAccess
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.property.ifPresent
import hiiragi283.ragium.api.recipe.HTGrinderRecipe
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTRecipeGetter
import hiiragi283.ragium.api.recipe.base.HTSingleItemRecipe
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.HTRecipeConverters
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.ItemHandlerHelper

class HTManualGrinderBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntity(RagiumBlockEntityTypes.MANUAL_GRINDER, pos, state),
    HTMachineAccess {
    private val itemHandler: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)

    private val serializer: HTHandlerSerializer = HTHandlerSerializer.ofItem(listOf(itemHandler.createSlot(0)))

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        serializer.writeNbt(nbt, dynamicOps)
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        serializer.readNbt(nbt, dynamicOps)
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
            level.replaceBlockState(pos) { stateIn: BlockState ->
                stateIn.setValue(BlockStateProperties.AGE_7, (step + 1) % 8)
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    private val recipeGetter: HTRecipeGetter.Listed<HTMachineRecipeInput, HTGrinderRecipe> = HTRecipeGetter.Listed(
        HTRecipeConverters::grinder,
    )

    private fun process(level: Level, pos: BlockPos, player: Player) {
        // Find matching recipe
        val stackIn: ItemStack = itemHandler.getStackInSlot(0)
        val input: HTMachineRecipeInput = HTMachineRecipeInput.of(enchantments, stackIn)
        recipeGetter
            .getFirstRecipe(input, level)
            .onSuccess { recipe: HTSingleItemRecipe ->
                // Drop output
                ItemHandlerHelper.giveItemToPlayer(player, recipe.assemble(input, level.registryAccess()))
                // Shrink input
                stackIn.shrink(recipe.input.count())
                // Play sound if present
                RagiumMachineKeys.GRINDER
                    .getProperty()
                    .ifPresent(HTMachinePropertyKeys.SOUND) { level.playSound(null, pos, it, SoundSource.BLOCKS) }
            }.onFailure { _: Throwable ->
                // Drop input
                ItemHandlerHelper.giveItemToPlayer(player, stackIn)
                itemHandler.setStackInSlot(0, ItemStack.EMPTY)
            }
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        itemHandler.dropStacks(level, pos)
    }

    //    HTMachineAccess    //

    override val enchantments: ItemEnchantments = ItemEnchantments.EMPTY
    override val front: Direction = Direction.NORTH
    override val isActive: Boolean = true
    override val levelAccess: Level?
        get() = level
    override val machineKey: HTMachineKey = RagiumMachineKeys.GRINDER
    override val pos: BlockPos
        get() = blockPos
    override var showPreview: Boolean = false
    override val containerData: ContainerData = SimpleContainerData(2)
    override val costModifier: Int = 0

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
    }

    //    HTMachineAccess    //

    override fun getItemHandler(direction: Direction?): IItemHandlerModifiable = HTStorageIO.INPUT.wrapItemHandler(itemHandler)
}
