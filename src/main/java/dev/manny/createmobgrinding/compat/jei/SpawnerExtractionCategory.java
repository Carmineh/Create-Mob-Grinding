package dev.manny.createmobgrinding.compat.jei;

import dev.manny.createmobgrinding.CreateMobGrinding;
import dev.manny.createmobgrinding.registry.ModItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class SpawnerExtractionCategory implements IRecipeCategory<SpawnerExtractionCategory.Recipe> {

    public static final RecipeType<Recipe> TYPE = RecipeType.create(CreateMobGrinding.MOD_ID, "spawner_extraction", Recipe.class);
    
    private final IDrawable background;
    private final IDrawable icon;

    public SpawnerExtractionCategory(IGuiHelper guiHelper) {
        // We can use a simple generic background or a custom one. A blank 100x50 is fine.
        this.background = guiHelper.createBlankDrawable(120, 50);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.IRON_PICKAXE));
    }

    @Override
    public RecipeType<Recipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.createmobgrinding.spawner_extraction");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup focuses) {
        // Input: Vanilla Spawner + Pickaxe
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 16)
               .addItemStack(new ItemStack(Blocks.SPAWNER));
               
        builder.addSlot(RecipeIngredientRole.CATALYST, 40, 16)
               .addItemStack(new ItemStack(Items.IRON_PICKAXE));
               
        // Output: Spawner Chunk
        builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 16)
               .addItemStack(new ItemStack(ModItems.MOB_SPAWNER_CHUNK.get()));
    }

    public static class Recipe {
        // Static dummy recipe
    }
}
